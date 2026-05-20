package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.worldgen.feature.config.PlantopiaCompositeConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.NotNull;

public class PlantopiaCompositeFeature extends Feature<PlantopiaCompositeConfiguration> {
    public PlantopiaCompositeFeature(Codec<PlantopiaCompositeConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<PlantopiaCompositeConfiguration> context) {
        var config = context.config();
        var random = context.random();
        var level = context.level();
        var chunkGenerator = context.chunkGenerator();
        var origin = context.origin();

        var successfullyPlaced = config.baseFeature().value().place(level, chunkGenerator, random, origin);

        if (successfullyPlaced) {
            for (var feature : config.features()) {
                if (random.nextFloat() < feature.chance) {
                    feature.place(level, chunkGenerator, random, origin);
                }
            }
        }

        return successfullyPlaced;
    }
}
