package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaHangingMossBlock;
import by.langvest.plantopia.util.helper.PlantopiaMathHelper;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaSeaHangingMossClusterConfiguration;
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

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class PlantopiaSeaHangingMossClusterFeature extends Feature<PlantopiaSeaHangingMossClusterConfiguration> {
    public PlantopiaSeaHangingMossClusterFeature(Codec<PlantopiaSeaHangingMossClusterConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PlantopiaSeaHangingMossClusterConfiguration> context) {
        var level = context.level();
        var origin = context.origin();
        var config = context.config();
        var random = context.random();

        var placementInfo = findPlacement(level, origin, config, random);
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

        float heightFalloff = config.heightFalloff().sample(random);
        float heightErosion = config.heightErosion().sample(random);
        float edgeErosion = config.edgeErosion().sample(random);
        float curvature = config.curvature().sample(random);
        double frequency = 0.15 + curvature * 0.1;
        var mutablePos = new BlockPos.MutableBlockPos();
        boolean successfullyPlaced = false;

        for (int dx = -xzSpread; dx <= xzSpread; dx++) {
            for (int dz = -xzSpread; dz <= xzSpread; dz++) {
                mutablePos.set(centerPos.getX() + dx, centerPos.getY(), centerPos.getZ() + dz);

                if (!PlantopiaMathHelper.isWithinShape(mutablePos, dx, dz, xzSpread, curvature, frequency)) {
                    continue;
                }

                var ceilingPos = findCeiling(level, mutablePos, ySpread, config);
                if (ceilingPos == null) {
                    continue;
                }

                int depth = config.depth().sample(random);
                if (!placeBlob(level, ceilingPos, depth, config.allowedBasisPlacement(), random)) {
                    continue;
                }

                successfullyPlaced = true;

                double distanceToCenter = Math.sqrt(Mth.square(dx) + Mth.square(dz));
                if (distanceToCenter > xzSpread) {
                    continue;
                }

                double remoteness = distanceToCenter / xzSpread;
                if (random.nextFloat() < remoteness * edgeErosion) {
                    continue;
                }

                double falloffFactor = 1.0 - (distanceToCenter / xzSpread) * heightFalloff;
                double smoothHeight = maxHeight * Mth.clamp(falloffFactor, 0.0, 1.0);
                double erodedOffset = (random.nextDouble() * 2 - 1) * maxHeight * heightErosion;
                int height = (int) Math.round(Mth.clamp(smoothHeight + erodedOffset, 0.0, maxHeight));

                if (height > 0) {
                    placeColumn(level, ceilingPos.below(), height, config.allowedPlantPlacement(), random);
                }
            }
        }

        return successfullyPlaced;
    }

    private int getPhysicalHeightLimit(WorldGenLevel level, BlockPos pos, int maxHeight, BlockPredicate allowedPlacement) {
        int limit = 0;
        var mutablePos = pos.mutable();

        for (int dy = 0; dy < maxHeight; dy++) {
            mutablePos.move(Direction.DOWN);

            if (allowedPlacement.test(level, mutablePos)) {
                limit++;
            } else {
                break;
            }
        }

        return limit;
    }

    private int getAdjustedMaxHeight(WorldGenLevel level, BlockPos pos, int maxHeight, BlockPredicate allowedPlacement, RandomSource random) {
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
    private BlockPos findCeiling(WorldGenLevel level, BlockPos pos, int ySpread, PlantopiaSeaHangingMossClusterConfiguration config) {
        var allowedPlacement = config.allowedBasisPlacement();
        var mutablePos = pos.mutable();

        for (int dy = -ySpread; dy <= ySpread; dy++) {
            mutablePos.setY(pos.getY() + dy);

            if (level.isOutsideBuildHeight(mutablePos)) {
                break;
            }

            if (allowedPlacement.test(level, mutablePos)) {
                return mutablePos.immutable();
            }
        }

        return null;
    }

    private boolean placeBlob(WorldGenLevel level, BlockPos pos, int depth, BlockPredicate allowedPlacement, RandomSource random) {
        var mutablePos = pos.mutable();
        boolean successfullyPlaced = false;

        for (int i = 0; i < depth; i++) {
            mutablePos.move(Direction.UP, i);

            if (level.isOutsideBuildHeight(mutablePos) || !allowedPlacement.test(level, mutablePos)) {
                break;
            }

            if (level.setBlock(mutablePos, getMossState(), Block.UPDATE_CLIENTS)) {
                successfullyPlaced = true;
            }
        }

        return successfullyPlaced;
    }

    @SuppressWarnings("UnusedReturnValue")
    private boolean placeColumn(WorldGenLevel level, BlockPos pos, int height, BlockPredicate allowedPlacement, RandomSource random) {
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
                    ConstantInt.of(Math.max(0, height - 1)),
                    BlockStateProvider.simple(getBodyState())
                ),
                BlockColumnConfiguration.layer(
                    ConstantInt.of(1),
                    BlockStateProvider.simple(getTipState())
                )
            )
        );
    }

    private @NotNull BlockState getMossState() {
        return PlantopiaBlocks.SEA_MOSS_BLOCK.get().defaultBlockState();
    }

    private @NotNull BlockState getBodyState() {
        return PlantopiaBlocks.SEA_HANGING_MOSS.get().defaultBlockState().setValue(PlantopiaHangingMossBlock.TIP, false);
    }

    private @NotNull BlockState getTipState() {
        return PlantopiaBlocks.SEA_HANGING_MOSS.get().defaultBlockState().setValue(PlantopiaHangingMossBlock.TIP, true);
    }

    @Nullable
    private PlacementInfo findPlacement(WorldGenLevel level, BlockPos origin, PlantopiaSeaHangingMossClusterConfiguration config, RandomSource random) {
        int searchDistance = config.searchDistance().sample(random);
        int maxHeight = config.height().sample(random);
        int xzSpread = config.xzSpread().sample(random);
        int ySpread = config.ySpread().sample(random);
        var mutablePos = origin.mutable();

        for (int dy = 0; dy < searchDistance; dy++) {
            mutablePos.move(Direction.UP);

            if (!config.allowedBasisPlacement().test(level, mutablePos)) continue;

            var centerPos = mutablePos.immutable();
            var adjustedMaxHeight = getAdjustedMaxHeight(level, centerPos, maxHeight, config.allowedPlantPlacement(), random);
            return new PlacementInfo(centerPos, adjustedMaxHeight, xzSpread, ySpread);
        }

        return null;
    }

    private record PlacementInfo(BlockPos pos, int maxHeight, int xzSpread, int ySpread) {}
}
