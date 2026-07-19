package by.langvest.plantopia.worldgen.placement.special;

import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementModifierTypes;
import by.langvest.plantopia.worldgen.util.verticalanchor.PlantopiaVerticalAnchor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
public class PlantopiaDensityPlacement extends PlacementModifier {
    public static final Codec<PlantopiaDensityPlacement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        FloatProvider.codec(0.0F, 16.0F).fieldOf("density").forGetter(it -> it.density),
        PlantopiaVerticalAnchor.CODEC.fieldOf("top_anchor").forGetter(it -> it.topAnchor),
        PlantopiaVerticalAnchor.CODEC.fieldOf("bottom_anchor").forGetter(it -> it.bottomAnchor)
    ).apply(instance, PlantopiaDensityPlacement::new));

    private final FloatProvider density;
    private final PlantopiaVerticalAnchor topAnchor;
    private final PlantopiaVerticalAnchor bottomAnchor;

    private PlantopiaDensityPlacement(FloatProvider density, PlantopiaVerticalAnchor topAnchor, PlantopiaVerticalAnchor bottomAnchor) {
        this.density = density;
        this.topAnchor = topAnchor;
        this.bottomAnchor = bottomAnchor;
    }

    @Contract("_, _, _ -> new")
    public static @NotNull PlantopiaDensityPlacement of(FloatProvider density, PlantopiaVerticalAnchor topAnchor, PlantopiaVerticalAnchor bottomAnchor) {
        return new PlantopiaDensityPlacement(density, topAnchor, bottomAnchor);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull PlantopiaDensityPlacement of(float density, PlantopiaVerticalAnchor topAnchor, PlantopiaVerticalAnchor bottomAnchor) {
        return new PlantopiaDensityPlacement(ConstantFloat.of(density), topAnchor, bottomAnchor);
    }

    @Override
    public @NotNull Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        OptionalInt optionalMaxY = getHeights(context, pos, topAnchor).max();
        OptionalInt optionalMinY = getHeights(context, pos, bottomAnchor).min();

        if (optionalMaxY.isEmpty() || optionalMinY.isEmpty()) {
            return Stream.empty();
        }

        int maxY = optionalMaxY.getAsInt();
        int minY = optionalMinY.getAsInt();

        if (maxY < minY) {
            return Stream.empty();
        }

        int verticalRange = maxY - minY + 1;
        int attempts = Math.round(verticalRange * density.sample(random));

        if (attempts <= 0) {
            return Stream.empty();
        }

        return IntStream.range(0, attempts).mapToObj(i -> {
            int y = minY + random.nextInt(verticalRange);
            return new BlockPos(pos.getX(), y, pos.getZ());
        });
    }

    @Override
    public @NotNull PlacementModifierType<?> type() {
        return PlantopiaPlacementModifierTypes.DENSITY.get();
    }

    private @Unmodifiable @NotNull IntStream getHeights(PlacementContext context, BlockPos chunkPos, PlantopiaVerticalAnchor anchor) {
        var level = context.getLevel();
        int x = chunkPos.getX();
        int z = chunkPos.getZ();

        return IntStream.of(
            anchor.resolveY(context, level, x, z),
            anchor.resolveY(context, level, x + 8, z + 8),
            anchor.resolveY(context, level, x + 15, z),
            anchor.resolveY(context, level, x, z + 15),
            anchor.resolveY(context, level, x + 15, z + 15)
        );
    }
}
