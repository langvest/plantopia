package by.langvest.plantopia.worldgen.feature.config;

import by.langvest.plantopia.worldgen.feature.PlantopiaProportionConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record PlantopiaCliffConfiguration(
    BlockStateProvider provider,
    PlantopiaProportionConfig depth,
    int smoothness,
    BlockPredicate predicate,
    HolderSet<Biome> allowedBiomes
) implements FeatureConfiguration {
    public static final Codec<PlantopiaCliffConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BlockStateProvider.CODEC.fieldOf("provider").forGetter(PlantopiaCliffConfiguration::provider),
        PlantopiaProportionConfig.CODEC.fieldOf("depth").forGetter(PlantopiaCliffConfiguration::depth),
        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("smoothness").forGetter(PlantopiaCliffConfiguration::smoothness),
        BlockPredicate.CODEC.fieldOf("predicate").forGetter(PlantopiaCliffConfiguration::predicate),
        Biome.LIST_CODEC.fieldOf("allowed_biomes").forGetter(PlantopiaCliffConfiguration::allowedBiomes)
    ).apply(instance, PlantopiaCliffConfiguration::new));
}
