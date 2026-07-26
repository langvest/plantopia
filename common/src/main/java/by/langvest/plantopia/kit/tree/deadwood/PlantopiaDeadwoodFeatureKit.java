package by.langvest.plantopia.kit.tree.deadwood;

import by.langvest.plantopia.kit.special.PlantopiaKit;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.PlantopiaDictionary.FANCY;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatureUtils.*;
import static by.langvest.plantopia.worldgen.feature.PlantopiaTreeFeatureUtils.*;

@ParametersAreNonnullByDefault
public class PlantopiaDeadwoodFeatureKit extends PlantopiaKit {
    public final ResourceKey<ConfiguredFeature<?, ?>> tree;
    public final ResourceKey<ConfiguredFeature<?, ?>> fancyTree;

    public PlantopiaDeadwoodFeatureKit(
        String baseName,
        Supplier<Block> log,
        Supplier<Block> leaves
    ) {
        this.tree = PlantopiaFeatures.declareFeature(
            baseName,
            PlantopiaFeatureDeclaration.builder()
                .feature(deciduousTree(context ->
                    createSimpleTree(log.get(), leaves.get())
                        .ignoreVines()
                        .build()
                ))
        );

        this.fancyTree = PlantopiaFeatures.declareFeature(
            compileNameFrom(FANCY, baseName),
            PlantopiaFeatureDeclaration.builder()
                .feature(deciduousTree(context ->
                    createSimpleFancyTree(log.get(), leaves.get())
                        .ignoreVines()
                        .build()
                ))
        );
    }
}
