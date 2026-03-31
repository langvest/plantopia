package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaSeaHangingMossPatchConfiguration;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;

/**
 * @see net.minecraft.data.worldgen.features.CaveFeatures
 */
public class PlantopiaCaveFeatures extends PlantopiaFeatures {
    private static final Map<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> declarations = Maps.newHashMap();

    public static @NotNull Map<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> getDeclarations() {
        return declarations;
    }

    private static @NotNull ResourceKey<ConfiguredFeature<?, ?>> declareConfiguredFeature(String name, PlantopiaFeatureDeclaration.@NotNull Builder builder) {
        var key = createKey(name);
        declarations.put(key, builder.build());
        return key;
    }

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_SEA_HANGING_MOSS_CAVE = declareConfiguredFeature(
        compileNameFrom(patchNameOf(PlantopiaBlocks.SEA_HANGING_MOSS), CAVE),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(PlantopiaFeatureTypes.SEA_HANGING_MOSS_PATCH, context ->
                new PlantopiaSeaHangingMossPatchConfiguration(
                    UniformInt.of(5, 7), // xzSpread
                    ConstantInt.of(2), // ySpread
                    ConstantInt.of(128), // tries
                    weightedListInt(values -> values
                        .add(UniformInt.of(10, 14), 3)
                        .add(UniformInt.of(14, 18), 7)
                        .add(UniformInt.of(18, 22), 2)
                    ), // height
                    UniformFloat.of(0.38F, 0.56F), // heightFalloff
                    ConstantFloat.of(0.132F), // heightErosion
                    ConstantFloat.of(-0.562F), // shapeSigma
                    ConstantFloat.of(0.148F), // shapeErosion
                    ConstantInt.of(9), // searchDistance
                    BlockPredicate.matchesTag(PlantopiaBlockTags.SEA_HANGING_MOSS_CAN_GENERATE_ON), // allowedAttachment
                    BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.WATER, Blocks.GLOW_LICHEN) // allowedPlacement
                )
            ))
    );
}
