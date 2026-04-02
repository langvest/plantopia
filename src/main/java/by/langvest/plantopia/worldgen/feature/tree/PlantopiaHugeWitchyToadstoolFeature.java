package by.langvest.plantopia.worldgen.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.jetbrains.annotations.NotNull;

public class PlantopiaHugeWitchyToadstoolFeature extends AbstractHugeMushroomFeature {
    protected final IntProvider level3Height = ConstantInt.of(2);
    protected final IntProvider level2Height = ConstantInt.of(2);
    protected final IntProvider level1Height = UniformInt.of(2, 3);

    public PlantopiaHugeWitchyToadstoolFeature(Codec<HugeMushroomFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    protected int getTreeHeight(@NotNull RandomSource random) {
        return random.nextInt(2) + 8;
    }

    @Override
    protected void placeTrunk(@NotNull LevelAccessor level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull HugeMushroomFeatureConfiguration config, int totalMushroomHeight, BlockPos.@NotNull MutableBlockPos mutablePos) {
        int trunkHeight = totalMushroomHeight - level3Height.getMinValue() - level2Height.getMinValue();

        for (int i = 0; i <= trunkHeight; i++) {
            mutablePos.setWithOffset(pos, 0, i, 0);
            placeBlock(level, mutablePos, random, config.stemProvider);
        }
    }

    @Override
    protected int getTreeRadiusForHeight(int _0, int _1, int maxFoliageRadius, int currentHeightFromBase) {
        return currentHeightFromBase > 2 ? maxFoliageRadius : 0;
    }

    @Override
    protected void makeCap(@NotNull LevelAccessor level, @NotNull RandomSource random, @NotNull BlockPos basePos, int totalMushroomHeight, BlockPos.@NotNull MutableBlockPos mutablePos, @NotNull HugeMushroomFeatureConfiguration config) {
        int foliageRadius = config.foliageRadius;

        int l3Height = level3Height.sample(random);
        int l2Height = level2Height.sample(random);
        int l1Height = level1Height.sample(random);

        // Level 3: Top spike (1x1).
        for (int i = 0; i < l3Height; i++) {
            int yOffset = totalMushroomHeight - i;
            mutablePos.setWithOffset(basePos, 0, yOffset, 0);
            placeBlock(level, mutablePos, random, config.capProvider);
        }

        // Level 2: Cross shape (+).
        int level2StartYOffset = totalMushroomHeight - l3Height;
        for (int i = 0; i < l2Height; i++) {
            int yOffset = level2StartYOffset - i;
            for (int x = -foliageRadius; x <= foliageRadius; x++) {
                for (int z = -foliageRadius; z <= foliageRadius; z++) {
                    if (Math.abs(x) + Math.abs(z) <= foliageRadius) {
                        mutablePos.setWithOffset(basePos, x, yOffset, z);
                        placeBlock(level, mutablePos, random, config.capProvider);
                    }
                }
            }
        }

        // Level 1: Square base (3x3).
        int level1StartYOffset = level2StartYOffset - l2Height;
        for (int i = 0; i < l1Height; i++) {
            int yOffset = level1StartYOffset - i;
            for (int x = -foliageRadius; x <= foliageRadius; x++) {
                for (int z = -foliageRadius; z <= foliageRadius; z++) {
                    // Leave the center empty for the trunk to pass through.
                    if (x == 0 && z == 0) continue;
                    mutablePos.setWithOffset(basePos, x, yOffset, z);
                    placeBlock(level, mutablePos, random, config.capProvider);
                }
            }
        }
    }

    private void placeBlock(@NotNull LevelAccessor level, BlockPos pos, RandomSource random, BlockStateProvider provider) {
        if (!level.getBlockState(pos).isSolidRender(level, pos)) {
            setBlock(level, pos, provider.getState(random, pos));
        }
    }
}
