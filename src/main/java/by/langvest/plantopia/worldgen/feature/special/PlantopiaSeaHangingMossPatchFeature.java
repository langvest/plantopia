package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaHangingMossBlock;
import by.langvest.plantopia.util.helper.PlantopiaMathHelper;
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
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlantopiaSeaHangingMossPatchFeature extends Feature<PlantopiaSeaHangingMossPatchConfiguration> {
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

        int tries = config.tries().sample(random);
        float shapeSigma = config.shapeSigma().sample(random);
        float shapeErosion = config.shapeErosion().sample(random);
        float heightFalloff = config.heightFalloff().sample(random);
        float heightErosion = config.heightErosion().sample(random);

        int successfulPlacements = 0;
        var localPos = new BlockPos.MutableBlockPos();

        for (int i = 0; i < tries; i++) {
            var xzOffset = PlantopiaMathHelper.getHorizontalRadialOffset(random, xzSpread, shapeSigma, shapeErosion);
            var yOffset = random.nextInt(-ySpread, ySpread + 1);
            localPos.set(xzOffset.getX(), yOffset, xzOffset.getZ());
            double distanceToCenter = Math.sqrt(localPos.distSqr(BlockPos.ZERO));
            double falloffFactor = 1.0 - (distanceToCenter / xzSpread) * heightFalloff;
            double smoothHeight = maxHeight * Mth.clamp(falloffFactor, 0.0, 1.0);
            double erodedOffset = (random.nextDouble() * 2 - 1) * maxHeight * heightErosion;
            int height = (int) Math.round(Mth.clamp(smoothHeight + erodedOffset, 0.0, maxHeight));

            if (placeColumn(level, centerPos, localPos, height, config, random)) {
                successfulPlacements++;
            }
        }

        return successfulPlacements > 0;
    }

    private boolean placeColumn(@NotNull WorldGenLevel level, @NotNull BlockPos centerPos, BlockPos.MutableBlockPos localPos, int height, @NotNull PlantopiaSeaHangingMossPatchConfiguration config, @NotNull RandomSource random) {
        if (height <= 0) {
            return false;
        }

        var allowedPlacement = config.allowedPlacement();
        var allowedAttachment = config.allowedAttachment();
        var columnBasePos = localPos.offset(centerPos);
        var bodyState = getBodyState();
        var tipState = getTipState();

        if (!allowedPlacement.test(level, columnBasePos)) {
            return false;
        }

        if (!mayPlaceAt(level, columnBasePos, allowedAttachment)) {
            return false;
        }

        if (!tipState.canSurvive(level, columnBasePos)) {
            return false;
        }

        return PlantopiaNaturalBlockColumnFeature.place(
            level,
            columnBasePos,
            random,
            Direction.DOWN,
            allowedPlacement,
            true,
            Block.UPDATE_CLIENTS,
            List.of(
                BlockColumnConfiguration.layer(
                    ConstantInt.of(height - 1),
                    BlockStateProvider.simple(bodyState)
                ),
                BlockColumnConfiguration.layer(
                    ConstantInt.of(1),
                    BlockStateProvider.simple(tipState)
                )
            )
        );
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
        var candidateState = getTipState();

        for (int i = 0; i < searchDistance; i++) {
            var candidatePos = originPos.relative(Direction.UP, i);

            if (!allowedPlacement.test(level, candidatePos)) continue;
            if (!mayPlaceAt(level, candidatePos, allowedAttachment)) continue;
            if (!candidateState.canSurvive(level, candidatePos)) continue;

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

    private record PlacementInfo(BlockPos pos, int maxHeight, int xzSpread, int ySpread) {}
}
