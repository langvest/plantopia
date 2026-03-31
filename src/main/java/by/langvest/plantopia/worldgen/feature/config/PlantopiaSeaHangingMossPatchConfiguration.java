package by.langvest.plantopia.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record PlantopiaSeaHangingMossPatchConfiguration(
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
    BlockPredicate allowedPlacement
) implements FeatureConfiguration {
    public static final Codec<PlantopiaSeaHangingMossPatchConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        IntProvider.codec(0, 32).fieldOf("xz_spread").forGetter(PlantopiaSeaHangingMossPatchConfiguration::xzSpread),
        IntProvider.codec(0, 32).fieldOf("y_spread").forGetter(PlantopiaSeaHangingMossPatchConfiguration::ySpread),
        IntProvider.codec(1, 512).fieldOf("tries").forGetter(PlantopiaSeaHangingMossPatchConfiguration::tries),
        IntProvider.codec(1, 64).fieldOf("height").forGetter(PlantopiaSeaHangingMossPatchConfiguration::height),
        FloatProvider.codec(0.0F, 10.0F).fieldOf("height_falloff").forGetter(PlantopiaSeaHangingMossPatchConfiguration::heightFalloff),
        FloatProvider.codec(0.0F, 10.0F).fieldOf("height_erosion").forGetter(PlantopiaSeaHangingMossPatchConfiguration::heightErosion),
        FloatProvider.codec(-10.0F, 10.0F).fieldOf("shape_sigma").forGetter(PlantopiaSeaHangingMossPatchConfiguration::shapeSigma),
        FloatProvider.codec(0.0F, 10.0F).fieldOf("shape_erosion").forGetter(PlantopiaSeaHangingMossPatchConfiguration::shapeErosion),
        IntProvider.codec(1, 32).fieldOf("search_distance").forGetter(PlantopiaSeaHangingMossPatchConfiguration::searchDistance),
        BlockPredicate.CODEC.fieldOf("allowed_attachment").forGetter(PlantopiaSeaHangingMossPatchConfiguration::allowedAttachment),
        BlockPredicate.CODEC.fieldOf("allowed_placement").forGetter(PlantopiaSeaHangingMossPatchConfiguration::allowedPlacement)
    ).apply(instance, PlantopiaSeaHangingMossPatchConfiguration::new));
}
