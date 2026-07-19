package by.langvest.plantopia.worldgen.feature.special;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class PlantopiaDeciduousTreeFeature extends PlantopiaAbstractTreeFeature {
    protected final List<TreeModifier> pipeline;

    public PlantopiaDeciduousTreeFeature(Codec<TreeConfiguration> codec) {
        super(codec);
        this.pipeline = List.of(this::makeStructure, this::applyDecorators, this::updateLeaves);
    }

    @Override
    protected List<TreeModifier> getTreePipeline(FeaturePlaceContext<TreeConfiguration> context) {
        return pipeline;
    }
}
