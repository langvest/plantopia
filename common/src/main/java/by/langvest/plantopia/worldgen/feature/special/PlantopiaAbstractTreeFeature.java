package by.langvest.plantopia.worldgen.feature.special;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
public abstract class PlantopiaAbstractTreeFeature extends Feature<TreeConfiguration> {
    public static final int DEFAULT_BLOCK_UPDATE_FLAGS = Block.UPDATE_NEIGHBORS | Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE;

    public PlantopiaAbstractTreeFeature(Codec<TreeConfiguration> codec) {
        super(codec);
    }

    protected TreeBlockPool createBlockPool(FeaturePlaceContext<TreeConfiguration> context) {
        return new TreeBlockPool(
            Sets.newHashSet(),
            Sets.newHashSet(),
            Sets.newHashSet(),
            Sets.newHashSet()
        );
    }

    protected TreeBlockSetter createBlockSetter(FeaturePlaceContext<TreeConfiguration> context, TreeBlockPool pool) {
        return new TreeBlockSetter(
            createRootSetter(context, pool),
            createTrunkSetter(context, pool),
            createFoliageSetter(context, pool),
            createDecorSetter(context, pool)
        );
    }

    protected BiConsumer<BlockPos, BlockState> createRootSetter(FeaturePlaceContext<TreeConfiguration> context, TreeBlockPool pool) {
        return (pos, state) -> {
            pool.root().add(pos.immutable());
            setBlock(context.level(), pos, state);
        };
    }

    protected BiConsumer<BlockPos, BlockState> createTrunkSetter(FeaturePlaceContext<TreeConfiguration> context, TreeBlockPool pool) {
        return (pos, state) -> {
            pool.trunk().add(pos.immutable());
            setBlock(context.level(), pos, state);
        };
    }

    protected FoliagePlacer.FoliageSetter createFoliageSetter(FeaturePlaceContext<TreeConfiguration> context, TreeBlockPool pool) {
        return new FoliagePlacer.FoliageSetter() {
            public void set(BlockPos pos, BlockState state) {
                pool.foliage().add(pos.immutable());
                setBlock(context.level(), pos, state);
            }

            public boolean isSet(BlockPos pos) {
                return pool.foliage().contains(pos);
            }
        };
    }

    protected BiConsumer<BlockPos, BlockState> createDecorSetter(FeaturePlaceContext<TreeConfiguration> context, TreeBlockPool pool) {
        return (pos, state) -> {
            pool.decor().add(pos.immutable());
            setBlock(context.level(), pos, state);
        };
    }

    protected record TreeBlockPool(
        Set<BlockPos> root,
        Set<BlockPos> trunk,
        Set<BlockPos> foliage,
        Set<BlockPos> decor
    ) {}

    protected record TreeBlockSetter(
        BiConsumer<BlockPos, BlockState> root,
        BiConsumer<BlockPos, BlockState> trunk,
        FoliagePlacer.FoliageSetter foliage,
        BiConsumer<BlockPos, BlockState> decor
    ) {}

    @FunctionalInterface
    protected interface TreeModifier {
        boolean apply(FeaturePlaceContext<TreeConfiguration> context, TreeBlockPool pool, TreeBlockSetter setter);
    }

    protected abstract List<TreeModifier> getTreePipeline(FeaturePlaceContext<TreeConfiguration> context);

    @Override
    public boolean place(FeaturePlaceContext<TreeConfiguration> context) {
        var pipeline = getTreePipeline(context);
        if (pipeline.isEmpty()) return false;

        var pool = createBlockPool(context);
        var setter = createBlockSetter(context, pool);

        for (var modifier : pipeline) {
            if (!modifier.apply(context, pool, setter)) {
                return false;
            }
        }

        return true;
    }

    protected boolean makeStructure(FeaturePlaceContext<TreeConfiguration> context, TreeBlockPool pool, TreeBlockSetter setter) {
        var config = context.config();
        var random = context.random();
        var origin = context.origin();
        var level = context.level();

        int treeHeight = config.trunkPlacer.getTreeHeight(random);
        int foliageHeight = config.foliagePlacer.foliageHeight(random, treeHeight, config);
        int foliageRadius = config.foliagePlacer.foliageRadius(random, treeHeight - foliageHeight);

        var trunkOrigin = config.rootPlacer.map(rootPlacer -> rootPlacer.getTrunkOrigin(origin, random)).orElse(origin);

        int minY = Math.min(origin.getY(), trunkOrigin.getY());
        int maxY = Math.max(origin.getY(), trunkOrigin.getY()) + treeHeight + 1;

        if (minY < level.getMinBuildHeight() + 1 || maxY > level.getMaxBuildHeight()) {
            return false;
        }

        var minClippedHeight = config.minimumSize.minClippedHeight();
        int maxFreeTreeHeight = getMaxFreeTreeHeight(level, treeHeight, trunkOrigin, config);

        if (maxFreeTreeHeight < treeHeight && (minClippedHeight.isEmpty() || maxFreeTreeHeight < minClippedHeight.getAsInt())) {
            return false;
        }

        if (config.rootPlacer.isPresent() && !config.rootPlacer.get().placeRoots(level, setter.root(), random, origin, trunkOrigin, config)) {
            return false;
        }

        var foliageAttachments = config.trunkPlacer.placeTrunk(level, setter.trunk(), random, maxFreeTreeHeight, trunkOrigin, config);
        foliageAttachments.forEach(foliageAttachment -> config.foliagePlacer.createFoliage(level, setter.foliage(), random, config, maxFreeTreeHeight, foliageAttachment, foliageHeight, foliageRadius));
        return true;
    }

