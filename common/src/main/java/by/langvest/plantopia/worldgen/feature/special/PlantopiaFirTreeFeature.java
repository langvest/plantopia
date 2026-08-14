package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.block.PlantopiaDirtUtils;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaFirTreeConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class PlantopiaFirTreeFeature extends PlantopiaAbstractTreeFeature<PlantopiaFirTreeConfiguration> {
    public PlantopiaFirTreeFeature(Codec<PlantopiaFirTreeConfiguration> codec) {
        super(codec);
    }

    @Override
    protected List<TreeModifier> getTreePipeline(FeaturePlaceContext<PlantopiaFirTreeConfiguration> context) {
        var config = context.config();

        return List.of(
            makeFirStructure(config),
            applyDecorators(config.decorators()),
            updateLeaves()
        );
    }

    @Contract(pure = true)
    protected static @NotNull TreeModifier makeFirStructure(PlantopiaFirTreeConfiguration config) {
        return (context, pool, setter) -> {
            var level = context.level();
            var random = context.random();
            var origin = context.origin();

            int trunkHeight = config.trunkHeight().sample(random);
            int foliageHeight = config.foliageHeight().sample(random, trunkHeight);
            int foliageOffset = config.foliageOffset().sample(random);
            int maxTrunkWidth = config.trunkWidth().sample(random);
            int totalTreeHeight = trunkHeight + 1 + Math.max(foliageOffset, -1);
            int baseTrunkHeight = totalTreeHeight - foliageHeight;
            var trunkWidthProvider = createTreeTrunkWidthProvider(totalTreeHeight, maxTrunkWidth);

            if (!checkSpace(level, origin, totalTreeHeight, baseTrunkHeight, trunkWidthProvider)) {
                return false;
            }

            var foliageAttachment = placeTrunk(level, random, origin, trunkHeight, trunkWidthProvider, setter, config);
            placeFoliage(random, foliageAttachment, foliageHeight, foliageOffset, totalTreeHeight, trunkWidthProvider, setter, config);
            return true;
        };
    }

    protected static int calculateTrunkWidth(int deltaHeight, int totalHeight, int maxTrunkWidth) {
        int width = (maxTrunkWidth * (totalHeight - deltaHeight) / totalHeight) + 1;
        return Mth.clamp(width, 1, maxTrunkWidth);
    }

    protected record TreeTrunkWidthInfo(int width, int begin, int end) {
        @Contract("_ -> new")
        protected static @NotNull TreeTrunkWidthInfo of(int width) {
            return new TreeTrunkWidthInfo(width, Mth.ceil(0.25D - width / 2.0D), Mth.floor(0.25D + width / 2.0D));
        }
    }

    protected interface TreeTrunkWidthProvider {
        TreeTrunkWidthInfo provide(int height);
    }

    @Contract(pure = true)
    protected static @NotNull TreeTrunkWidthProvider createTreeTrunkWidthProvider(int totalTreeHeight, int maxTrunkWidth) {
        return height -> TreeTrunkWidthInfo.of(calculateTrunkWidth(height, totalTreeHeight, maxTrunkWidth));
    }

    protected static boolean checkSpace(LevelAccessor level, BlockPos trunkOrigin, int totalTreeHeight, int baseTrunkHeight, TreeTrunkWidthProvider trunkWidthProvider) {
        if (trunkOrigin.getY() + totalTreeHeight >= level.getMaxBuildHeight()) {
            return false;
        }

        var mutablePos = new BlockPos.MutableBlockPos();

        for (int dy = 0; dy <= totalTreeHeight; dy++) {
            var trunkInfo = trunkWidthProvider.provide(dy);
            int scanBegin = (dy <= baseTrunkHeight ? trunkInfo.begin : trunkInfo.begin - 1);
            int sacnEnd = (dy <= baseTrunkHeight ? trunkInfo.end : trunkInfo.end + 1);

            for (int dx = scanBegin; dx <= sacnEnd; dx++) {
                for (int dz = scanBegin; dz <= sacnEnd; dz++) {
                    mutablePos.setWithOffset(trunkOrigin, dx, dy, dz);

                    if (!TreeFeature.validTreePos(level, mutablePos)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    protected static FoliagePlacer.@NotNull FoliageAttachment placeTrunk(
        WorldGenLevel level,
        RandomSource random,
        BlockPos origin,
        int trunkHeight,
        TreeTrunkWidthProvider trunkWidthProvider,
        TreeBlockSetter setter,
        PlantopiaFirTreeConfiguration config
    ) {
        var mutablePos = new BlockPos.MutableBlockPos();

        for (int dy = 0; dy < trunkHeight; dy++) {
            var trunkInfo = trunkWidthProvider.provide(dy);

            for (int dx = trunkInfo.begin; dx <= trunkInfo.end; dx++) {
                for (int dz = trunkInfo.begin; dz <= trunkInfo.end; dz++) {
                    if (dy == 0) {
                        mutablePos.setWithOffset(origin, dx, -1, dz);
                        var currentState = level.getBlockState(mutablePos);
                        if (PlantopiaDirtUtils.canConvertToDirt(currentState)) {
                            setter.root().accept(mutablePos, PlantopiaDirtUtils.getCorrespondingState(currentState, Blocks.DIRT.defaultBlockState()));
                        }
                    }

                    mutablePos.setWithOffset(origin, dx, dy, dz);
                    setter.trunk().accept(mutablePos, config.trunkProvider().getState(random, mutablePos));
                }
            }
        }

        return new FoliagePlacer.FoliageAttachment(mutablePos.set(origin.getX(), mutablePos.getY() + 1, origin.getZ()), 0, false);
    }

    protected static void placeFoliage(
        RandomSource random,
        FoliagePlacer.FoliageAttachment attachment,
        int foliageHeight,
        int foliageOffset,
        int totalTreeHeight,
        TreeTrunkWidthProvider trunkWidthProvider,
        TreeBlockSetter setter,
        PlantopiaFirTreeConfiguration config
    ) {
        var foliageApex = attachment.pos().offset(0, foliageOffset, 0);
        var mutablePos = new BlockPos.MutableBlockPos();

        for (int row = 0; row < foliageHeight; row++) {
            mutablePos.setWithOffset(foliageApex, 0, -row, 0);
            var trunkInfo = trunkWidthProvider.provide(totalTreeHeight - row);
            int radius = Math.min(Math.min((row + 2) / 3, 3 + (foliageHeight - row)), 6);
            placeFoliageRow(random, mutablePos, row, radius, trunkInfo, setter, config);
        }
    }

    protected static void placeFoliageRow(
        RandomSource random,
        BlockPos center,
        int row,
        int radius,
        TreeTrunkWidthInfo trunkInfo,
        TreeBlockSetter setter,
        PlantopiaFirTreeConfiguration config
    ) {
        if (radius == 0) {
            setter.foliage().setSafely(center, config.foliageProvider().getState(random, center));
            return;
        }

        if (radius <= 3) {
            int patternRadius = (row % 2 == 0) ? radius : radius / 2;
            placeLeafRow(random, center, patternRadius, trunkInfo, setter, config);
            return;
        }

        if (row % 2 == 0) {
            placeBranchRow(random, center.offset(trunkInfo.begin, 0, trunkInfo.begin), radius, Direction.NORTH, setter, config);
            placeBranchRow(random, center.offset(trunkInfo.end, 0, trunkInfo.begin), radius, Direction.EAST, setter, config);
            placeBranchRow(random, center.offset(trunkInfo.end, 0, trunkInfo.end), radius, Direction.SOUTH, setter, config);
            placeBranchRow(random, center.offset(trunkInfo.begin, 0, trunkInfo.end), radius, Direction.WEST, setter, config);
        }
    }

    protected static void placeLeafRow(
        RandomSource random,
        BlockPos center,
        int range,
        TreeTrunkWidthInfo trunkInfo,
        TreeBlockSetter setter,
        PlantopiaFirTreeConfiguration config
    ) {
        var mutablePos = new BlockPos.MutableBlockPos();

        int begin = trunkInfo.begin - range;
        int end = trunkInfo.end + range;

        for (int dx = begin; dx <= end; dx++) {
            for (int dz = begin; dz <= end; dz++) {

                if ((range > 0) && (dx == begin || dx == end) && (dz == begin || dz == end)) {
                    continue;
                }

                int distFromTrunk =
                    (dx < 0 ? trunkInfo.begin - dx : dx - trunkInfo.end)
                        + (dz < 0 ? trunkInfo.begin - dz : dz - trunkInfo.end);

                if (distFromTrunk < 4 || (distFromTrunk == 4 && random.nextInt(2) == 0)) {
                    mutablePos.set(center.getX() + dx, center.getY(), center.getZ() + dz);
                    setter.foliage().setSafely(mutablePos, config.foliageProvider().getState(random, mutablePos));
                }
            }
        }
    }

    protected static void placeBranchRow(
        RandomSource random,
        BlockPos branchOrigin,
        int distance,
        Direction direction,
        TreeBlockSetter setter,
        PlantopiaFirTreeConfiguration config
    ) {
        var mutablePos = new BlockPos.MutableBlockPos();
        var sideways = direction.getCounterClockWise();

        for (int i = 1; i <= distance; i++) {
            var branchPos = branchOrigin.relative(direction, i);

            if (distance - i > 2) {
                setter.foliage().setSafely(branchPos.above(), config.foliageProvider().getState(random, mutablePos));
                setter.foliage().setSafely(branchPos.above().relative(sideways, 1), config.foliageProvider().getState(random, branchPos.above().relative(sideways, 1)));
                setter.foliage().setSafely(branchPos.above().relative(sideways, -1), config.foliageProvider().getState(random, branchPos.above().relative(sideways, -1)));

                var logState = config.trunkProvider().getState(random, branchPos);

                if (logState.hasProperty(BlockStateProperties.AXIS)) {
                    logState = logState.setValue(BlockStateProperties.AXIS, direction.getAxis());
                }

                setter.trunk().setSafely(branchPos, logState);
            }

            int leafRange = (i == 1 || i == distance) ? 1 : 2;
            for (int j = -leafRange; j <= leafRange; j++) {
                if (i < distance || random.nextInt(2) == 0) {
                    mutablePos.set(branchPos).move(sideways, j);
                    setter.foliage().setSafely(mutablePos, config.foliageProvider().getState(random, mutablePos));
                }
            }
        }
    }
}
