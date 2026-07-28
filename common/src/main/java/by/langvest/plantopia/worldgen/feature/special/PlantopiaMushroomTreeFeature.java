package by.langvest.plantopia.worldgen.feature.special;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class PlantopiaMushroomTreeFeature extends PlantopiaAbstractTreeFeature<TreeConfiguration> {
    public PlantopiaMushroomTreeFeature(Codec<TreeConfiguration> codec) {
        super(codec);
    }

    @Override
    protected List<TreeModifier> getTreePipeline(FeaturePlaceContext<TreeConfiguration> context) {
        var config = context.config();

        return List.of(
            makeStructure(config),
            applyDecorators(config.decorators)
        );
    }

    @Override
    protected int getBlockUpdateFlags() {
        return Block.UPDATE_ALL;
    }
}
