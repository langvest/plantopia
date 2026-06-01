package by.langvest.plantopia.worldgen.feature.config;

import by.langvest.plantopia.worldgen.feature.blockplacer.PlantopiaBlockPlacer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;
import java.util.Optional;

public record PlantopiaRadialPatchConfiguration(
    IntProvider tries,
    IntProvider xzSpread,
    IntProvider ySpread,
    double sigma,
    double erosion,
    List<PlantopiaBlockPlacer> blocks,
    Optional<BlockPredicate> predicate,
    Optional<Heightmap.Types> heightmap
) implements FeatureConfiguration {
    public static final Codec<PlantopiaRadialPatchConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        IntProvider.codec(0, 512).fieldOf("tries").forGetter(PlantopiaRadialPatchConfiguration::tries),
        IntProvider.codec(0, 128).fieldOf("xz_spread").forGetter(PlantopiaRadialPatchConfiguration::xzSpread),
        IntProvider.codec(0, 128).fieldOf("y_spread").forGetter(PlantopiaRadialPatchConfiguration::ySpread),
        Codec.DOUBLE.fieldOf("sigma").forGetter(PlantopiaRadialPatchConfiguration::sigma),
        Codec.DOUBLE.fieldOf("erosion").forGetter(PlantopiaRadialPatchConfiguration::erosion),
        Codec.list(PlantopiaBlockPlacer.CODEC).fieldOf("blocks").forGetter(PlantopiaRadialPatchConfiguration::blocks),
        BlockPredicate.CODEC.optionalFieldOf("predicate").forGetter(PlantopiaRadialPatchConfiguration::predicate),
        Heightmap.Types.CODEC.optionalFieldOf("heightmap").forGetter(PlantopiaRadialPatchConfiguration::heightmap)
    ).apply(instance, PlantopiaRadialPatchConfiguration::new));
}
