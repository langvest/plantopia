package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.block.PlantopiaNaturalBlock;
import by.langvest.plantopia.block.special.PlantopiaTriplePlantBlock;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
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

        if(!candidateState.canSurvive(level, pos)) return false;

        if(candidateBlock instanceof PlantopiaNaturalBlock naturalBlock) {
            if(!naturalBlock.canPlaceNaturallyAt(level, pos, random)) return false;

            naturalBlock.placeNaturallyAt(level, pos, candidateState, random, 2);

            return true;
        }

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
}
