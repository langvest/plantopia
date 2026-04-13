package by.langvest.plantopia.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;
import java.util.Optional;

public record PlantopiaIciclePatchConfiguration(
    IntProvider xzSpread,
    IntProvider ySpread,
    IntProvider tries,
    IntProvider height,
    FloatProvider heightFalloff,
    FloatProvider heightErosion,
    FloatProvider shapeSigma,
    FloatProvider shapeErosion,
    IntProvider searchDistance,
    BlockPredicate allowedAttachment,
    BlockPredicate allowedPlacement,
    List<Direction> growthDirections,
    Optional<Heightmap.Types> heightmap
) implements FeatureConfiguration {
    public static final Codec<PlantopiaIciclePatchConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        IntProvider.codec(0, 32).fieldOf("xz_spread").forGetter(PlantopiaIciclePatchConfiguration::xzSpread),
        IntProvider.codec(0, 32).fieldOf("y_spread").forGetter(PlantopiaIciclePatchConfiguration::ySpread),
        IntProvider.codec(1, 512).fieldOf("tries").forGetter(PlantopiaIciclePatchConfiguration::tries),
        IntProvider.codec(1, 64).fieldOf("height").forGetter(PlantopiaIciclePatchConfiguration::height),
        FloatProvider.codec(0.0F, 10.0F).fieldOf("height_falloff").forGetter(PlantopiaIciclePatchConfiguration::heightFalloff),
        FloatProvider.codec(0.0F, 10.0F).fieldOf("height_erosion").forGetter(PlantopiaIciclePatchConfiguration::heightErosion),
        FloatProvider.codec(-10.0F, 10.0F).fieldOf("shape_sigma").forGetter(PlantopiaIciclePatchConfiguration::shapeSigma),
        FloatProvider.codec(0.0F, 10.0F).fieldOf("shape_erosion").forGetter(PlantopiaIciclePatchConfiguration::shapeErosion),
        IntProvider.codec(1, 32).fieldOf("search_distance").forGetter(PlantopiaIciclePatchConfiguration::searchDistance),
        BlockPredicate.CODEC.fieldOf("allowed_attachment").forGetter(PlantopiaIciclePatchConfiguration::allowedAttachment),
        BlockPredicate.CODEC.fieldOf("allowed_placement").forGetter(PlantopiaIciclePatchConfiguration::allowedPlacement),
        Direction.VERTICAL_CODEC.listOf().fieldOf("growth_directions").forGetter(PlantopiaIciclePatchConfiguration::growthDirections),
        Heightmap.Types.CODEC.optionalFieldOf("heightmap").forGetter(PlantopiaIciclePatchConfiguration::heightmap)
    ).apply(instance, PlantopiaIciclePatchConfiguration::new));
}
