package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaBranchingShrubBlock;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.util.helper.PlantopiaMathHelper;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaBranchingShrubPatchConfiguration;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;

@ParametersAreNonnullByDefault
public class PlantopiaBranchingShrubPatchFeature extends Feature<PlantopiaBranchingShrubPatchConfiguration> {
    public PlantopiaBranchingShrubPatchFeature(Codec<PlantopiaBranchingShrubPatchConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PlantopiaBranchingShrubPatchConfiguration> context) {
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
            double distanceToCenter = Math.sqrt(Mth.square(xzOffset.getX()) + Mth.square(xzOffset.getZ()));
            double falloffFactor = 1.0 - (distanceToCenter / xzSpread) * heightFalloff;
            double smoothHeight = maxHeight * Mth.clamp(falloffFactor, 0.0, 1.0);
            double erodedOffset = (random.nextDouble() * 2 - 1) * maxHeight * heightErosion;
            int height = (int) Math.round(Mth.clamp(smoothHeight + erodedOffset, 0.0, maxHeight));
            if (growthDirection == Direction.UP && height == 1 && random.nextFloat() < 0.35F) height++;

            if (placeColumn(level, centerPos, localPos, growthDirection, height, config, random)) {
                successfulPlacements++;
            }
        }

        return successfulPlacements > 0;
    }

    private boolean placeColumn(WorldGenLevel level, BlockPos centerPos, BlockPos.MutableBlockPos localPos, Direction growthDirection, int height, PlantopiaBranchingShrubPatchConfiguration config, RandomSource random) {
        if (height <= 0) {
            return false;
        }

        var allowedPlacement = growthDirection.getAxis().isVertical() ? config.allowedVerticalPlacement() : config.allowedHorizontalPlacement();
        var allowedAttachment = config.allowedAttachment();
        var columnBasePos = rotate(localPos, growthDirection).offset(centerPos);
        var baseState = getBaseState(growthDirection);
        var bodyState = getBodyState(growthDirection);

        if (!allowedPlacement.test(level, columnBasePos)) {
            return false;
        }

        if (!mayPlaceAt(level, columnBasePos, growthDirection, allowedAttachment)) {
            return false;
        }

        if (!baseState.canSurvive(level, columnBasePos)) {
            return false;
        }

        return PlantopiaNaturalBlockColumnFeature.place(
            level,
            columnBasePos,
            random,
            growthDirection,
            allowedPlacement,
            false,
            Block.UPDATE_CLIENTS,
            List.of(
                BlockColumnConfiguration.layer(
                    ConstantInt.of(1),
                    BlockStateProvider.simple(baseState)
                ),
                BlockColumnConfiguration.layer(
                    ConstantInt.of(height - 1),
                    BlockStateProvider.simple(bodyState)
                )
            )
        );
    }

    private BlockPos.MutableBlockPos rotate(BlockPos.MutableBlockPos pos, Direction direction) {
        return switch (direction) {
            case DOWN -> pos.set(pos.getX(), -pos.getY(), pos.getZ());
            case UP -> pos;
            case NORTH -> pos.set(pos.getX(), pos.getZ(), -pos.getY());
            case SOUTH -> pos.set(pos.getX(), pos.getZ(), pos.getY());
            case WEST -> pos.set(-pos.getY(), pos.getX(), pos.getZ());
            case EAST -> pos.set(pos.getY(), pos.getX(), pos.getZ());
        };
    }

    private boolean canBreatheThrough(WorldGenLevel level, BlockPos pos) {
        return canBreatheThrough(level.getBlockState(pos));
    }

    private boolean canBreatheThrough(BlockState state) {
        if (state.is(BlockTags.LEAVES)) return false;
        if (state.is(PlantopiaBlocks.SEA_HANGING_MOSS.get())) return false;
        return !state.is(PlantopiaBlockTags.GROUND_OVERWORLD);
    }

    private Block getPlantBlock() {
        return PlantopiaBlocks.BRANCHING_SHRUB.get();
    }

    private BlockState getBaseState(Direction growthDirection) {
        return getPlantBlock().defaultBlockState()
            .setValue(PlantopiaBranchingShrubBlock.BASE, true)
            .setValue(PlantopiaBranchingShrubBlock.FACING, growthDirection);
    }

    private BlockState getBodyState(Direction growthDirection) {
        return getPlantBlock().defaultBlockState()
            .setValue(PlantopiaBranchingShrubBlock.BASE, false)
            .setValue(PlantopiaBranchingShrubBlock.FACING, growthDirection);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean mayPlaceAt(WorldGenLevel level, BlockPos pos, Direction growthDirection, BlockPredicate allowedAttachment) {
        var attachedPos = pos.relative(growthDirection.getOpposite());
        var attachedState = level.getBlockState(attachedPos);

        if (attachedState.is(getPlantBlock())) {
            return false;
        }

        if (!attachedState.isFaceSturdy(level, attachedPos, growthDirection)) {
            return false;
        }

        if (growthDirection == Direction.UP) {
            var fluidState = level.getFluidState(pos);

            if (attachedState.is(Blocks.GRASS_BLOCK) || !fluidState.isEmpty()) {
                int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX(), pos.getZ());

                if (pos.getY() < surfaceY) {
                    return false;
                }
            }
        }

        return allowedAttachment.test(level, attachedPos);
    }

    private float getAirRatio(int[] passableCounts, int maxHeight) {
        int totalPassable = Arrays.stream(passableCounts).sum();
        return (float) totalPassable / (passableCounts.length * maxHeight);
    }

    private float getAirThreshold(WorldGenLevel level, BlockPos pos) {
        int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX(), pos.getZ());
        int depth = surfaceY - pos.getY();
        double delta = Mth.inverseLerp(depth, 0, 20);

        return (float) Mth.lerp(delta, 0.5F, 0.1F);
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

    private float getUniformThreshold(WorldGenLevel level, BlockPos pos, Direction direction) {
        if (direction.getAxis().isVertical()) {
            return 0.1F;
        }

        int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX(), pos.getZ());
        int depth = surfaceY - pos.getY();
        double delta = Mth.inverseLerp(depth, 0, 15);

        return (float) Mth.lerp(delta, 0.6F, 0.3F);
    }

    private boolean isAreaFavorableAt(WorldGenLevel level, BlockPos pos, Direction direction, int radius, int maxHeight) {
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

    private int [] collectColumnData(WorldGenLevel level, BlockPos pos, Direction growthDirection, int radius, int maxHeight) {
        var testPoints = getTestPoints(radius);
        int[] passableCounts = new int[testPoints.size()];

        for (int idx = 0; idx < testPoints.size(); idx++) {
            var point = testPoints.get(idx);
            var testPos = rotate(point, growthDirection).offset(pos).mutable();
            int count = 0;
            for (int i = 0; i < maxHeight; i++) {
                if (canBreatheThrough(level, testPos)) {
                    count++;
                }
                testPos.move(growthDirection);
            }
            passableCounts[idx] = count;
        }

        return passableCounts;
    }

    private @Unmodifiable List<BlockPos.MutableBlockPos> getTestPoints(int radius) {
        int maxOffset = Math.max(1, radius / 2);
        int rawOffset = (int) Math.ceil(radius * 0.36);
        int offset = Mth.clamp(rawOffset, 1, maxOffset);

        return List.of(
            new BlockPos.MutableBlockPos(offset, 0, offset),
            new BlockPos.MutableBlockPos(-offset, 0, offset),
            new BlockPos.MutableBlockPos(offset, 0, -offset),
            new BlockPos.MutableBlockPos(-offset, 0, -offset)
        );
    }

    private int getAdjustedMaxHeight(WorldGenLevel level, BlockPos pos, Direction growthDirection, int maxHeight) {
        int smallHeight = 2;

        if (growthDirection.getAxis().isHorizontal()) {
            return Math.min(maxHeight, smallHeight);
        }

        int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX(), pos.getZ());
        int depth = surfaceY - pos.getY();
        float delta = (float) Mth.clamp(Mth.inverseLerp(depth, 0, 12), 0.0, 1.0);

        if (growthDirection == Direction.UP) {
            return (int) Mth.lerp(delta, maxHeight, Math.min(maxHeight, smallHeight));
        }

        if (growthDirection == Direction.DOWN) {
            return (int) Mth.lerp(delta, Math.min(maxHeight, smallHeight), maxHeight);
        }

        return maxHeight;
    }

    private int getAdjustedXZSpread(WorldGenLevel level, BlockPos pos, Direction growthDirection, int xzSpread) {
        int smallXZSpread = 1;

        if (growthDirection.getAxis().isHorizontal()) {
            int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX(), pos.getZ());
            int depth = surfaceY - pos.getY();
            float delta = (float) Mth.clamp(Mth.inverseLerp(depth, 0, 12), 0.0, 1.0);

            return (int) Mth.lerp(delta, Math.min(xzSpread, smallXZSpread), xzSpread);
        }

        return xzSpread;
    }

    private List<Direction> getAllowedGrowthDirections(PlantopiaBranchingShrubPatchConfiguration config, RandomSource random) {
        var allowedGrowthDirections = config.growthDirections();
        var horizontalDirections = Lists.<Direction>newArrayList();
        var growthDirections = Lists.<Direction>newArrayList();

        for (var direction : allowedGrowthDirections) {
            if (direction.getAxis().isVertical()) {
                growthDirections.add(direction);
            } else {
                horizontalDirections.add(direction);
            }
        }

        if (!horizontalDirections.isEmpty()) {
            PlantopiaMathHelper.shuffle(horizontalDirections, random);
            growthDirections.addAll(horizontalDirections);
        }

        return growthDirections;
    }

    @Nullable
    private PlacementInfo findPlacement(WorldGenLevel level, BlockPos originPos, PlantopiaBranchingShrubPatchConfiguration config, RandomSource random) {
        int searchDistance = config.searchDistance().sample(random);
        int maxHeight = config.height().sample(random);
        int xzSpread = config.xzSpread().sample(random);
        int ySpread = config.ySpread().sample(random);
        var allowedAttachment = config.allowedAttachment();
        var growthDirections = getAllowedGrowthDirections(config, random);

        for (var direction : growthDirections) {
            var allowedPlacement = direction.getAxis().isVertical() ? config.allowedVerticalPlacement() : config.allowedHorizontalPlacement();
            var candidateState = getBaseState(direction);

            for (int i = 0; i < searchDistance; i++) {
                var candidatePos = originPos.relative(direction.getOpposite(), i);

                if (!allowedPlacement.test(level, candidatePos)) continue;
                if (!mayPlaceAt(level, candidatePos, direction, allowedAttachment)) continue;
                if (!candidateState.canSurvive(level, candidatePos)) continue;

                var adjustedMaxHeight = getAdjustedMaxHeight(level, candidatePos, direction, maxHeight);
                var adjustedXZSpread = getAdjustedXZSpread(level, candidatePos, direction, xzSpread);
                var favorableMaxHeight = adjustedMaxHeight + 1;

                if (!isAreaFavorableAt(level, candidatePos, direction, adjustedXZSpread, favorableMaxHeight)) continue;

                return new PlacementInfo(candidatePos, direction, adjustedMaxHeight, adjustedXZSpread, ySpread);
            }
        }

        return null;
    }

    private record PlacementInfo(BlockPos pos, Direction direction, int maxHeight, int xzSpread, int ySpread) {
    }
}
