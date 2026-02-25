package by.langvest.plantopia.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.Optional;

public record PlantopiaPitConfiguration(
    IntProvider xzSpread,
    IntProvider ySpread,
    IntProvider depth,
    FloatProvider curvature,
    BlockStateProvider toPlace,
    Optional<BlockPredicate> predicate,
    Optional<Heightmap.Types> heightmap
) implements FeatureConfiguration {
    public static final Codec<PlantopiaPitConfiguration> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        IntProvider.codec(0, 128).fieldOf("xz_spread").forGetter(PlantopiaPitConfiguration::xzSpread),
        IntProvider.codec(0, 128).fieldOf("y_spread").forGetter(PlantopiaPitConfiguration::ySpread),
        IntProvider.codec(1, 64).fieldOf("depth").forGetter(PlantopiaPitConfiguration::depth),
        FloatProvider.codec(0.0F, 100.0F).fieldOf("curvature").forGetter(PlantopiaPitConfiguration::curvature),
        BlockStateProvider.CODEC.fieldOf("to_place").forGetter(PlantopiaPitConfiguration::toPlace),
        BlockPredicate.CODEC.optionalFieldOf("predicate").forGetter(PlantopiaPitConfiguration::predicate),
        Heightmap.Types.CODEC.optionalFieldOf("heightmap").forGetter(PlantopiaPitConfiguration::heightmap)
    ).apply(instance, PlantopiaPitConfiguration::new));
}
