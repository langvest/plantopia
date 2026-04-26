package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaHangingMossBlock;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaSeaHangingMossPatchConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlantopiaSeaHangingMossPatchFeature extends Feature<PlantopiaSeaHangingMossPatchConfiguration> {
    private static final int MIN_DEPTH_FOR_EFFECT = 0;
    private static final int MAX_DEPTH_FOR_EFFECT = 20;
    private static final int SURFACE_MAX_HEIGHT = 2;
    private static final float SURFACE_HEIGHT_FALLOFF = 1.064F;

    public PlantopiaSeaHangingMossPatchFeature(Codec<PlantopiaSeaHangingMossPatchConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<PlantopiaSeaHangingMossPatchConfiguration> context) {
        var level = context.level();
        var random = context.random();
        var originPos = context.origin();
        var config = context.config();

        var placementInfo = findPlacement(level, originPos, config, random);
        if (placementInfo == null) {
            return false;
        }

        var centerPos = placementInfo.pos();
        var maxHeight = placementInfo.maxHeight();
        var xzSpread = placementInfo.xzSpread();
        var ySpread = placementInfo.ySpread();
        if (maxHeight <= 0) {
            return false;
        }

        float surfaceProximityFactor = getSurfaceProximityFactor(level, centerPos);
        maxHeight = (int) Mth.lerp(surfaceProximityFactor, maxHeight, SURFACE_MAX_HEIGHT);
        float heightFalloff = Mth.lerp(surfaceProximityFactor, config.heightFalloff().sample(random), SURFACE_HEIGHT_FALLOFF);

        float edgeErosion = config.edgeErosion().sample(random);
        float heightErosion = config.heightErosion().sample(random);

        int successfulPlacements = 0;
        var localPos = new BlockPos.MutableBlockPos();
        boolean centerInWater = level.isWaterAt(centerPos);

        for (int x = -xzSpread; x <= xzSpread; x++) {
            for (int z = -xzSpread; z <= xzSpread; z++) {
                double distanceToCenter = Math.sqrt(Mth.square(x) + Mth.square(z));
                if (distanceToCenter > xzSpread) {
                    continue;
                }

                double remoteness = distanceToCenter / xzSpread;
                if (random.nextFloat() < remoteness * edgeErosion) {
                    continue;
                }

                localPos.set(x, 0, z);
                var placementPos = findCeiling(level, centerPos.offset(localPos), ySpread, config.allowedPlacement(), config.allowedAttachment());
                if (placementPos == null) {
                    continue;
                }

                if (level.isWaterAt(placementPos) != centerInWater) {
                    continue;
                }

                double falloffFactor = 1.0 - (distanceToCenter / xzSpread) * heightFalloff;
                double smoothHeight = maxHeight * Mth.clamp(falloffFactor, 0.0, 1.0);
                double erodedOffset = (random.nextDouble() * 2 - 1) * maxHeight * heightErosion;
                int height = (int) Math.round(Mth.clamp(smoothHeight + erodedOffset, 0.0, maxHeight));

                if (height > 0) {
                    if (placeColumn(level, placementPos, height, config.allowedPlacement(), random)) {
                        successfulPlacements++;
                    }
                }
            }
        }

        return successfulPlacements > 0;
    }

    private float getSurfaceProximityFactor(WorldGenLevel level, BlockPos pos) {
        int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX(), pos.getZ());
        int depth = surfaceY - pos.getY();
        return (float) Mth.clamp(Mth.inverseLerp(depth, MAX_DEPTH_FOR_EFFECT, MIN_DEPTH_FOR_EFFECT), 0.0, 1.0);
    }

    private boolean placeColumn(@NotNull WorldGenLevel level, @NotNull BlockPos pos, int height, BlockPredicate allowedPlacement, @NotNull RandomSource random) {
        return PlantopiaNaturalBlockColumnFeature.place(
            level,
            pos,
            random,
            Direction.DOWN,
            allowedPlacement,
            true,
            Block.UPDATE_CLIENTS,
            List.of(
                BlockColumnConfiguration.layer(
                    ConstantInt.of(height - 1),
                    BlockStateProvider.simple(getBodyState())
                ),
                BlockColumnConfiguration.layer(
                    ConstantInt.of(1),
                    BlockStateProvider.simple(getTipState())
                )
            )
        );
    }

    @Nullable
    private BlockPos findCeiling(@NotNull WorldGenLevel level, @NotNull BlockPos pos, int ySpread, BlockPredicate allowedPlacement, BlockPredicate allowedAttachment) {
        var mutablePos = new BlockPos.MutableBlockPos().set(pos);
        for (int y = -ySpread; y <= ySpread; y++) {
            mutablePos.setY(pos.getY() + y);

            if (allowedPlacement.test(level, mutablePos) &&
                allowedPlacement.test(level, mutablePos.below()) &&
                mayPlaceAt(level, mutablePos, allowedAttachment) &&
                getTipState().canSurvive(level, mutablePos)) {
                return mutablePos.immutable();
            }
        }
        return null;
    }


    private int getAdjustedMaxHeight(@NotNull WorldGenLevel level, @NotNull BlockPos pos, int maxHeight, BlockPredicate allowedPlacement, RandomSource random) {
        var physicalLimit = getPhysicalHeightLimit(level, pos, maxHeight, allowedPlacement);

        if (physicalLimit < 2) {
            return 0;
        }

        int naturalMaxHeight = Math.min(physicalLimit, maxHeight);
        var diff = maxHeight - naturalMaxHeight;

        if (diff > 0 && random.nextInt(3) == 0) {
            var bonusHeight = Math.min(random.nextInt(0, 5), diff);
            naturalMaxHeight += bonusHeight;
        }

        return naturalMaxHeight;
    }

    @Nullable
    private PlacementInfo findPlacement(@NotNull WorldGenLevel level, @NotNull BlockPos originPos, @NotNull PlantopiaSeaHangingMossPatchConfiguration config, @NotNull RandomSource random) {
        int searchDistance = config.searchDistance().sample(random);
        int maxHeight = config.height().sample(random);
        int xzSpread = config.xzSpread().sample(random);
        int ySpread = config.ySpread().sample(random);
        var allowedPlacement = config.allowedPlacement();
        var allowedAttachment = config.allowedAttachment();

        for (int i = 0; i < searchDistance; i++) {
            var candidatePos = originPos.relative(Direction.UP, i);

            if (!allowedPlacement.test(level, candidatePos)) continue;
            if (!mayPlaceAt(level, candidatePos, allowedAttachment)) continue;
            if (!getTipState().canSurvive(level, candidatePos)) continue;

            var adjustedMaxHeight = getAdjustedMaxHeight(level, candidatePos, maxHeight, allowedPlacement, random);

            return new PlacementInfo(candidatePos, adjustedMaxHeight, xzSpread, ySpread);
        }

        return null;
    }

    private int getPhysicalHeightLimit(@NotNull WorldGenLevel level, @NotNull BlockPos pos, int maxHeight, BlockPredicate allowedPlacement) {
        int limit = 0;
        var mutablePos = pos.mutable();

        for (int i = 0; i < maxHeight; i++) {
            mutablePos.move(Direction.DOWN);

            if (allowedPlacement.test(level, mutablePos)) {
                limit++;
            } else {
                break;
            }
        }

        return limit;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean mayPlaceAt(@NotNull WorldGenLevel level, @NotNull BlockPos pos, BlockPredicate allowedAttachment) {
        var attachedPos = pos.above();
        var attachedState = level.getBlockState(attachedPos);

        if (attachedState.is(getPlantBlock())) {
            return false;
        }

        if (!attachedState.isFaceSturdy(level, attachedPos, Direction.DOWN)) {
            return false;
        }

        return allowedAttachment.test(level, attachedPos);
    }

    private Block getPlantBlock() {
        return PlantopiaBlocks.SEA_HANGING_MOSS.get();
    }

    private @NotNull BlockState getBodyState() {
        return getPlantBlock().defaultBlockState().setValue(PlantopiaHangingMossBlock.TIP, false);
    }

    private @NotNull BlockState getTipState() {
        return getPlantBlock().defaultBlockState().setValue(PlantopiaHangingMossBlock.TIP, true);
    }

    private record PlacementInfo(BlockPos pos, int maxHeight, int xzSpread, int ySpread) {
    }
}
