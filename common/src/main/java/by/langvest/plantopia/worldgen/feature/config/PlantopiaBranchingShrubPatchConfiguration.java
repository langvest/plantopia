package by.langvest.plantopia.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public record PlantopiaBranchingShrubPatchConfiguration(
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
    BlockPredicate allowedVerticalPlacement,
    BlockPredicate allowedHorizontalPlacement,
    List<Direction> growthDirections
) implements FeatureConfiguration {
    public static final Codec<PlantopiaBranchingShrubPatchConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        IntProvider.codec(0, 32).fieldOf("xz_spread").forGetter(PlantopiaBranchingShrubPatchConfiguration::xzSpread),
        IntProvider.codec(0, 32).fieldOf("y_spread").forGetter(PlantopiaBranchingShrubPatchConfiguration::ySpread),
        IntProvider.codec(1, 512).fieldOf("tries").forGetter(PlantopiaBranchingShrubPatchConfiguration::tries),
        IntProvider.codec(1, 64).fieldOf("height").forGetter(PlantopiaBranchingShrubPatchConfiguration::height),
        FloatProvider.codec(0.0F, 10.0F).fieldOf("height_falloff").forGetter(PlantopiaBranchingShrubPatchConfiguration::heightFalloff),
        FloatProvider.codec(0.0F, 10.0F).fieldOf("height_erosion").forGetter(PlantopiaBranchingShrubPatchConfiguration::heightErosion),
        FloatProvider.codec(-10.0F, 10.0F).fieldOf("shape_sigma").forGetter(PlantopiaBranchingShrubPatchConfiguration::shapeSigma),
        FloatProvider.codec(0.0F, 10.0F).fieldOf("shape_erosion").forGetter(PlantopiaBranchingShrubPatchConfiguration::shapeErosion),
        IntProvider.codec(1, 32).fieldOf("search_distance").forGetter(PlantopiaBranchingShrubPatchConfiguration::searchDistance),
        BlockPredicate.CODEC.fieldOf("allowed_attachment").forGetter(PlantopiaBranchingShrubPatchConfiguration::allowedAttachment),
        BlockPredicate.CODEC.fieldOf("allowed_vertical_placement").forGetter(PlantopiaBranchingShrubPatchConfiguration::allowedVerticalPlacement),
        BlockPredicate.CODEC.fieldOf("allowed_horizontal_placement").forGetter(PlantopiaBranchingShrubPatchConfiguration::allowedHorizontalPlacement),
        Direction.CODEC.listOf().fieldOf("growth_directions").forGetter(PlantopiaBranchingShrubPatchConfiguration::growthDirections)
    ).apply(instance, PlantopiaBranchingShrubPatchConfiguration::new));
}
