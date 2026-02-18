package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.block.PlantopiaFreezableBlock;
import by.langvest.plantopia.block.PlantopiaNaturalBlock;
import by.langvest.plantopia.block.special.PlantopiaTriplePlantBlock;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;

public class PlantopiaNaturalBlockFeature extends Feature<SimpleBlockConfiguration> {
    public PlantopiaNaturalBlockFeature(Codec<SimpleBlockConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<SimpleBlockConfiguration> context) {
        var config = context.config();
        var level = context.level();
        var random = context.random();
        var pos = context.origin();
        var state = config.toPlace().getState(random, pos);

        return place(level, state, pos, random, Block.UPDATE_CLIENTS);
    }

    public boolean place(WorldGenLevel level, @NotNull BlockState state, BlockPos pos, RandomSource random, int flags) {
        var block = state.getBlock();

        if(block instanceof PlantopiaNaturalBlock naturalBlock) {
            return naturalBlock.generateAt(level, pos, state, random, flags);
        }

        if(!state.canSurvive(level, pos)) return false;
        if(!isValidPosToPlace(state, level, pos)) return false;

        if(block instanceof DoublePlantBlock) {
            if(!level.isEmptyBlock(pos.above(1))) return false;

            DoublePlantBlock.placeAt(level, state, pos, flags);

            return true;
        }

        if(block instanceof PlantopiaTriplePlantBlock) {
            if(!level.isEmptyBlock(pos.above(1))) return false;
            if(!level.isEmptyBlock(pos.above(2))) return false;

            PlantopiaTriplePlantBlock.placeAt(level, pos, state, flags);

            return true;
        }

        return level.setBlock(pos, copyWaterloggedFrom(level, pos, state), flags);
    }

    protected boolean isValidPosToPlace(BlockState candidateState, @NotNull WorldGenLevel level, BlockPos pos) {
        var currentState = level.getBlockState(pos);
        var currentFluidState = level.getFluidState(pos);

        if(currentFluidState.isSourceOfType(Fluids.WATER)) {
            var biome = level.getBiome(pos).get();

            if(biome.shouldFreeze(level, pos)) {
                if(candidateState.getBlock() instanceof PlantopiaFreezableBlock freezableBlock) {
                    return freezableBlock.shouldIce(candidateState, level, pos, true);
                } else {
                    return false;
                }
            }
        }

        return true;
    }
}
