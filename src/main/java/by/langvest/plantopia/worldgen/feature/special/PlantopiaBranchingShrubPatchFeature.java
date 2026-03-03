package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaBranchingShrubBlock;
import by.langvest.plantopia.util.helper.PlantopiaMathHelper;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaBranchingShrubPatchConfiguration;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.List;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;

public class PlantopiaBranchingShrubPatchFeature extends Feature<PlantopiaBranchingShrubPatchConfiguration> {
    public PlantopiaBranchingShrubPatchFeature(Codec<PlantopiaBranchingShrubPatchConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<PlantopiaBranchingShrubPatchConfiguration> context) {
        var level = context.level();
        var random = context.random();
        var origin = context.origin();
        var config = context.config();
        int horizontalSpread = config.xzSpread().sample(random);
        int verticalSpread = config.ySpread().sample(random);
        int maxHeight = config.height().sample(random);
        var surfaceInfo = findSurface(level, origin, config, random, horizontalSpread, maxHeight);

        if (surfaceInfo == null) {
            return false;
        }

        var centerPos = surfaceInfo.getFirst();
        var growthDirection = surfaceInfo.getSecond();
        int tries = config.tries().sample(random);
        float shapeSigma = config.shapeSigma().sample(random);
        float shapeErosion = config.shapeErosion().sample(random);
        float heightFalloff = config.heightFalloff().sample(random);
        float heightErosion = config.heightErosion().sample(random);

        int successfulPlacements = 0;

        for (int i = 0; i < tries; i++) {
            var radialOffset = PlantopiaMathHelper.getHorizontalRadialOffset(random, horizontalSpread, shapeSigma, shapeErosion);
            var verticalOffset = random.nextInt(-verticalSpread, verticalSpread + 1);
            var localPos = new BlockPos(radialOffset.getX(), verticalOffset, radialOffset.getZ());
            double distanceToCenter = Math.sqrt(localPos.getX() * localPos.getX() + localPos.getZ() * localPos.getZ());
            double heightFactor = 1.0 - (distanceToCenter / horizontalSpread) * heightFalloff;
            double idealHeightDouble = maxHeight * Mth.clamp(heightFactor, 0.0, 1.0);
            double erosionAmountDouble = (random.nextDouble() * 2 - 1) * maxHeight * heightErosion;
            int finalHeight = (int) Math.round(Mth.clamp(idealHeightDouble + erosionAmountDouble, 1.0, maxHeight));

            if (placeColumn(level, centerPos, localPos, growthDirection, finalHeight)) {
                successfulPlacements++;
            }
        }

        return successfulPlacements > 0;
    }

    private boolean placeColumn(@NotNull WorldGenLevel level, @NotNull BlockPos centerPos, @NotNull BlockPos columnBasePos, @NotNull Direction growthDirection, int height) {
        var currentPos = rotate(columnBasePos, growthDirection).offset(centerPos).mutable();

        boolean successfulPlaced = false;

        for (int i = 0; i < height; i++) {
            currentPos = currentPos.move(growthDirection);

            if (!canReplace(level, currentPos)) break;

            var segmentState = getBranchingShrubBlock().defaultBlockState()
                .setValue(PlantopiaBranchingShrubBlock.BASE, i == 0)
                .setValue(PlantopiaBranchingShrubBlock.FACING, growthDirection);

            if (i == 0) {
                if (!segmentState.canSurvive(level, currentPos)) break;
                if (!mayPlace(level, currentPos, segmentState)) break;
            }

            level.setBlock(currentPos, copyWaterloggedFrom(level, currentPos, segmentState), Block.UPDATE_CLIENTS);
            successfulPlaced = true;
        }

        return successfulPlaced;
    }

    private BlockPos rotate(@NotNull BlockPos pos, @NotNull Direction direction) {
        return switch (direction) {
            case DOWN -> new BlockPos(pos.getX(), -pos.getY(), pos.getZ());
            case UP -> pos;
            case NORTH -> new BlockPos(pos.getX(), pos.getZ(), -pos.getY());
            case SOUTH -> new BlockPos(pos.getX(), pos.getZ(), pos.getY());
            case WEST -> new BlockPos(-pos.getY(), pos.getX(), pos.getZ());
            case EAST -> new BlockPos(pos.getY(), pos.getX(), pos.getZ());
        };
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean canReplace(@NotNull WorldGenLevel level, BlockPos pos) {
        var state = level.getBlockState(pos);

        return canReplace(state);
    }

    private boolean canReplace(@NotNull BlockState state) {
        return state.isAir()
            || state.is(Blocks.WATER)
            || state.is(Blocks.SNOW)
            || state.is(Blocks.GRASS)
            || state.is(Blocks.FERN)
            || state.is(Blocks.SEAGRASS)
            || state.is(Blocks.GLOW_LICHEN);
    }

    private boolean canBreathThrough(@NotNull WorldGenLevel level, BlockPos pos) {
        var state = level.getBlockState(pos);

        return canBreathThrough(state);
    }

    private boolean isLandscape(@NotNull BlockState state) {
        return state.is(BlockTags.DIRT) || state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(BlockTags.SAND) || state.is(Blocks.SANDSTONE);
    }

    private boolean canBreathThrough(@NotNull BlockState state) {
        return !isLandscape(state);
    }

    private Block getBranchingShrubBlock() {
        return PlantopiaBlocks.BRANCHING_SHRUB.get();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean mayPlace(@NotNull WorldGenLevel level, @NotNull BlockPos pos, @NotNull BlockState state) {
        var facing = state.getValue(PlantopiaBranchingShrubBlock.FACING);
        var surfacePos = pos.relative(facing.getOpposite());
        var surfaceState = level.getBlockState(surfacePos);

        if (surfaceState.is(Blocks.SAND) || surfaceState.is(Blocks.MOSS_BLOCK)) {
            return false;
        }

        if (facing == Direction.UP && surfaceState.is(Blocks.GRASS_BLOCK)) {
            int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX(), pos.getZ());
            if (pos.getY() < surfaceY) return false;
        }

        if (facing.getAxis().isHorizontal()) {
            var posBelow = pos.below();
            var stateBelow = level.getBlockState(posBelow);
            if (stateBelow.is(Blocks.GRASS_BLOCK)) return false;
        }

        return true;
    }

    private float getAirRatio(int[] passableCounts, int maxHeight) {
        int totalPassable = Arrays.stream(passableCounts).sum();
        return (float) totalPassable / (passableCounts.length * maxHeight);
    }

    private float getAirThreshold(@NotNull WorldGenLevel level, @NotNull BlockPos pos) {
        int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX(), pos.getZ());
        int depth = surfaceY - pos.getY();
        double progress = Mth.inverseLerp(depth, 0, 20);
        return (float) Mth.lerp(progress, 0.5F, 0.1F);
    }

