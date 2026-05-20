package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.util.helper.PlantopiaMathHelper;
import by.langvest.plantopia.worldgen.feature.PlantopiaIcicleUtils;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaIciclePatchConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlantopiaIciclePatchFeature extends Feature<PlantopiaIciclePatchConfiguration> {
    public PlantopiaIciclePatchFeature(Codec<PlantopiaIciclePatchConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<PlantopiaIciclePatchConfiguration> context) {
        var level = context.level();
        var random = context.random();
        var originPos = context.origin();
        var config = context.config();

        var placementInfo = findPlacement(level, originPos, config, random);
        if (placementInfo == null) {
            return false;
        }

        var centerPos = placementInfo.pos();
        var growthDirection = placementInfo.direction();
        var maxHeight = placementInfo.maxHeight();
        var xzSpread = placementInfo.xzSpread();
        var ySpread = placementInfo.ySpread();
        var heightmap = config.heightmap();
        if (maxHeight <= 0) {
            return false;
        }

        int tries = config.tries().sample(random);
        float shapeSigma = config.shapeSigma().sample(random);
        float shapeErosion = config.shapeErosion().sample(random);
        float heightFalloff = config.heightFalloff().sample(random);
        float heightErosion = config.heightErosion().sample(random);

        int successfulPlacements = 0;

        for (int i = 0; i < tries; i++) {
            var xzOffset = PlantopiaMathHelper.getHorizontalRadialOffset(random, xzSpread, shapeSigma, shapeErosion);
            double distanceToCenter = Math.sqrt(Mth.square(xzOffset.getX()) + Mth.square(xzOffset.getZ()));
            double falloffFactor = 1.0 - (distanceToCenter / xzSpread) * heightFalloff;
            double smoothHeight = maxHeight * Mth.clamp(falloffFactor, 0.0, 1.0);
            double erodedOffset = (random.nextDouble() * 2 - 1) * maxHeight * heightErosion;
            int height = (int) Math.round(Mth.clamp(smoothHeight + erodedOffset, 0.0, maxHeight));

            final BlockPos columnBasePos;
            if (heightmap.isPresent()) {
                int x = centerPos.getX() + xzOffset.getX();
                int z = centerPos.getZ() + xzOffset.getZ();
                int y = level.getHeight(heightmap.get(), x, z);

                if (Math.abs(y - centerPos.getY()) > ySpread) {
                    continue;
                }

                columnBasePos = new BlockPos(x, growthDirection == Direction.DOWN ? y - 1 : y, z);
            } else {
                var yOffset = random.nextInt(-ySpread, ySpread + 1);
                var localPos = new BlockPos.MutableBlockPos(xzOffset.getX(), yOffset, xzOffset.getZ());
                columnBasePos = rotate(localPos, growthDirection).offset(centerPos);
            }

            if (placeColumn(level, columnBasePos, growthDirection, height, config, random)) {
                successfulPlacements++;
            }
        }

        return successfulPlacements > 0;
    }

    private boolean placeColumn(@NotNull WorldGenLevel level, @NotNull BlockPos columnBasePos, @NotNull Direction growthDirection, int height, @NotNull PlantopiaIciclePatchConfiguration config, @NotNull RandomSource random) {
        if (height <= 0) {
            return false;
        }

        var allowedPlacement = config.allowedPlacement();
        var allowedAttachment = config.allowedAttachment();
        var baseState = PlantopiaIcicleUtils.getIcicleState(growthDirection, DripstoneThickness.BASE);

        if (!mayPlaceAt(level, columnBasePos, growthDirection, allowedAttachment)) {
            return false;
        }

        if (!baseState.canSurvive(level, columnBasePos)) {
            return false;
        }

        int finalHeight = 0;
        var mutablePos = columnBasePos.mutable();
        for (int i = 0; i < height; i++) {
            var currentPos = mutablePos.move(growthDirection, i);
            if (!allowedPlacement.test(level, currentPos) || !PlantopiaNaturalBlockColumnFeature.isFavorablePos(baseState, level, currentPos, growthDirection)) {
                break;
            }
            finalHeight++;
        }

        if (finalHeight <= 0) {
            return false;
        }

        return PlantopiaIcicleUtils.growIcicle(
                level,
                columnBasePos,
                growthDirection,
                finalHeight,
                Block.UPDATE_CLIENTS,
                false,
                random
        );
    }

    private BlockPos.MutableBlockPos rotate(@NotNull BlockPos.MutableBlockPos pos, @NotNull Direction direction) {
        return switch (direction) {
            case DOWN -> pos.set(pos.getX(), -pos.getY(), pos.getZ());
            case UP -> pos;
            case NORTH -> pos.set(pos.getX(), pos.getZ(), -pos.getY());
            case SOUTH -> pos.set(pos.getX(), pos.getZ(), pos.getY());
            case WEST -> pos.set(-pos.getY(), pos.getX(), pos.getZ());
            case EAST -> pos.set(pos.getY(), pos.getX(), pos.getZ());
        };
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean mayPlaceAt(@NotNull WorldGenLevel level, @NotNull BlockPos pos, @NotNull Direction growthDirection, BlockPredicate allowedAttachment) {
        var attachedPos = pos.relative(growthDirection.getOpposite());
        var attachedState = level.getBlockState(attachedPos);

        if (attachedState.is(PlantopiaIcicleUtils.getIcicleBlock())) {
            return false;
        }

        if (!attachedState.isFaceSturdy(level, attachedPos, growthDirection)) {
            return false;
        }

        return allowedAttachment.test(level, attachedPos);
    }

    @Nullable
    private PlacementInfo findPlacement(@NotNull WorldGenLevel level, @NotNull BlockPos originPos, @NotNull PlantopiaIciclePatchConfiguration config, @NotNull RandomSource random) {
        int searchDistance = config.searchDistance().sample(random);
        int maxHeight = config.height().sample(random);
        int xzSpread = config.xzSpread().sample(random);
        int ySpread = config.ySpread().sample(random);
        var allowedAttachment = config.allowedAttachment();
        var allowedPlacement = config.allowedPlacement();
        var growthDirections = config.growthDirections();

        for (var direction : growthDirections) {
            var candidateState = PlantopiaIcicleUtils.getIcicleState(direction, DripstoneThickness.BASE);

            for (int i = 0; i < searchDistance; i++) {
                var candidatePos = originPos.relative(direction.getOpposite(), i);

                if (!allowedPlacement.test(level, candidatePos)) continue;
                if (!mayPlaceAt(level, candidatePos, direction, allowedAttachment)) continue;
                if (!candidateState.canSurvive(level, candidatePos)) continue;

                return new PlacementInfo(candidatePos, direction, maxHeight, xzSpread, ySpread);
            }
        }

        return null;
    }

    private record PlacementInfo(BlockPos pos, Direction direction, int maxHeight, int xzSpread, int ySpread) {}
}
