package by.langvest.plantopia.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record PlantopiaLargeIcicleConfiguration(
    int floorToCeilingSearchRange,
    IntProvider columnRadius,
    FloatProvider heightScale,
    float maxColumnRadiusToCaveHeightRatio,
    FloatProvider stalactiteBluntness,
    FloatProvider stalagmiteBluntness,
    FloatProvider windSpeed,
    int minRadiusForWind,
    float minBluntnessForWind,
    BlockStateProvider provider
) implements FeatureConfiguration {
    public static final Codec<PlantopiaLargeIcicleConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.intRange(1, 512).fieldOf("floor_to_ceiling_search_range").orElse(30).forGetter(PlantopiaLargeIcicleConfiguration::floorToCeilingSearchRange),
        IntProvider.codec(1, 60).fieldOf("column_radius").forGetter(PlantopiaLargeIcicleConfiguration::columnRadius),
        FloatProvider.codec(0.0F, 20.0F).fieldOf("height_scale").forGetter(PlantopiaLargeIcicleConfiguration::heightScale),
        Codec.floatRange(0.1F, 1.0F).fieldOf("max_column_radius_to_cave_height_ratio").forGetter(PlantopiaLargeIcicleConfiguration::maxColumnRadiusToCaveHeightRatio),
        FloatProvider.codec(0.1F, 10.0F).fieldOf("stalactite_bluntness").forGetter(PlantopiaLargeIcicleConfiguration::stalactiteBluntness),
        FloatProvider.codec(0.1F, 10.0F).fieldOf("stalagmite_bluntness").forGetter(PlantopiaLargeIcicleConfiguration::stalagmiteBluntness),
        FloatProvider.codec(0.0F, 2.0F).fieldOf("wind_speed").forGetter(PlantopiaLargeIcicleConfiguration::windSpeed),
        Codec.intRange(0, 100).fieldOf("min_radius_for_wind").forGetter(PlantopiaLargeIcicleConfiguration::minRadiusForWind),
        Codec.floatRange(0.0F, 5.0F).fieldOf("min_bluntness_for_wind").forGetter(PlantopiaLargeIcicleConfiguration::minBluntnessForWind),
        BlockStateProvider.CODEC.fieldOf("provider").orElse(BlockStateProvider.simple(Blocks.PACKED_ICE)).forGetter(PlantopiaLargeIcicleConfiguration::provider)
    ).apply(instance, PlantopiaLargeIcicleConfiguration::new));
}
