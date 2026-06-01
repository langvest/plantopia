package by.langvest.plantopia.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record PlantopiaIcicleConfiguration(
    float chanceOfTallerIcicle,
    float chanceOfIcicleSpread,
    float chanceOfSpreadRadius2,
    float chanceOfSpreadRadius3
) implements FeatureConfiguration {
    public static final Codec<PlantopiaIcicleConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_taller_icicle").orElse(0.2F).forGetter(PlantopiaIcicleConfiguration::chanceOfTallerIcicle),
        Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_icicle_spread").orElse(0.7F).forGetter(PlantopiaIcicleConfiguration::chanceOfIcicleSpread),
        Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spread_radius2").orElse(0.5F).forGetter(PlantopiaIcicleConfiguration::chanceOfSpreadRadius2),
        Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spread_radius3").orElse(0.5F).forGetter(PlantopiaIcicleConfiguration::chanceOfSpreadRadius3)
    ).apply(instance, PlantopiaIcicleConfiguration::new));
}
