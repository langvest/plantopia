package by.langvest.plantopia.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public record PlantopiaLimitedRandomPatchConfiguration(
    IntProvider tries,
    IntProvider limit,
    IntProvider xzSpread,
    IntProvider ySpread,
    Holder<PlacedFeature> feature
) implements FeatureConfiguration {
    public static final Codec<PlantopiaLimitedRandomPatchConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        IntProvider.codec(0, 512).fieldOf("tries").forGetter(PlantopiaLimitedRandomPatchConfiguration::tries),
        IntProvider.codec(0, 512).fieldOf("limit").forGetter(PlantopiaLimitedRandomPatchConfiguration::limit),
        IntProvider.codec(0, 128).fieldOf("xz_spread").forGetter(PlantopiaLimitedRandomPatchConfiguration::xzSpread),
        IntProvider.codec(0, 128).fieldOf("y_spread").forGetter(PlantopiaLimitedRandomPatchConfiguration::ySpread),
        PlacedFeature.CODEC.fieldOf("feature").forGetter(PlantopiaLimitedRandomPatchConfiguration::feature)
    ).apply(instance, PlantopiaLimitedRandomPatchConfiguration::new));
}
