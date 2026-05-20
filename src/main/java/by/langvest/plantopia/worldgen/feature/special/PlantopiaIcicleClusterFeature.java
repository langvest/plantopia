package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.worldgen.feature.PlantopiaIcicleUtils;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaIcicleClusterConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ClampedNormalFloat;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.OptionalInt;

public class PlantopiaIcicleClusterFeature extends Feature<PlantopiaIcicleClusterConfiguration> {
    public PlantopiaIcicleClusterFeature(Codec<PlantopiaIcicleClusterConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<PlantopiaIcicleClusterConfiguration> context) {
        var level = context.level();
        var origin = context.origin();
        var config = context.config();
        var random = context.random();

        if (!PlantopiaIcicleUtils.isEmptyOrWater(level, origin)) {
            return false;
        }

        int height = config.height().sample(random);
        float density = config.density().sample(random);
        int radiusX = config.radius().sample(random);
        int radiusZ = config.radius().sample(random);

        for (int x = -radiusX; x <= radiusX; ++x) {
            for (int z = -radiusZ; z <= radiusZ; ++z) {
                double chance = this.getChanceOfStalagmiteOrStalactite(radiusX, radiusZ, x, z, config);
                var pos = origin.offset(x, 0, z);
                this.placeColumn(level, random, pos, x, z, chance, height, density, config);
            }
        }

        return true;
    }


    private void placeColumn(WorldGenLevel level, RandomSource random, BlockPos pos, int x, int z, double chance, int height, float density, @NotNull PlantopiaIcicleClusterConfiguration config) {
        Optional<Column> optional = Column.scan(level, pos, config.floorToCeilingSearchRange(), PlantopiaIcicleUtils::isEmptyOrWater, PlantopiaIcicleUtils::isNeitherEmptyNorWater);
        if (optional.isEmpty()) {
            return;
        }

        OptionalInt ceiling = optional.get().getCeiling();
        OptionalInt floor = optional.get().getFloor();
        if (ceiling.isEmpty() && floor.isEmpty()) {
            return;
        }

        boolean placeStalactite = random.nextDouble() < chance;
        int stalactiteHeight;
        if (ceiling.isPresent() && placeStalactite) {
            int layerThickness = config.iceBlockLayerThickness().sample(random);
            this.replaceBlocksWithIce(level, pos.atY(ceiling.getAsInt()), layerThickness, Direction.UP);
            int maxHeight = floor.isPresent() ? Math.min(height, ceiling.getAsInt() - floor.getAsInt()) : height;
            stalactiteHeight = this.getIcicleHeight(random, x, z, density, maxHeight, config);
        } else {
            stalactiteHeight = 0;
        }

        boolean placeStalagmite = random.nextDouble() < chance;
        int stalagmiteHeight;
        if (floor.isPresent() && placeStalagmite) {
            int layerThickness = config.iceBlockLayerThickness().sample(random);
            this.replaceBlocksWithIce(level, pos.atY(floor.getAsInt()), layerThickness, Direction.DOWN);
            if (ceiling.isPresent()) {
                stalagmiteHeight = Math.max(0, stalactiteHeight + Mth.randomBetweenInclusive(random, -config.maxStalagmiteStalactiteHeightDiff(), config.maxStalagmiteStalactiteHeightDiff()));
            } else {
                stalagmiteHeight = this.getIcicleHeight(random, x, z, density, height, config);
            }
        } else {
            stalagmiteHeight = 0;
        }

        int finalStalactiteHeight;
        int finalStalagmiteHeight;
        if (ceiling.isPresent() && floor.isPresent() && ceiling.getAsInt() - stalactiteHeight <= floor.getAsInt() + stalagmiteHeight) {
            int floorY = floor.getAsInt();
            int ceilingY = ceiling.getAsInt();
            int i = Math.max(ceilingY - stalactiteHeight, floorY + 1);
            int j = Math.min(floorY + stalagmiteHeight, ceilingY - 1);
            int k = Mth.randomBetweenInclusive(random, i, j + 1);
            int l = k - 1;
            finalStalactiteHeight = ceilingY - k;
            finalStalagmiteHeight = l - floorY;
        } else {
            finalStalactiteHeight = stalactiteHeight;
            finalStalagmiteHeight = stalagmiteHeight;
        }

        boolean merge = random.nextBoolean() && finalStalactiteHeight > 0 && finalStalagmiteHeight > 0 && optional.get().getHeight().isPresent() && finalStalactiteHeight + finalStalagmiteHeight == optional.get().getHeight().getAsInt();
        if (ceiling.isPresent()) {
            PlantopiaIcicleUtils.growIcicleOnIceIfPossible(level, pos.atY(ceiling.getAsInt() - 1), Direction.DOWN, finalStalactiteHeight, merge, random);
        }

        if (floor.isPresent()) {
            PlantopiaIcicleUtils.growIcicleOnIceIfPossible(level, pos.atY(floor.getAsInt() + 1), Direction.UP, finalStalagmiteHeight, merge, random);
        }
    }

    private int getIcicleHeight(@NotNull RandomSource random, int x, int z, float chance, int height, PlantopiaIcicleClusterConfiguration config) {
        if (random.nextFloat() > chance) {
            return 0;
        }

        int i = Math.abs(x) + Math.abs(z);
        float f = (float) Mth.clampedMap(i, 0.0D, config.maxDistanceFromCenterAffectingHeightBias(), (double) height / 2.0D, 0.0D);
        return (int) randomBetweenBiased(random, 0.0F, height, f, config.heightDeviation());
    }

    private void replaceBlocksWithIce(WorldGenLevel level, @NotNull BlockPos pos, int thickness, Direction direction) {
        var mutablePos = pos.mutable();
        for (int i = 0; i < thickness; ++i) {
            if (!PlantopiaIcicleUtils.placeIceBlockIfPossible(level, mutablePos)) {
                return;
            }
            mutablePos.move(direction);
        }
    }

    private double getChanceOfStalagmiteOrStalactite(int xRadius, int zRadius, int x, int z, @NotNull PlantopiaIcicleClusterConfiguration config) {
        int i = xRadius - Math.abs(x);
        int j = zRadius - Math.abs(z);
        int k = Math.min(i, j);
        return Mth.clampedMap(k, 0.0F, config.maxDistanceFromEdgeAffectingChanceOfIcicleColumn(), config.chanceOfIcicleColumnAtMaxDistanceFromCenter(), 1.0F);
    }

    private static float randomBetweenBiased(RandomSource random, float min, float max, float mean, float deviation) {
        return ClampedNormalFloat.sample(random, mean, deviation, min, max);
    }
}
