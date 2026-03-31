package by.langvest.plantopia.worldgen.placement.special;

import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementModifierTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public class PlantopiaSwitchPlacement extends PlacementModifier {
    public static final Codec<PlantopiaSwitchPlacement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        PlacementModifier.CODEC.listOf().listOf().fieldOf("placements").forGetter(it -> it.placements)
    ).apply(instance, PlantopiaSwitchPlacement::new));

    private final List<List<PlacementModifier>> placements;

    public PlantopiaSwitchPlacement(List<List<PlacementModifier>> placements) {
        this.placements = placements;
    }

    @SafeVarargs
    public static @NotNull PlantopiaSwitchPlacement switched(List<PlacementModifier>... placements) {
        return new PlantopiaSwitchPlacement(List.of(placements));
    }

    @Override
    public @NotNull Stream<BlockPos> getPositions(@NotNull PlacementContext context, @NotNull RandomSource random, @NotNull BlockPos pos) {
        for (List<PlacementModifier> placementOption : this.placements) {
            // Start with the initial position
            Stream<BlockPos> currentPositions = Stream.of(pos);

            // Sequentially apply each modifier in the current option
            for (PlacementModifier modifier : placementOption) {
                currentPositions = currentPositions.flatMap(p -> modifier.getPositions(context, random, p));
            }

            // Collect the results to check if this option was successful
            List<BlockPos> resultPositions = currentPositions.toList();

            // If we found any positions, use this option and stop searching
            if (!resultPositions.isEmpty()) {
                return resultPositions.stream();
            }
        }

        // If no option yielded any positions, return an empty stream
        return Stream.empty();
    }

    @Override
    public @NotNull PlacementModifierType<?> type() {
        return PlantopiaPlacementModifierTypes.SWITCH.get();
    }
}