    protected int getMaxFreeTreeHeight(LevelSimulatedReader level, int trunkHeight, BlockPos topPosition, TreeConfiguration config) {
        var mutableBlockPos = new BlockPos.MutableBlockPos();

        for (int dy = 0; dy <= trunkHeight + 1; dy++) {
            int sizeAtHeight = config.minimumSize.getSizeAtHeight(trunkHeight, dy);

            for (int dx = -sizeAtHeight; dx <= sizeAtHeight; dx++) {
                for (int dz = -sizeAtHeight; dz <= sizeAtHeight; dz++) {
                    mutableBlockPos.setWithOffset(topPosition, dx, dy, dz);
                    if (!config.trunkPlacer.isFree(level, mutableBlockPos) || !config.ignoreVines && isVine(level, mutableBlockPos)) {
                        return dy - 2;
                    }
                }
            }
        }

        return trunkHeight;
    }

    protected boolean applyDecorators(FeaturePlaceContext<TreeConfiguration> context, TreeBlockPool pool, TreeBlockSetter setter) {
        var level = context.level();
        var random = context.random();
        var config = context.config();

        if (config.decorators.isEmpty()) return true;

        var decoratorContext = new TreeDecorator.Context(level, setter.decor(), random, pool.trunk(), pool.foliage(), pool.root());
        for (var decorator : config.decorators) {
            decorator.place(decoratorContext);
        }

        return true;
    }

    public static boolean isVine(LevelSimulatedReader level, BlockPos pos) {
        return level.isStateAtPosition(pos, state -> state.is(Blocks.VINE));
    }

    @Override
    protected void setBlock(LevelWriter level, BlockPos pos, BlockState state) {
        level.setBlock(pos, state, getBlockUpdateFlags());
    }

    protected int getBlockUpdateFlags() {
        return DEFAULT_BLOCK_UPDATE_FLAGS;
    }

    protected boolean updateLeaves(FeaturePlaceContext<TreeConfiguration> context, TreeBlockPool pool, TreeBlockSetter setter) {
        var level = context.level();

        if (pool.foliage().isEmpty()) return true;
        if (pool.trunk().isEmpty()) return false;

        return BoundingBox.encapsulatingPositions(
            Iterables.concat(pool.root(), pool.trunk(), pool.foliage(), pool.decor())
        ).map(box -> {
            var shape = getUpdatedLeavesShape(level, box, pool.trunk(), Sets.union(pool.root(), pool.decor()));
            StructureTemplate.updateShapeAtEdge(level, Block.UPDATE_ALL, shape, box.minX(), box.minY(), box.minZ());
            return true;
        }).orElse(false);
    }

    protected DiscreteVoxelShape getUpdatedLeavesShape(LevelAccessor level, BoundingBox box, Set<BlockPos> trunkPool, Set<BlockPos> skipPool) {
        var shape = new BitSetDiscreteVoxelShape(box.getXSpan(), box.getYSpan(), box.getZSpan());
        List<Set<BlockPos>> distanceQueue = Lists.newArrayList();

        for (int i = 0; i < 7; ++i) {
            distanceQueue.add(Sets.newHashSet());
        }

        for (var pos : skipPool) {
            if (box.isInside(pos)) {
                shape.fill(pos.getX() - box.minX(), pos.getY() - box.minY(), pos.getZ() - box.minZ());
            }
        }

        var mutablePos = new BlockPos.MutableBlockPos();
        int currentDistance = 0;
        distanceQueue.get(0).addAll(trunkPool);

        while (true) {
            while (currentDistance >= 7 || !distanceQueue.get(currentDistance).isEmpty()) {
                if (currentDistance >= 7) {
                    return shape;
                }

                var iterator = distanceQueue.get(currentDistance).iterator();
                var currentPos = iterator.next();
                iterator.remove();

                if (!box.isInside(currentPos)) {
                    continue;
                }

                if (currentDistance != 0) {
                    BlockState leafState = level.getBlockState(currentPos);
                    setBlock(level, currentPos, leafState.setValue(BlockStateProperties.DISTANCE, currentDistance));
                }

                shape.fill(currentPos.getX() - box.minX(), currentPos.getY() - box.minY(), currentPos.getZ() - box.minZ());

                for (var direction : Direction.values()) {
                    mutablePos.setWithOffset(currentPos, direction);

                    if (!box.isInside(mutablePos)) {
                        continue;
                    }

                    int relativeX = mutablePos.getX() - box.minX();
                    int relativeY = mutablePos.getY() - box.minY();
                    int relativeZ = mutablePos.getZ() - box.minZ();

                    if (shape.isFull(relativeX, relativeY, relativeZ)) {
                        continue;
                    }

                    var neighborState = level.getBlockState(mutablePos);
                    var optionalDistance = LeavesBlock.getOptionalDistanceAt(neighborState);

                    if (optionalDistance.isEmpty()) {
                        continue;
                    }

                    int nextDistance = Math.min(optionalDistance.getAsInt(), currentDistance + 1);
                    if (nextDistance >= 7) {
                        continue;
                    }

                    distanceQueue.get(nextDistance).add(mutablePos.immutable());
                    currentDistance = Math.min(currentDistance, nextDistance);
                }
            }

            currentDistance++;
        }
    }
}
