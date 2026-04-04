package by.langvest.plantopia.worldgen.feature.config;

import by.langvest.plantopia.worldgen.placement.PlantopiaMultiNoiseConfig;
import by.langvest.plantopia.worldgen.placement.PlantopiaThresholdType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record PlantopiaAzollaAndMossConfiguration(
    PlantopiaMultiNoiseConfig noiseConfig,
    PlantopiaThresholdType activationType,
    float noiseLevel,
    float erosion,
    int blurRadius,
    BlockPredicate predicate,
    HolderSet<Biome> allowedBiomes
) implements FeatureConfiguration {
    public static final Codec<PlantopiaAzollaAndMossConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        PlantopiaMultiNoiseConfig.CODEC.fieldOf("noise_config").forGetter(PlantopiaAzollaAndMossConfiguration::noiseConfig),
        PlantopiaThresholdType.CODEC.fieldOf("activation_type").forGetter(PlantopiaAzollaAndMossConfiguration::activationType),
        Codec.FLOAT.fieldOf("noise_level").forGetter(PlantopiaAzollaAndMossConfiguration::noiseLevel),
        Codec.FLOAT.fieldOf("erosion").forGetter(PlantopiaAzollaAndMossConfiguration::erosion),
        ExtraCodecs.POSITIVE_INT.optionalFieldOf("blur_radius", 3).forGetter(PlantopiaAzollaAndMossConfiguration::blurRadius),
        BlockPredicate.CODEC.fieldOf("predicate").forGetter(PlantopiaAzollaAndMossConfiguration::predicate),
        Biome.LIST_CODEC.fieldOf("allowed_biomes").forGetter(PlantopiaAzollaAndMossConfiguration::allowedBiomes)
    ).apply(instance, PlantopiaAzollaAndMossConfiguration::new));
}
