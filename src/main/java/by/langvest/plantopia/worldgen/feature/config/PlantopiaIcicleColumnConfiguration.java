package by.langvest.plantopia.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record PlantopiaIcicleColumnConfiguration(
    IntProvider height,
    Direction direction,
    BlockPredicate allowedPlacement
) implements FeatureConfiguration {
    public static final Codec<PlantopiaIcicleColumnConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        IntProvider.codec(1, 64).fieldOf("height").forGetter(PlantopiaIcicleColumnConfiguration::height),
        Direction.VERTICAL_CODEC.fieldOf("direction").forGetter(PlantopiaIcicleColumnConfiguration::direction),
        BlockPredicate.CODEC.fieldOf("allowed_placement").forGetter(PlantopiaIcicleColumnConfiguration::allowedPlacement)
    ).apply(instance, PlantopiaIcicleColumnConfiguration::new));
}
