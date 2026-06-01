package by.langvest.plantopia.worldgen.feature.config;

import by.langvest.plantopia.worldgen.placement.PlantopiaMultiNoiseConfig;
import by.langvest.plantopia.worldgen.placement.PlantopiaThresholdType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record PlantopiaFrazilConfiguration(
    PlantopiaMultiNoiseConfig noiseConfig,
    PlantopiaThresholdType activationType,
    float noiseLevel,
    BlockPredicate predicate,
    HolderSet<Biome> allowedBiomes
) implements FeatureConfiguration {
    public static final Codec<PlantopiaFrazilConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        PlantopiaMultiNoiseConfig.CODEC.fieldOf("noise_config").forGetter(PlantopiaFrazilConfiguration::noiseConfig),
        PlantopiaThresholdType.CODEC.fieldOf("activation_type").forGetter(PlantopiaFrazilConfiguration::activationType),
        Codec.FLOAT.fieldOf("noise_level").forGetter(PlantopiaFrazilConfiguration::noiseLevel),
        BlockPredicate.CODEC.fieldOf("predicate").forGetter(PlantopiaFrazilConfiguration::predicate),
        Biome.LIST_CODEC.fieldOf("allowed_biomes").forGetter(PlantopiaFrazilConfiguration::allowedBiomes)
    ).apply(instance, PlantopiaFrazilConfiguration::new));
}
