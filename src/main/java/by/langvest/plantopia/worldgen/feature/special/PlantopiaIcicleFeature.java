package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.block.special.PlantopiaIcicleBlock;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaIcicleConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

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

        int height = random.nextFloat() < config.chanceOfTallerIcicle() && isEmptyOrWater(level.getBlockState(origin.relative(direction))) ? 2 : 1;
        growIcicle(level, origin, direction, height);
        return true;
    }

    private static Optional<Direction> getTipDirection(@NotNull LevelAccessor level, @NotNull BlockPos pos, RandomSource random) {
        boolean canPlaceAbove = isIcicleBase(level.getBlockState(pos.above()));
        boolean canPlaceBelow = isIcicleBase(level.getBlockState(pos.below()));

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
        placeIceBlockIfPossible(level, pos);

        for (var direction : Direction.Plane.HORIZONTAL) {
            if (random.nextFloat() > config.chanceOfIcicleSpread()) {
                continue;
            }

            var pos1 = pos.relative(direction);
            placeIceBlockIfPossible(level, pos1);

            if (random.nextFloat() > config.chanceOfSpreadRadius2()) {
                continue;
            }

            var pos2 = pos1.relative(Direction.getRandom(random));
            placeIceBlockIfPossible(level, pos2);

            if (random.nextFloat() > config.chanceOfSpreadRadius3()) {
                continue;
            }

            var pos3 = pos2.relative(Direction.getRandom(random));
            placeIceBlockIfPossible(level, pos3);
        }
    }

    protected static void buildBaseToTipColumn(Direction direction, int height, Consumer<BlockState> blockSetter) {
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
            blockSetter.accept(createIcicle(direction, DripstoneThickness.TIP));
        }
    }

    protected static void growIcicle(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull Direction direction, int height) {
        if (!isIcicleBase(level.getBlockState(pos.relative(direction.getOpposite())))) {
            return;
        }

        var mutablePos = pos.mutable();
        buildBaseToTipColumn(direction, height, (state) -> {
            if (state.is(PlantopiaIcicleBlock.getIcicleBlock())) {
                state = state.setValue(PlantopiaIcicleBlock.WATERLOGGED, level.isWaterAt(mutablePos));
            }
            level.setBlock(mutablePos, state, 2);
            mutablePos.move(direction);
        });
    }

    protected static void placeIceBlockIfPossible(@NotNull LevelAccessor level, BlockPos pos) {
        var state = level.getBlockState(pos);

        if (state.is(getBaseBlock())) {
            return;
        }

        if (state.is(PlantopiaBlockTags.PACKED_ICE_REPLACEABLE_BLOCKS)) {
            level.setBlock(pos, getBaseBlock().defaultBlockState(), 2);
        }
    }

    private static @NotNull BlockState createIcicle(Direction direction, DripstoneThickness thickness) {
        return PlantopiaIcicleBlock.getIcicleBlock().defaultBlockState()
                .setValue(PlantopiaIcicleBlock.TIP_DIRECTION, direction)
                .setValue(PlantopiaIcicleBlock.THICKNESS, thickness);
    }

    public static boolean isIcicleBase(@NotNull BlockState state) {
        return state.is(getBaseBlock()) || state.is(PlantopiaBlockTags.PACKED_ICE_REPLACEABLE_BLOCKS);
    }

    public static boolean isEmptyOrWater(@NotNull BlockState state) {
        return state.isAir() || state.is(Blocks.WATER);
    }

    private static Block getBaseBlock() {
        return Blocks.PACKED_ICE;
    }
}
