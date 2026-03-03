package by.langvest.plantopia.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record PlantopiaBranchingShrubPatchConfiguration(
    IntProvider xzSpread,
    IntProvider ySpread,
    IntProvider tries,
    IntProvider height,
    FloatProvider heightFalloff,
    FloatProvider heightErosion,
    FloatProvider shapeSigma,
    FloatProvider shapeErosion,
    IntProvider searchDistance
) implements FeatureConfiguration {
    public static final Codec<PlantopiaBranchingShrubPatchConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        IntProvider.codec(0, 128).fieldOf("xz_spread").forGetter(PlantopiaBranchingShrubPatchConfiguration::xzSpread),
        IntProvider.codec(0, 128).fieldOf("y_spread").forGetter(PlantopiaBranchingShrubPatchConfiguration::ySpread),
        IntProvider.codec(1, 512).fieldOf("tries").forGetter(PlantopiaBranchingShrubPatchConfiguration::tries),
        IntProvider.codec(1, 64).fieldOf("height").forGetter(PlantopiaBranchingShrubPatchConfiguration::height),
        FloatProvider.codec(0.0F, 10.0F).fieldOf("height_falloff").forGetter(PlantopiaBranchingShrubPatchConfiguration::heightFalloff),
        FloatProvider.codec(0.0F, 10.0F).fieldOf("height_erosion").forGetter(PlantopiaBranchingShrubPatchConfiguration::heightErosion),
        FloatProvider.codec(-10.0F, 10.0F).fieldOf("shape_sigma").forGetter(PlantopiaBranchingShrubPatchConfiguration::shapeSigma),
        FloatProvider.codec(0.0F, 10.0F).fieldOf("shape_erosion").forGetter(PlantopiaBranchingShrubPatchConfiguration::shapeErosion),
        IntProvider.codec(1, 32).fieldOf("search_distance").forGetter(PlantopiaBranchingShrubPatchConfiguration::searchDistance)
    ).apply(instance, PlantopiaBranchingShrubPatchConfiguration::new));
}
