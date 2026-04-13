package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.worldgen.feature.PlantopiaIcicleUtil;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaIcicleConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PlantopiaIcicleFeature extends Feature<PlantopiaIcicleConfiguration> {
    public PlantopiaIcicleFeature(Codec<PlantopiaIcicleConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<PlantopiaIcicleConfiguration> context) {
        var level = context.level();
        var origin = context.origin();
        var random = context.random();
        var config = context.config();

        var tipDirection = getTipDirection(level, origin, random);
        if (tipDirection.isEmpty()) {
            return false;
        }

        var direction = tipDirection.get();
        var basePos = origin.relative(direction.getOpposite());

        createPatchOfIceBlocks(level, random, basePos, config);

        int height = random.nextFloat() < config.chanceOfTallerIcicle() && PlantopiaIcicleUtil.isEmptyOrWater(level.getBlockState(origin.relative(direction))) ? 2 : 1;
        PlantopiaIcicleUtil.growIcicleOnIceIfPossible(level, origin, direction, height, false, random);
        return true;
    }

    private static Optional<Direction> getTipDirection(@NotNull LevelAccessor level, @NotNull BlockPos pos, RandomSource random) {
        boolean canPlaceAbove = PlantopiaIcicleUtil.isValidGround(level.getBlockState(pos.above()));
        boolean canPlaceBelow = PlantopiaIcicleUtil.isValidGround(level.getBlockState(pos.below()));

        if (canPlaceAbove && canPlaceBelow) {
            return Optional.of(random.nextBoolean() ? Direction.DOWN : Direction.UP);
        }
        if (canPlaceAbove) {
            return Optional.of(Direction.DOWN);
        }
        if (canPlaceBelow) {
            return Optional.of(Direction.UP);
        }
        return Optional.empty();
    }

    private static void createPatchOfIceBlocks(LevelAccessor level, RandomSource random, BlockPos pos, PlantopiaIcicleConfiguration config) {
        PlantopiaIcicleUtil.placeIceBlockIfPossible(level, pos);

        for (var direction : Direction.Plane.HORIZONTAL) {
            if (random.nextFloat() > config.chanceOfIcicleSpread()) {
                continue;
            }

            var pos1 = pos.relative(direction);
            PlantopiaIcicleUtil.placeIceBlockIfPossible(level, pos1);

            if (random.nextFloat() > config.chanceOfSpreadRadius2()) {
                continue;
            }

            var pos2 = pos1.relative(Direction.getRandom(random));
            PlantopiaIcicleUtil.placeIceBlockIfPossible(level, pos2);

            if (random.nextFloat() > config.chanceOfSpreadRadius3()) {
                continue;
            }

            var pos3 = pos2.relative(Direction.getRandom(random));
            PlantopiaIcicleUtil.placeIceBlockIfPossible(level, pos3);
        }
    }
}
