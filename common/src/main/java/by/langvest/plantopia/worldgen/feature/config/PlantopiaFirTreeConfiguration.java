package by.langvest.plantopia.worldgen.feature.config;

import by.langvest.plantopia.worldgen.util.intproportion.PlantopiaIntProportion;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

import java.util.List;

public record PlantopiaFirTreeConfiguration(
    BlockStateProvider trunkProvider,
    IntProvider trunkHeight,
    IntProvider trunkWidth,
    BlockStateProvider foliageProvider,
    PlantopiaIntProportion foliageHeight,
    IntProvider foliageOffset,
    List<TreeDecorator> decorators
) implements FeatureConfiguration {
    public static final Codec<PlantopiaFirTreeConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(PlantopiaFirTreeConfiguration::trunkProvider),
        IntProvider.CODEC.fieldOf("trunk_height").forGetter(PlantopiaFirTreeConfiguration::trunkHeight),
        IntProvider.CODEC.fieldOf("trunk_width").forGetter(PlantopiaFirTreeConfiguration::trunkWidth),
        BlockStateProvider.CODEC.fieldOf("foliage_provider").forGetter(PlantopiaFirTreeConfiguration::foliageProvider),
        PlantopiaIntProportion.CODEC.fieldOf("foliage_height").forGetter(PlantopiaFirTreeConfiguration::foliageHeight),
        IntProvider.CODEC.fieldOf("foliage_offset").forGetter(PlantopiaFirTreeConfiguration::foliageOffset),
        TreeDecorator.CODEC.listOf().fieldOf("decorators").forGetter(PlantopiaFirTreeConfiguration::decorators)
    ).apply(instance, PlantopiaFirTreeConfiguration::new));
}
