package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaIcicleBlock;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.worldgen.feature.special.PlantopiaNaturalBlockColumnFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import org.jetbrains.annotations.NotNull;

public final class PlantopiaIcicleUtils {
    public static double getIcicleHeight(double radius, double maxRadius, double scale, double minRadius) {
        if (radius < minRadius) {
            radius = minRadius;
        }

        double d0 = 0.384D;
        double d1 = radius / maxRadius * d0;
        double d2 = 0.75D * Math.pow(d1, 1.3333333333333333D);
        double d3 = Math.pow(d1, 0.6666666666666666D);
        double d4 = 0.3333333333333333D * Math.log(d1);
        double d5 = scale * (d2 - d3 - d4);
        d5 = Math.max(d5, 0.0D);
        return d5 / d0 * maxRadius;
    }

    public static boolean isCircleMostlyEmbeddedInStone(WorldGenLevel level, BlockPos pos, int radius) {
        if (isEmptyOrWaterOrLava(level, pos)) {
            return false;
        }

        float angleStep = 6.0F / (float) radius;
        for (float angle = 0.0F; angle < ((float) Math.PI * 2F); angle += angleStep) {
            int xOffset = (int) (Mth.cos(angle) * (float) radius);
            int zOffset = (int) (Mth.sin(angle) * (float) radius);
            if (isEmptyOrWaterOrLava(level, pos.offset(xOffset, 0, zOffset))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmptyOrWater(@NotNull LevelAccessor level, BlockPos pos) {
        return level.isStateAtPosition(pos, PlantopiaIcicleUtils::isEmptyOrWater);
    }

    public static boolean isEmptyOrWaterOrLava(@NotNull LevelAccessor level, BlockPos pos) {
        return level.isStateAtPosition(pos, PlantopiaIcicleUtils::isEmptyOrWaterOrLava);
    }

    public static void growIcicleOnIceIfPossible(@NotNull WorldGenLevel level, @NotNull BlockPos pos, @NotNull Direction direction, int height, boolean mergeTip, RandomSource random) {
        var oppositeDirection = direction.getOpposite();
        var attachedPos = pos.relative(oppositeDirection);
        var attachedState = level.getBlockState(attachedPos);

        if (!isValidGround(attachedState)) {
            return;
        }

        growIcicle(level, pos, direction, height, Block.UPDATE_CLIENTS, mergeTip, random);
    }

    public static boolean growIcicle(@NotNull WorldGenLevel level, @NotNull BlockPos pos, @NotNull Direction direction, int height, int flags, boolean mergeTip, RandomSource random) {
        if (height <= 0) {
            return false;
        }

        return PlantopiaNaturalBlockColumnFeature.place(
            level,
            pos,
            random,
            direction,
            BlockPredicate.alwaysTrue(),
            false,
            flags,
            4,
            (l, p, r, layerIndex, totalHeight) -> switch (layerIndex) {
                case 0 -> height >= 3 ? 1 : 0;
                case 1 -> Math.max(0, height - 3);
                case 2 -> height >= 2 ? 1 : 0;
                case 3 -> height >= 1 ? 1 : 0;
                default -> 0;
            },
            (l, p, r, layerIndex, blockIndex, layerHeight, totalHeight) -> switch (layerIndex) {
                case 0 -> getIcicleState(direction, DripstoneThickness.BASE);
                case 1 -> getIcicleState(direction, DripstoneThickness.MIDDLE);
                case 2 -> getIcicleState(direction, DripstoneThickness.FRUSTUM);
                case 3 -> getIcicleState(direction, mergeTip ? DripstoneThickness.TIP_MERGE : DripstoneThickness.TIP);
                default -> Blocks.AIR.defaultBlockState();
            }
        );
    }

    public static boolean placeIceBlockIfPossible(@NotNull LevelAccessor level, BlockPos pos) {
        var state = level.getBlockState(pos);

        if (state.is(getIceBlock())) {
            return true;
        }

        if (state.is(PlantopiaBlockTags.PACKED_ICE_REPLACEABLE_BLOCKS)) {
            level.setBlock(pos, getIceState(), Block.UPDATE_CLIENTS);
            return true;
        }

        return false;
    }

    public static @NotNull BlockState getIcicleState(Direction direction, DripstoneThickness thickness) {
        return getIcicleBlock().defaultBlockState()
            .setValue(PlantopiaIcicleBlock.TIP_DIRECTION, direction)
            .setValue(PlantopiaIcicleBlock.THICKNESS, thickness);
    }

    public static @NotNull BlockState getIceState() {
        return getIceBlock().defaultBlockState();
    }

    public static boolean isValidGround(@NotNull BlockState state) {
        return state.is(getIceBlock()) || state.is(PlantopiaBlockTags.PACKED_ICE_REPLACEABLE_BLOCKS);
    }

    public static boolean isEmptyOrWater(@NotNull BlockState state) {
        return state.isAir() || state.is(Blocks.WATER);
    }

    public static boolean isNeitherEmptyNorWater(BlockState state) {
        return !isEmptyOrWater(state);
    }

    public static boolean isEmptyOrWaterOrLava(@NotNull BlockState state) {
        return state.isAir() || state.is(Blocks.WATER) || state.is(Blocks.LAVA);
    }

    public static Block getIcicleBlock() {
        return PlantopiaBlocks.ICICLE.get();
    }

    public static Block getIceBlock() {
        return Blocks.PACKED_ICE;
    }
}
