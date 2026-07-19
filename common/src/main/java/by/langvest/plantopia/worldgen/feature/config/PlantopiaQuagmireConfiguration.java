package by.langvest.plantopia.worldgen.feature.config;

import by.langvest.plantopia.worldgen.placement.PlantopiaMultiNoiseConfig;
import by.langvest.plantopia.worldgen.util.PlantopiaThresholdType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record PlantopiaQuagmireConfiguration(
    PlantopiaMultiNoiseConfig noiseConfig,
    PlantopiaThresholdType activationType,
    float noiseLevel,
    float erosion,
    int blurRadius,
    BlockPredicate predicate,
    HolderSet<Biome> allowedBiomes
) implements FeatureConfiguration {
    public static final Codec<PlantopiaQuagmireConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        PlantopiaMultiNoiseConfig.CODEC.fieldOf("noise_config").forGetter(PlantopiaQuagmireConfiguration::noiseConfig),
        PlantopiaThresholdType.CODEC.fieldOf("activation_type").forGetter(PlantopiaQuagmireConfiguration::activationType),
        Codec.FLOAT.fieldOf("noise_level").forGetter(PlantopiaQuagmireConfiguration::noiseLevel),
        Codec.FLOAT.fieldOf("erosion").forGetter(PlantopiaQuagmireConfiguration::erosion),
        ExtraCodecs.POSITIVE_INT.optionalFieldOf("blur_radius", 3).forGetter(PlantopiaQuagmireConfiguration::blurRadius),
        BlockPredicate.CODEC.fieldOf("predicate").forGetter(PlantopiaQuagmireConfiguration::predicate),
        Biome.LIST_CODEC.fieldOf("allowed_biomes").forGetter(PlantopiaQuagmireConfiguration::allowedBiomes)
    ).apply(instance, PlantopiaQuagmireConfiguration::new));
}
