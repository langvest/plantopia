package by.langvest.plantopia.worldgen.placement.special;

import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementModifierTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
public class PlantopiaConditionPlacement extends PlacementModifier {
    public static final Codec<PlantopiaConditionPlacement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PlacementModifier.CODEC.listOf().fieldOf("if_placement").forGetter(it -> it.ifPlacement),
            PlacementModifier.CODEC.listOf().fieldOf("then_placement").forGetter(it -> it.thenPlacement),
            PlacementModifier.CODEC.listOf().optionalFieldOf("else_placement", List.of()).forGetter(it -> it.elsePlacement)
    ).apply(instance, PlantopiaConditionPlacement::new));

    private final List<PlacementModifier> ifPlacement;
    private final List<PlacementModifier> thenPlacement;
    private final List<PlacementModifier> elsePlacement;

    public PlantopiaConditionPlacement(List<PlacementModifier> ifPlacement, List<PlacementModifier> thenPlacement, List<PlacementModifier> elsePlacement) {
        this.ifPlacement = ifPlacement;
        this.thenPlacement = thenPlacement;
        this.elsePlacement = elsePlacement;
    }

    @Contract("_, _ -> new")
    public static @NotNull PlantopiaConditionPlacement conditional(List<PlacementModifier> ifPlacement, List<PlacementModifier> thenPlacement) {
        return new PlantopiaConditionPlacement(ifPlacement, thenPlacement, List.of());
    }

    @Contract("_, _, _ -> new")
    public static @NotNull PlantopiaConditionPlacement conditional(List<PlacementModifier> ifPlacement, List<PlacementModifier> thenPlacement, List<PlacementModifier> elsePlacement) {
        return new PlantopiaConditionPlacement(ifPlacement, thenPlacement, elsePlacement);
    }

    @Override
    public @NotNull Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        List<BlockPos> ifResult = resolvePlacement(ifPlacement, context, random, pos).toList();

        if (!ifResult.isEmpty()) {
            return ifResult.stream().flatMap(p -> resolvePlacement(thenPlacement, context, random, p));
        } else {
            return resolvePlacement(elsePlacement, context, random, pos);
        }
    }

    private Stream<BlockPos> resolvePlacement(List<PlacementModifier> placements, PlacementContext context, RandomSource random, BlockPos pos) {
        Stream<BlockPos> currentPositions = Stream.of(pos);
        for (PlacementModifier modifier : placements) {
            currentPositions = currentPositions.flatMap(p -> modifier.getPositions(context, random, p));
        }
        return currentPositions;
    }

    @Override
    public @NotNull PlacementModifierType<?> type() {
        return PlantopiaPlacementModifierTypes.CONDITION.get();
    }
}