    private float getUniformRatio(int[] passableCounts) {
        var maxOptional = Arrays.stream(passableCounts).max();

        if (maxOptional.isEmpty()) {
            return 0.0F;
        }

        int bestCount = maxOptional.getAsInt();
        int worstCount = Arrays.stream(passableCounts).min().getAsInt();

        if (bestCount == 0) {
            return 1.0F;
        }

        return (float) worstCount / bestCount;
    }

    private float getUniformThreshold(@NotNull WorldGenLevel level, @NotNull BlockPos pos, @NotNull Direction direction) {
        if (direction.getAxis().isVertical()) {
            return 0.1F;
        }

        int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX(), pos.getZ());
        int depth = surfaceY - pos.getY();
        double progress = Mth.inverseLerp(depth, 0, 15);
        return (float) Mth.lerp(progress, 0.6F, 0.3F);
    }

    private boolean isAreaFavorable(@NotNull WorldGenLevel level, @NotNull BlockPos pos, @NotNull Direction direction, int radius, int maxHeight) {
        int[] passableCounts = collectColumnData(level, pos, direction, radius, maxHeight);

        float airRatio = getAirRatio(passableCounts, maxHeight);
        float airThreshold = getAirThreshold(level, pos);

        if (airRatio < airThreshold) {
            return false;
        }

        float uniformRatio = getUniformRatio(passableCounts);
        float uniformThreshold = getUniformThreshold(level, pos, direction);

        return uniformRatio >= uniformThreshold;
    }

    private int @NotNull [] collectColumnData(@NotNull WorldGenLevel level, @NotNull BlockPos pos, @NotNull Direction growthDirection, int radius, int maxHeight) {
        var testPoints = getTestPoints(radius);
        int[] passableCounts = new int[testPoints.size()];

        for (int idx = 0; idx < testPoints.size(); idx++) {
            BlockPos point = testPoints.get(idx);
            var testPos = rotate(point, growthDirection).offset(pos).mutable();
            int count = 0;
            for (int i = 0; i < maxHeight; i++) {
                if (canBreathThrough(level, testPos)) {
                    count++;
                }
                testPos.move(growthDirection);
            }
            passableCounts[idx] = count;
        }

        return passableCounts;
    }

    private @NotNull @Unmodifiable List<BlockPos> getTestPoints(int radius) {
        int maxOffset = Math.max(1, radius / 2);
        int rawOffset = (int) Math.ceil(radius * 0.36);
        int offset = Mth.clamp(rawOffset, 1, maxOffset);

        return List.of(
            new BlockPos(offset, 0, offset),
            new BlockPos(-offset, 0, offset),
            new BlockPos(offset, 0, -offset),
            new BlockPos(-offset, 0, -offset)
        );
    }

    @Nullable
    private Pair<BlockPos, Direction> findSurface(@NotNull WorldGenLevel level, @NotNull BlockPos origin, @NotNull PlantopiaBranchingShrubPatchConfiguration config, @NotNull RandomSource random, int xzSpread, int maxHeight) {
        int searchDistance = config.searchDistance().sample(random);
        var searchDirections = Lists.newArrayList(Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

        PlantopiaMathHelper.shuffle(searchDirections, random);

        for (var direction : searchDirections) {
            var candidateFace = direction.getOpposite();

            var candidateState = getBranchingShrubBlock().defaultBlockState()
                .setValue(PlantopiaBranchingShrubBlock.BASE, true)
                .setValue(PlantopiaBranchingShrubBlock.FACING, candidateFace);

            for (int i = 0; i < searchDistance; i++) {
                var candidatePos = origin.relative(direction, i);

                if (!canReplace(level, candidatePos)) continue;
                if (!candidateState.canSurvive(level, candidatePos)) continue;
                if (!mayPlace(level, candidatePos, candidateState)) continue;
                if (!isAreaFavorable(level, candidatePos, candidateFace, xzSpread, maxHeight)) continue;

                return Pair.of(candidatePos, candidateFace);
            }
        }

        return null;
    }
}
