package by.langvest.plantopia.worldgen.feature.foliageplacer;

import by.langvest.plantopia.worldgen.util.PlantopiaTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;
import static by.langvest.plantopia.worldgen.util.PlantopiaProviderUtils.weightedListInt;

@ParametersAreNonnullByDefault
public abstract class PlantopiaFoliagePlacer extends FoliagePlacer {
    protected static final IntProvider DEFAULT_HANGING_LEAVES_DEPTH = weightedListInt(values -> values
        .add(ConstantInt.of(2), 2)
        .add(ConstantInt.of(1), 1)
    );

    public PlantopiaFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    protected record PlaceContext(
        LevelSimulatedReader level,
        FoliageSetter setter,
        RandomSource random,
        FoliageAttachment attachment,
        TreeConfiguration config,
        int maxFreeTreeHeight,
        int height,
        int radius,
        int offset
    ) {
        protected @NotNull BlockState getFoliageState(BlockPos pos) {
            return config.foliageProvider.getState(random, pos);
        }

        protected boolean tryPlaceLeaf(BlockPos pos, BlockState state) {
            if (!TreeFeature.validTreePos(level, pos)) return false;
            setter.set(pos, copyWaterloggedFrom(level, pos, state));
            return true;
        }
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        var context = new PlaceContext(level, blockSetter, random, attachment, config, maxFreeTreeHeight, foliageHeight, foliageRadius, offset);
        createFoliage(context);
    }

    protected abstract void createFoliage(PlaceContext context);

    protected void placeRow(PlaceContext context, int dy, int range, @Nullable PlantopiaTemplate template, @Nullable LeafModifier modifier) {
        if (range < 0) return;

        var attachment = context.attachment();
        var random = context.random();
        boolean isLarge = attachment.doubleTrunk();
        int i = isLarge ? 1 : 0;
        var mutablePos = new BlockPos.MutableBlockPos();

        for (int dx = -range; dx <= range + i; dx++) {
            for (int dz = -range; dz <= range + i; dz++) {
                int templateDx = dx;
                int templateDz = dz;

                if (isLarge) {
                    if (dx >= 1) templateDx = dx - 1;
                    if (dz >= 1) templateDz = dz - 1;
                }

                if (template == null || template.test(random, templateDx, templateDz, range)) {
                    mutablePos.setWithOffset(attachment.pos(), dx, dy, dz);
                    var state = context.getFoliageState(mutablePos);

                    if (modifier != null) {
                        state = modifier.apply(context, mutablePos, state, dx, dz, templateDx, templateDz, range);
                    }

                    if (state != null) {
                        context.tryPlaceLeaf(mutablePos, state);
                    }
                }
            }
        }
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int dx, int dy, int dz, int range, boolean large) {
        return false;
    }

    @FunctionalInterface
    protected interface LeafModifier {
        @Nullable BlockState apply(PlaceContext context, BlockPos pos, BlockState state, int realDx, int realDz, int templateDx, int templateDz, int range);
    }

    @Contract(pure = true)
    protected static @NotNull LeafModifier revealMushroomInsides() {
        return (context, pos, state, realDx, realDz, templateDx, templateDz, range) -> {
            if (state.getBlock() instanceof HugeMushroomBlock) {
                if (templateDx > 0) state = state.setValue(BlockStateProperties.WEST, false);
                if (templateDx < 0) state = state.setValue(BlockStateProperties.EAST, false);
                if (templateDz > 0) state = state.setValue(BlockStateProperties.NORTH, false);
                if (templateDz < 0) state = state.setValue(BlockStateProperties.SOUTH, false);
            }
            return state;
        };
    }

    @FunctionalInterface
    protected interface HangingLeafProvider {
        @Nullable BlockState provide(PlaceContext context, BlockPos pos, BlockState state, int currentDepth, int totalDepth);
    }

    @Contract(pure = true)
    protected static @NotNull LeafModifier placeHangingLeaves(float chance, IntProvider depth, HangingLeafProvider hangingLeafProvider) {
        return (context, pos, state, realDx, realDz, templateDx, templateDz, range) -> {
            var random = context.random();
            if (random.nextFloat() < chance) {
                var mutablePos = pos.mutable();
                int totalDepth = depth.sample(random);

                for (int currentDepth = 1; currentDepth <= totalDepth; currentDepth++) {
                    mutablePos.move(Direction.DOWN);
                    var hangingState = hangingLeafProvider.provide(context, mutablePos, state, currentDepth, totalDepth);
                    if (hangingState != null) {
                        context.tryPlaceLeaf(mutablePos, hangingState);
                    }
                }
            }
            return state;
        };
    }

    @Contract(pure = true)
    protected static @NotNull LeafModifier placeHangingLeaves(float chance) {
        return placeHangingLeaves(chance, DEFAULT_HANGING_LEAVES_DEPTH, (context, pos, state, currentDepth, totalDepth) -> context.getFoliageState(pos));
    }

    @Contract(pure = true)
    protected static @NotNull LeafModifier placeHangingLeaves(float chance, int depth) {
        return placeHangingLeaves(chance, ConstantInt.of(depth), (context, pos, state, currentDepth, totalDepth) -> context.getFoliageState(pos));
    }

    @Contract(pure = true)
    protected static @NotNull LeafModifier filteredByTemplate(PlantopiaTemplate template, LeafModifier modifier) {
        return (context, pos, state, realDx, realDz, templateDx, templateDz, range) -> {
            if (template.test(context.random(), templateDx, templateDz, range)) {
                return modifier.apply(context, pos, state, realDx, realDz, templateDx, templateDz, range);
            }
            return state;
        };
    }
}
