package by.langvest.plantopia.worldgen.feature.foliageplacer;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;

@ParametersAreNonnullByDefault
public abstract class PlantopiaFoliagePlacer extends FoliagePlacer {
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
        protected boolean hasDoubleTrunk() {
            return attachment.doubleTrunk();
        }

        protected @NotNull BlockPos ceiling() {
            return attachment.pos().offset(0, offset, 0);
        }

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

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int dx, int dy, int dz, int range, boolean large) {
        return false;
    }
}
