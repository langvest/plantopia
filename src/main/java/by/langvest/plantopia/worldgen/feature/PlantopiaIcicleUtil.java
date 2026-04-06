package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.block.special.PlantopiaIcicleBlock;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class PlantopiaIcicleUtil {
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
        return level.isStateAtPosition(pos, PlantopiaIcicleUtil::isEmptyOrWater);
    }

    public static boolean isEmptyOrWaterOrLava(@NotNull LevelAccessor level, BlockPos pos) {
        return level.isStateAtPosition(pos, PlantopiaIcicleUtil::isEmptyOrWaterOrLava);
    }

    public static void buildBaseToTipColumn(Direction direction, int height, boolean mergeTip, Consumer<BlockState> blockSetter) {
        if (height >= 3) {
            blockSetter.accept(createIcicle(direction, DripstoneThickness.BASE));
            for (int i = 0; i < height - 3; ++i) {
                blockSetter.accept(createIcicle(direction, DripstoneThickness.MIDDLE));
            }
        }

        if (height >= 2) {
            blockSetter.accept(createIcicle(direction, DripstoneThickness.FRUSTUM));
        }

        if (height >= 1) {
            blockSetter.accept(createIcicle(direction, mergeTip ? DripstoneThickness.TIP_MERGE : DripstoneThickness.TIP));
        }
    }

    public static void growIcicle(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull Direction direction, int height, boolean mergeTip) {
        if (!isIcicleBase(level.getBlockState(pos.relative(direction.getOpposite())))) {
            return;
        }

        var mutablePos = pos.mutable();
        buildBaseToTipColumn(direction, height, mergeTip, (state) -> {
            if (state.is(getIcicleBlock())) {
                state = state.setValue(PlantopiaIcicleBlock.WATERLOGGED, level.isWaterAt(mutablePos));
            }
            level.setBlock(mutablePos, state, 2);
            mutablePos.move(direction);
        });
    }

    public static boolean placeIceBlockIfPossible(@NotNull LevelAccessor level, BlockPos pos) {
        var state = level.getBlockState(pos);
        if (state.is(getBaseBlock())) {
            return true;
        }
        if (state.is(PlantopiaBlockTags.PACKED_ICE_REPLACEABLE_BLOCKS)) {
            level.setBlock(pos, getBaseBlock().defaultBlockState(), 2);
            return true;
        }
        return false;
    }

    private static @NotNull BlockState createIcicle(Direction direction, DripstoneThickness thickness) {
        return getIcicleBlock().defaultBlockState()
                .setValue(PlantopiaIcicleBlock.TIP_DIRECTION, direction)
                .setValue(PlantopiaIcicleBlock.THICKNESS, thickness);
    }

    public static boolean isIcicleBaseOrLava(BlockState state) {
        return isIcicleBase(state) || state.is(Blocks.LAVA);
    }

    public static boolean isIcicleBase(@NotNull BlockState state) {
        return state.is(getBaseBlock()) || state.is(PlantopiaBlockTags.PACKED_ICE_REPLACEABLE_BLOCKS);
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
        return PlantopiaIcicleBlock.getIcicleBlock();
    }

    public static Block getBaseBlock() {
        return Blocks.PACKED_ICE;
    }
}
