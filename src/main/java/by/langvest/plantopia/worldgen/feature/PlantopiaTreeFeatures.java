package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.block.PlantopiaBlocks;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;

/**
 * @see net.minecraft.data.worldgen.features.TreeFeatures
 */
public class PlantopiaTreeFeatures extends PlantopiaFeatures {
    private static final Map<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> declarations = Maps.newHashMap();

    public static @NotNull Map<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> getDeclarations() {
        return declarations;
    }

    private static @NotNull ResourceKey<ConfiguredFeature<?, ?>> declareConfiguredFeature(String name, PlantopiaFeatureDeclaration.@NotNull Builder builder) {
        var key = createKey(name);
        declarations.put(key, builder.build());
        return key;
    }

    public static final ResourceKey<ConfiguredFeature<?, ?>> HUGE_WITCHY_TOADSTOOL = declareConfiguredFeature(
        compileNameFrom(HUGE, PlantopiaBlocks.WITCHY_TOADSTOOL),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(PlantopiaFeatureTypes.HUGE_WITCHY_TOADSTOOL, context ->
                new HugeMushroomFeatureConfiguration(
                    BlockStateProvider.simple(
                        PlantopiaBlocks.WITCHY_TOADSTOOL_BLOCK.get().defaultBlockState()
                            .setValue(HugeMushroomBlock.DOWN, false)
                    ),
                    BlockStateProvider.simple(
                        Blocks.MUSHROOM_STEM.defaultBlockState()
                            .setValue(HugeMushroomBlock.UP, false)
                            .setValue(HugeMushroomBlock.DOWN, false)
                    ),
                    1
                )
            ))
    );
}
