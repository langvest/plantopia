package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.block.PlantopiaFreezableBlock;
import by.langvest.plantopia.block.PlantopiaNaturalBlock;
import by.langvest.plantopia.block.special.PlantopiaTriplePlantBlock;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;
import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.isWaterSourceBlock;

@ParametersAreNonnullByDefault
public class PlantopiaNaturalBlockFeature extends Feature<SimpleBlockConfiguration> {
    public PlantopiaNaturalBlockFeature(Codec<SimpleBlockConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> context) {
        var config = context.config();
        var random = context.random();
        var pos = context.origin();

        return place(
            context.level(),
            config.toPlace().getState(random, pos),
            pos,
            random,
            Block.UPDATE_CLIENTS
        );
    }

    public static boolean isEmptyBlock(BlockState state) {
        return state.isAir();
    }

    public static boolean isWaterBlock(BlockState state) {
        return isWaterSourceBlock(state);
    }

    public static boolean isEmptyFluid(FluidState state) {
        return state.isEmpty();
    }

    public static boolean isWaterFluid(FluidState state) {
        return state.isSourceOfType(Fluids.WATER);
    }

    public static boolean place(ServerLevelAccessor level, BlockState state, BlockPos pos, RandomSource random, int flags) {
        var block = state.getBlock();

        if (!state.canSurvive(level, pos)) return false;

        boolean isLiquidContainer = block instanceof LiquidBlockContainer;
        boolean isWaterloggable = state.hasProperty(BlockStateProperties.WATERLOGGED);

        Predicate<BlockPos> invadePredicate = candidatePos -> {
            if (!isFavorablePos(state, level, candidatePos)) return false;
            var currentFluidState = level.getFluidState(candidatePos);
            if (isLiquidContainer && !isWaterloggable) return isWaterFluid(currentFluidState);
            if (isWaterloggable) return isEmptyFluid(currentFluidState) || isWaterFluid(currentFluidState);
            return isEmptyFluid(currentFluidState);
        };

        Predicate<BlockPos> spreadPredicate = candidatePos -> {
            if (!isFavorablePos(state, level, candidatePos)) return false;
            var candidateState = level.getBlockState(candidatePos);
            if (isLiquidContainer && !isWaterloggable) return isWaterBlock(candidateState);
            if (isWaterloggable) return isEmptyBlock(candidateState) || isWaterBlock(candidateState);
            return isEmptyBlock(candidateState);
        };

        if (!invadePredicate.test(pos)) return false;

        if (block instanceof PlantopiaNaturalBlock naturalBlock) {
            var context = new PlantopiaNaturalBlock.PlaceContext(level, pos, state, flags, random, invadePredicate, spreadPredicate);

            return naturalBlock.placeNaturally(context);
        }

        if (block instanceof DoublePlantBlock) {
            if (!spreadPredicate.test(pos.above(1))) return false;

            DoublePlantBlock.placeAt(level, state, pos, flags);

            return true;
        }

        if (block instanceof PlantopiaTriplePlantBlock) {
            if (!spreadPredicate.test(pos.above(1))) return false;
            if (!spreadPredicate.test(pos.above(2))) return false;

            PlantopiaTriplePlantBlock.placeAt(level, pos, state, flags);

            return true;
        }

        return level.setBlock(pos, copyWaterloggedFrom(level, pos, state), flags);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isFavorablePos(BlockState state, ServerLevelAccessor level, BlockPos pos) {
        var currentFluidState = level.getFluidState(pos);

        if (currentFluidState.isSourceOfType(Fluids.WATER)) {
            var biome = level.getBiome(pos).value();

            if (biome.shouldFreeze(level, pos, false)) {
                int iceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, pos.getX(), pos.getZ()) - 1;

                if (pos.getY() == iceY) {
                    if (state.getBlock() instanceof PlantopiaFreezableBlock freezableBlock) {
                        return freezableBlock.shouldIce(copyWaterloggedFrom(level, pos, state), level, pos, false);
                    } else {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
