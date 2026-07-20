package by.langvest.plantopia.worldgen.feature.foliageplacer;

import by.langvest.plantopia.worldgen.util.PlantopiaTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
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

@ParametersAreNonnullByDefault
public abstract class PlantopiaFoliagePlacer extends FoliagePlacer {
    public PlantopiaFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    protected void placeRow(
        LevelSimulatedReader level,
        FoliageSetter foliageSetter,
        RandomSource random,
        FoliageAttachment attachment,
        TreeConfiguration config,
        int dy,
        int range,
        PlantopiaTemplate template,
        @Nullable BlockStateModifier modifier
    ) {
        boolean large = attachment.doubleTrunk();
        int i = large ? 1 : 0;
        var mutablePos = new BlockPos.MutableBlockPos();

        for (int dx = -range; dx <= range + i; dx++) {
            for (int dz = -range; dz <= range + i; dz++) {
                int templateDx = dx;
                int templateDz = dz;

                if (large) {
                    if (dx >= 1) templateDx = dx - 1;
                    if (dz >= 1) templateDz = dz - 1;
                }

                if (template.test(random, templateDx, templateDz, range)) {
                    mutablePos.setWithOffset(attachment.pos(), dx, dy, dz);
                    var state = config.foliageProvider.getState(random, mutablePos);

                    if (modifier != null) {
                        state = modifier.modify(level, state, mutablePos, random, dx, dz, templateDx, templateDz, range);
                    }

                    if (state != null) {
                        tryPlaceLeaf(level, mutablePos, state, foliageSetter, random);
                    }
                }
            }
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    protected boolean tryPlaceLeaf(LevelSimulatedReader level, BlockPos pos, BlockState state, FoliageSetter foliageSetter, RandomSource random) {
        if (!TreeFeature.validTreePos(level, pos)) return false;
        foliageSetter.set(pos, copyWaterloggedFrom(level, pos, state));
        return true;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int dx, int dy, int dz, int range, boolean large) {
        return false;
    }

    @FunctionalInterface
    protected interface BlockStateModifier {
        BlockState modify(LevelSimulatedReader level, BlockState state, BlockPos pos, RandomSource random, int realDx, int realDz, int templateDx, int templateDz, int range);
    }

    @Contract(pure = true)
    protected static @NotNull BlockStateModifier revealMushroomInsides() {
        return (level, state, pos, random, realDx, realDz, templateDx, templateDz, range) -> {
            if (state.getBlock() instanceof HugeMushroomBlock) {
                if (templateDx > 0) state = state.setValue(BlockStateProperties.WEST, false);
                if (templateDx < 0) state = state.setValue(BlockStateProperties.EAST, false);
                if (templateDz > 0) state = state.setValue(BlockStateProperties.NORTH, false);
                if (templateDz < 0) state = state.setValue(BlockStateProperties.SOUTH, false);
            }
            return state;
        };
    }
}
