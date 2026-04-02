package by.langvest.plantopia.worldgen.feature.config;

import by.langvest.plantopia.worldgen.placement.PlantopiaDipType;
import by.langvest.plantopia.worldgen.placement.PlantopiaThresholdType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;
import java.util.Optional;

public record PlantopiaPoiAnchorConfiguration(
    Holder<ConfiguredFeature<?, ?>> feature,
    PlantopiaDipType dipType,
    VerticalAnchor bottomAnchor,
    VerticalAnchor topAnchor,
    IntProvider originAvoidDistance,
    List<PlantopiaThresholdType> allowedThresholds,
    Optional<BlockPredicate> predicate,
    Optional<Heightmap.Types> heightmap
) implements FeatureConfiguration {
    public static final Codec<PlantopiaPoiAnchorConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ConfiguredFeature.CODEC.fieldOf("feature").forGetter(PlantopiaPoiAnchorConfiguration::feature),
        PlantopiaDipType.CODEC.fieldOf("dip_type").forGetter(PlantopiaPoiAnchorConfiguration::dipType),
        VerticalAnchor.CODEC.fieldOf("bottom_anchor").forGetter(PlantopiaPoiAnchorConfiguration::bottomAnchor),
        VerticalAnchor.CODEC.fieldOf("top_anchor").forGetter(PlantopiaPoiAnchorConfiguration::topAnchor),
        IntProvider.CODEC.fieldOf("origin_avoid_distance").forGetter(PlantopiaPoiAnchorConfiguration::originAvoidDistance),
        PlantopiaThresholdType.CODEC.listOf().fieldOf("allowed_thresholds").forGetter(PlantopiaPoiAnchorConfiguration::allowedThresholds),
        BlockPredicate.CODEC.optionalFieldOf("predicate").forGetter(PlantopiaPoiAnchorConfiguration::predicate),
        Heightmap.Types.CODEC.optionalFieldOf("heightmap").forGetter(PlantopiaPoiAnchorConfiguration::heightmap)
    ).apply(instance, PlantopiaPoiAnchorConfiguration::new));
}
