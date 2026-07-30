package by.langvest.plantopia.worldgen.feature.config;

import by.langvest.plantopia.worldgen.util.intproportion.PlantopiaIntProportion;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

import java.util.List;

public record PlantopiaMegaFirTreeConfiguration(
    BlockStateProvider trunkProvider,
    BlockStateProvider foliageProvider,
    IntProvider treeHeight,
    PlantopiaIntProportion trunkHeight,
    IntProvider trunkWidth,
    List<TreeDecorator> decorators
) implements FeatureConfiguration {
    public static final Codec<PlantopiaMegaFirTreeConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(PlantopiaMegaFirTreeConfiguration::trunkProvider),
        BlockStateProvider.CODEC.fieldOf("foliage_provider").forGetter(PlantopiaMegaFirTreeConfiguration::foliageProvider),
        IntProvider.CODEC.fieldOf("tree_height").forGetter(PlantopiaMegaFirTreeConfiguration::treeHeight),
        PlantopiaIntProportion.CODEC.fieldOf("trunk_height").forGetter(PlantopiaMegaFirTreeConfiguration::trunkHeight),
        IntProvider.CODEC.fieldOf("trunk_width").forGetter(PlantopiaMegaFirTreeConfiguration::trunkWidth),
        TreeDecorator.CODEC.listOf().fieldOf("decorators").forGetter(PlantopiaMegaFirTreeConfiguration::decorators)
    ).apply(instance, PlantopiaMegaFirTreeConfiguration::new));
}
