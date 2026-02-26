package by.langvest.plantopia.worldgen.placement.special;

import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementModifierTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PlantopiaUndergroundDensityPlacement extends PlacementModifier {
    public static final Codec<PlantopiaUndergroundDensityPlacement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        FloatProvider.codec(0.0F, 16.0F).fieldOf("density").forGetter(it -> it.density),
        VerticalAnchor.CODEC.fieldOf("bottom_anchor").forGetter(it -> it.bottomAnchor),
        Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(it -> it.heightmap)
    ).apply(instance, PlantopiaUndergroundDensityPlacement::new));

    private final FloatProvider density;
    private final VerticalAnchor bottomAnchor;
    private final Heightmap.Types heightmap;

    private PlantopiaUndergroundDensityPlacement(FloatProvider density, VerticalAnchor bottomAnchor, Heightmap.Types heightmap) {
        this.density = density;
        this.bottomAnchor = bottomAnchor;
        this.heightmap = heightmap;
    }

    @Contract("_, _, _ -> new")
    public static @NotNull PlantopiaUndergroundDensityPlacement of(FloatProvider density, VerticalAnchor bottomAnchor, Heightmap.Types heightmap) {
        return new PlantopiaUndergroundDensityPlacement(density, bottomAnchor, heightmap);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull PlantopiaUndergroundDensityPlacement of(float density, VerticalAnchor bottomAnchor, Heightmap.Types heightmap) {
        return new PlantopiaUndergroundDensityPlacement(ConstantFloat.of(density), bottomAnchor, heightmap);
    }

    @Override
    public @NotNull Stream<BlockPos> getPositions(@NotNull PlacementContext context, @NotNull RandomSource random, @NotNull BlockPos pos) {
        int minY = bottomAnchor.resolveY(context);
        int surfaceY = context.getHeight(heightmap, pos.getX() + 8, pos.getZ() + 8);

        if (surfaceY <= minY) {
            return Stream.empty();
        }

        int verticalRange = surfaceY - minY;
        int attempts = Math.round(verticalRange * density.sample(random));

        if (attempts <= 0) {
            return Stream.empty();
        }
        
        return IntStream.range(0, attempts).mapToObj(i -> {
            int y = random.nextInt(minY, surfaceY);
            return new BlockPos(pos.getX(), y, pos.getZ());
        });
    }

    @Override
    public @NotNull PlacementModifierType<?> type() {
        return PlantopiaPlacementModifierTypes.UNDERGROUND_DENSITY.get();
    }
}
