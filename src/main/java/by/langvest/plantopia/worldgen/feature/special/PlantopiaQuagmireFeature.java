package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaAzollaBlock;
import by.langvest.plantopia.util.helper.PlantopiaMathHelper;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaQuagmireConfiguration;
import by.langvest.plantopia.worldgen.placement.PlantopiaThresholdType;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlantopiaQuagmireFeature extends Feature<PlantopiaQuagmireConfiguration> {
    public PlantopiaQuagmireFeature(Codec<PlantopiaQuagmireConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<PlantopiaQuagmireConfiguration> context) {
        var level = context.level();
        var originPos = context.origin();
        var random = context.random();
        var config = context.config();
        var predicate = config.predicate();
        var noiseConfig = config.noiseConfig();
        var activationType = config.activationType();
        var allowedBiomes = config.allowedBiomes();
        float noiseThreshold = config.noiseLevel();
        int blurRadius = config.blurRadius();

        double edgeWorstNoiseLevel = activationType == PlantopiaThresholdType.ABOVE ? -1.0D : 1.0D;
        int size = 16 + blurRadius * 2;

        double[][] noiseMask = new double[size][size];
        var mutablePos = new BlockPos.MutableBlockPos();
        int seaLevel = level.getSeaLevel();

        Holder<Biome> firstBiomeHolder = null;
        boolean multipleBiomes = false;

        for (int dx = 0; dx < size; dx++) {
            for (int dz = 0; dz < size; dz++) {
                int x = originPos.getX() - blurRadius + dx;
                int z = originPos.getZ() - blurRadius + dz;
                mutablePos.set(x, seaLevel, z);

                var biomeHolder = level.getBiome(mutablePos);
                noiseMask[dx][dz] = isBiomeAllowed(allowedBiomes, biomeHolder) ? noiseConfig.getValue(x, z) : edgeWorstNoiseLevel;

                if (firstBiomeHolder == null) {
                    firstBiomeHolder = biomeHolder;
                } else if (biomeHolder != firstBiomeHolder) {
                    multipleBiomes = true;
                }
            }
        }

        if (!multipleBiomes && firstBiomeHolder != null && !isBiomeAllowed(allowedBiomes, firstBiomeHolder)) {
            return false;
        }

        double[][] blurredMask = null;
        if (multipleBiomes) {
            blurredMask = PlantopiaMathHelper.boxBlurMatrix(noiseMask, blurRadius);
        }

        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                double pureNoiseLevel = noiseMask[dx + blurRadius][dz + blurRadius];

                if (pureNoiseLevel == edgeWorstNoiseLevel) {
                    continue;
                }

                int x = originPos.getX() + dx;
                int z = originPos.getZ() + dz;
                mutablePos.set(x, seaLevel, z);

                double finalNoiseLevel = multipleBiomes ? adjustBlurredNoise(blurredMask[dx + blurRadius][dz + blurRadius], pureNoiseLevel, activationType) : pureNoiseLevel;

                boolean shouldPlace;
                if (activationType.isAbove()) {
                    shouldPlace = finalNoiseLevel > noiseThreshold;
                } else {
                    shouldPlace = finalNoiseLevel < noiseThreshold;
                }

                if (shouldPlace && predicate.test(level, mutablePos)) {
                    double density;
                    if (activationType.isAbove()) {
                        density = (finalNoiseLevel - noiseThreshold) / (1.0D - noiseThreshold);
                    } else {
                        density = (noiseThreshold - finalNoiseLevel) / (noiseThreshold + 1.0D);
                    }

                    var state = getGradientState(density, config.erosion(), random);
                    if (state != null) {
                        PlantopiaNaturalBlockFeature.place(level, state, mutablePos, random, Block.UPDATE_CLIENTS);
                    }
                }
            }
        }

        return true;
    }

    protected @Nullable BlockState getGradientState(double density, float erosion, RandomSource random) {
        final float level1 = 0.866F;
        final float level2 = 0.836F;
        final float level3 = 0.58F;
        final float level4 = 0.34F;
        final float level5 = 0.12F;

        if (density >= level1) return getMossCarpetState();
        if (density >= level2 && random.nextInt(3) == 0) return getMossCarpetState();

        final float minErosionEffect = 0.05F;
        final float maxErosionEffect = 0.8F;
        float erosionFactor = (float) Mth.lerp(1.0F - density, minErosionEffect, maxErosionEffect);
        float erosionStrength = erosion * erosionFactor;
        float erosionDelta = random.nextFloat() * erosionStrength;
        double erodedDensity = density - erosionDelta;

        if (erodedDensity <= 0) {
            return null;
        }

        if (erodedDensity >= level3) return getAzollaState(4, random);
        if (erodedDensity >= level4) return getAzollaState(3, random);
        if (erodedDensity >= level5) return getAzollaState(2, random);

        double survivalChance = erodedDensity / level5;

        if (random.nextFloat() > survivalChance) {
            return null;
        }

        return getAzollaState(1, random);
    }

    private double adjustBlurredNoise(double blurredNoiseLevel, double pureNoiseLevel, PlantopiaThresholdType activationType) {
        return blurredNoiseLevel;
    }

    private boolean isBiomeAllowed(@NotNull HolderSet<Biome> allowedBiomes, @NotNull Holder<Biome> biomeHolder) {
        return allowedBiomes.contains(biomeHolder);
    }

    private @NotNull BlockState getAzollaState(int leafAmount, RandomSource random) {
        return PlantopiaBlocks.AZOLLA.get().defaultBlockState()
            .setValue(PlantopiaAzollaBlock.AMOUNT, leafAmount)
            .setValue(PlantopiaAzollaBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random));
    }

    private @NotNull BlockState getMossCarpetState() {
        return Blocks.MOSS_CARPET.defaultBlockState();
    }
}
