package by.langvest.plantopia.worldgen.feature.foliageplacer;

import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class PlantopiaDynamicFoliagePlacer extends FoliagePlacer {
    public PlantopiaDynamicFoliagePlacer() {
        super(ConstantInt.of(0), ConstantInt.of(0));
    }

    @Override
    public void createFoliage(LevelSimulatedReader level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int treeHeight, int _ignoredFoliageRadius) {
        createDynamicFoliage(level, blockSetter, random, config, maxFreeTreeHeight, attachment, treeHeight);
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        // Intentionally left empty. Vanilla foliage parameters (foliageHeight, foliageRadius, offset)
        // are ignored. All foliage generation logic is handled by createDynamicFoliage() method.
    }

    protected abstract void createDynamicFoliage(LevelSimulatedReader level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int treeHeight);

    @Override
    public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) {
        return treeHeight;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        return false;
    }
}
