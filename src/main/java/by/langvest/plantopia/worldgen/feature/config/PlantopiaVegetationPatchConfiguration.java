package by.langvest.plantopia.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public record PlantopiaVegetationPatchConfiguration(
    BlockPredicate predicate,
    TagKey<Block> replaceable,
    BlockStateProvider groundState,
    Holder<PlacedFeature> vegetationFeature,
    CaveSurface surface,
    IntProvider depth,
    float extraBottomBlockChance,
    int verticalRange,
    float vegetationChance,
    IntProvider xzRadius,
    float extraEdgeColumnChance
) implements FeatureConfiguration {
    public static final Codec<PlantopiaVegetationPatchConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BlockPredicate.CODEC.fieldOf("predicate").forGetter(PlantopiaVegetationPatchConfiguration::predicate),
        TagKey.hashedCodec(Registries.BLOCK).fieldOf("replaceable").forGetter(PlantopiaVegetationPatchConfiguration::replaceable),
        BlockStateProvider.CODEC.fieldOf("ground_state").forGetter(PlantopiaVegetationPatchConfiguration::groundState),
        PlacedFeature.CODEC.fieldOf("vegetation_feature").forGetter(PlantopiaVegetationPatchConfiguration::vegetationFeature),
        CaveSurface.CODEC.fieldOf("surface").forGetter(PlantopiaVegetationPatchConfiguration::surface),
        IntProvider.codec(1, 128).fieldOf("depth").forGetter(PlantopiaVegetationPatchConfiguration::depth),
        Codec.floatRange(0.0F, 1.0F).fieldOf("extra_bottom_block_chance").forGetter(PlantopiaVegetationPatchConfiguration::extraBottomBlockChance),
        Codec.intRange(1, 256).fieldOf("vertical_range").forGetter(PlantopiaVegetationPatchConfiguration::verticalRange),
        Codec.floatRange(0.0F, 1.0F).fieldOf("vegetation_chance").forGetter(PlantopiaVegetationPatchConfiguration::vegetationChance),
        IntProvider.CODEC.fieldOf("xz_radius").forGetter(PlantopiaVegetationPatchConfiguration::xzRadius),
        Codec.floatRange(0.0F, 1.0F).fieldOf("extra_edge_column_chance").forGetter(PlantopiaVegetationPatchConfiguration::extraEdgeColumnChance)
    ).apply(instance, PlantopiaVegetationPatchConfiguration::new));
}
