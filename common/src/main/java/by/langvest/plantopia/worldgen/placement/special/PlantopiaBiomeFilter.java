package by.langvest.plantopia.worldgen.placement.special;

import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementModifierTypes;
import by.langvest.plantopia.worldgen.util.PlantopiaSelectionType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaBiomeFilter extends PlacementFilter {
    public static final Codec<PlantopiaBiomeFilter> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Biome.LIST_CODEC.fieldOf("biomes").forGetter(it -> it.biomes),
        PlantopiaSelectionType.CODEC.fieldOf("selection").forGetter(it -> it.selectionType)
    ).apply(instance, PlantopiaBiomeFilter::new));

    private final HolderSet<Biome> biomes;
    private final PlantopiaSelectionType selectionType;

    private PlantopiaBiomeFilter(HolderSet<Biome> biomes, PlantopiaSelectionType selectionType) {
        this.biomes = biomes;
        this.selectionType = selectionType;
    }

    @Contract("_ -> new")
    public static PlantopiaBiomeFilter include(HolderSet<Biome> biomes) {
        return new PlantopiaBiomeFilter(biomes, PlantopiaSelectionType.INCLUDE);
    }

    @Contract("_ -> new")
    public static PlantopiaBiomeFilter exclude(HolderSet<Biome> biomes) {
        return new PlantopiaBiomeFilter(biomes, PlantopiaSelectionType.EXCLUDE);
    }

    @Override
    protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        Holder<Biome> biome = context.getLevel().getBiome(pos);
        boolean containsBiome = biomes.contains(biome);

        if (selectionType == PlantopiaSelectionType.INCLUDE) {
            return containsBiome;
        } else {
            return !containsBiome;
        }
    }

    @Override
    public @NotNull PlacementModifierType<?> type() {
        return PlantopiaPlacementModifierTypes.BIOME_FILTER.get();
    }
}
