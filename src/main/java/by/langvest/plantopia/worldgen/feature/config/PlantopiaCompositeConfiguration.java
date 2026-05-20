package by.langvest.plantopia.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public record PlantopiaCompositeConfiguration(
    Holder<PlacedFeature> baseFeature,
    List<WeightedPlacedFeature> features
) implements FeatureConfiguration {
    public static final Codec<PlantopiaCompositeConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        PlacedFeature.CODEC.fieldOf("base_feature").forGetter(PlantopiaCompositeConfiguration::baseFeature),
        WeightedPlacedFeature.CODEC.listOf().fieldOf("features").forGetter(PlantopiaCompositeConfiguration::features)
    ).apply(instance, PlantopiaCompositeConfiguration::new));
}
