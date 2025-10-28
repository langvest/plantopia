package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.block.PlantopiaFreezableBlock;
import by.langvest.plantopia.block.PlantopiaNaturalBlock;
import by.langvest.plantopia.block.special.PlantopiaTriplePlantBlock;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
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
        var candidateState = config.toPlace().getState(random, pos);
        var candidateBlock = candidateState.getBlock();

        if(candidateBlock instanceof PlantopiaNaturalBlock naturalBlock) {
            return naturalBlock.generateAt(level, pos, candidateState, random, 2);
        }

        if(!candidateState.canSurvive(level, pos)) return false;
        if(!isValidPosToPlace(candidateState, level, pos)) return false;

        if(candidateBlock instanceof DoublePlantBlock) {
            if(!level.isEmptyBlock(pos.above(1))) return false;

            DoublePlantBlock.placeAt(level, candidateState, pos, 2);

            return true;
        }

        if(candidateBlock instanceof PlantopiaTriplePlantBlock) {
            if(!level.isEmptyBlock(pos.above(1))) return false;
            if(!level.isEmptyBlock(pos.above(2))) return false;

            PlantopiaTriplePlantBlock.placeAt(level, pos, candidateState, 2);

            return true;
        }

        return level.setBlock(pos, copyWaterloggedFrom(level, pos, candidateState), 2);
    }

    protected boolean isValidPosToPlace(BlockState candidateState, @NotNull WorldGenLevel level, BlockPos pos) {
        var fluidState = level.getFluidState(pos);

        if(fluidState.isSourceOfType(Fluids.WATER)) {
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
