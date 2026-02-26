package by.langvest.plantopia.worldgen.placement.special;

import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementModifierTypes;
import by.langvest.plantopia.worldgen.placement.PlantopiaThresholdActivationType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PlantopiaHeightmapFilter extends PlacementFilter {
    public static final Codec<PlantopiaHeightmapFilter> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(it -> it.heightmap),
        PlantopiaThresholdActivationType.CODEC.fieldOf("activation").forGetter(it -> it.activationType)
    ).apply(instance, PlantopiaHeightmapFilter::new));

    private final Heightmap.Types heightmap;
    private final PlantopiaThresholdActivationType activationType;

    private PlantopiaHeightmapFilter(Heightmap.Types heightmap, PlantopiaThresholdActivationType activationType) {
        this.heightmap = heightmap;
        this.activationType = activationType;
    }

    @Contract("_ -> new")
    public static @NotNull PlantopiaHeightmapFilter below(Heightmap.Types heightmap) {
        return new PlantopiaHeightmapFilter(heightmap, PlantopiaThresholdActivationType.BELOW);
    }

    @Contract("_ -> new")
    public static @NotNull PlantopiaHeightmapFilter above(Heightmap.Types heightmap) {
        return new PlantopiaHeightmapFilter(heightmap, PlantopiaThresholdActivationType.ABOVE);
    }

    @Override
    protected boolean shouldPlace(@NotNull PlacementContext context, @NotNull RandomSource random, @NotNull BlockPos pos) {
        int candidateY = pos.getY();
        int heightmapY = context.getHeight(heightmap, pos.getX(), pos.getZ());
        
        if (activationType == PlantopiaThresholdActivationType.BELOW) {
            return candidateY < heightmapY;
        } else {
            return candidateY >= heightmapY;
        }
    }

    @Override
    public @NotNull PlacementModifierType<?> type() {
        return PlantopiaPlacementModifierTypes.HEIGHTMAP_FILTER.get();
    }
}
