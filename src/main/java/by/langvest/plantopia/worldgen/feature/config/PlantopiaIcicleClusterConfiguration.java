package by.langvest.plantopia.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record PlantopiaIcicleClusterConfiguration(
    int floorToCeilingSearchRange,
    IntProvider height,
    IntProvider radius,
    int maxStalagmiteStalactiteHeightDiff,
    int heightDeviation,
    IntProvider iceBlockLayerThickness,
    FloatProvider density,
    float chanceOfIcicleColumnAtMaxDistanceFromCenter,
    int maxDistanceFromEdgeAffectingChanceOfIcicleColumn,
    int maxDistanceFromCenterAffectingHeightBias
) implements FeatureConfiguration {
    public static final Codec<PlantopiaIcicleClusterConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.intRange(1, 512).fieldOf("floor_to_ceiling_search_range").forGetter(PlantopiaIcicleClusterConfiguration::floorToCeilingSearchRange),
        IntProvider.codec(1, 128).fieldOf("height").forGetter(PlantopiaIcicleClusterConfiguration::height),
        IntProvider.codec(1, 128).fieldOf("radius").forGetter(PlantopiaIcicleClusterConfiguration::radius),
        Codec.intRange(0, 64).fieldOf("max_stalagmite_stalactite_height_diff").forGetter(PlantopiaIcicleClusterConfiguration::maxStalagmiteStalactiteHeightDiff),
        Codec.intRange(1, 64).fieldOf("height_deviation").forGetter(PlantopiaIcicleClusterConfiguration::heightDeviation),
        IntProvider.codec(0, 128).fieldOf("ice_block_layer_thickness").forGetter(PlantopiaIcicleClusterConfiguration::iceBlockLayerThickness),
        FloatProvider.codec(0.0F, 2.0F).fieldOf("density").forGetter(PlantopiaIcicleClusterConfiguration::density),
        Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_icicle_column_at_max_distance_from_center").forGetter(PlantopiaIcicleClusterConfiguration::chanceOfIcicleColumnAtMaxDistanceFromCenter),
        Codec.intRange(1, 64).fieldOf("max_distance_from_edge_affecting_chance_of_icicle_column").forGetter(PlantopiaIcicleClusterConfiguration::maxDistanceFromEdgeAffectingChanceOfIcicleColumn),
        Codec.intRange(1, 64).fieldOf("max_distance_from_center_affecting_height_bias").forGetter(PlantopiaIcicleClusterConfiguration::maxDistanceFromCenterAffectingHeightBias)
    ).apply(instance, PlantopiaIcicleClusterConfiguration::new));
}
