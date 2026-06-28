package by.langvest.plantopia.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record PlantopiaSeaHangingMossClusterConfiguration(
    IntProvider xzSpread,
    IntProvider ySpread,
    IntProvider searchDistance,
    IntProvider depth,
    FloatProvider curvature,
    BlockPredicate allowedBasisPlacement,
    IntProvider height,
    FloatProvider heightFalloff,
    FloatProvider heightErosion,
    FloatProvider edgeErosion,
    BlockPredicate allowedPlantPlacement
) implements FeatureConfiguration {
    public static final Codec<PlantopiaSeaHangingMossClusterConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        IntProvider.codec(0, 32).fieldOf("xz_spread").forGetter(PlantopiaSeaHangingMossClusterConfiguration::xzSpread),
        IntProvider.codec(0, 32).fieldOf("y_spread").forGetter(PlantopiaSeaHangingMossClusterConfiguration::ySpread),
        IntProvider.codec(0, 32).fieldOf("search_distance").forGetter(PlantopiaSeaHangingMossClusterConfiguration::searchDistance),
        IntProvider.codec(1, 64).fieldOf("depth").forGetter(PlantopiaSeaHangingMossClusterConfiguration::depth),
        FloatProvider.codec(0.0F, 10.0F).fieldOf("curvature").forGetter(PlantopiaSeaHangingMossClusterConfiguration::curvature),
        BlockPredicate.CODEC.fieldOf("allowed_basis_placement").forGetter(PlantopiaSeaHangingMossClusterConfiguration::allowedBasisPlacement),
        IntProvider.codec(1, 64).fieldOf("height").forGetter(PlantopiaSeaHangingMossClusterConfiguration::height),
        FloatProvider.codec(0.0F, 10.0F).fieldOf("height_falloff").forGetter(PlantopiaSeaHangingMossClusterConfiguration::heightFalloff),
        FloatProvider.codec(0.0F, 10.0F).fieldOf("height_erosion").forGetter(PlantopiaSeaHangingMossClusterConfiguration::heightErosion),
        FloatProvider.codec(0.0F, 10.0F).fieldOf("edge_erosion").forGetter(PlantopiaSeaHangingMossClusterConfiguration::edgeErosion),
        BlockPredicate.CODEC.fieldOf("allowed_plant_placement").forGetter(PlantopiaSeaHangingMossClusterConfiguration::allowedPlantPlacement)
    ).apply(instance, PlantopiaSeaHangingMossClusterConfiguration::new));
}
