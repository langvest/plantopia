package by.langvest.plantopia.worldgen.feature.blockplacer;

import by.langvest.plantopia.worldgen.feature.PlantopiaBlockPlacerType;
import by.langvest.plantopia.worldgen.feature.PlantopiaBlockPlacerTypes;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.jetbrains.annotations.NotNull;

public class PlantopiaSimpleBlockPlacer extends PlantopiaBlockPlacer {
    public static final Codec<PlantopiaSimpleBlockPlacer> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        BlockStateProvider.CODEC.fieldOf("provider").forGetter(target -> target.provider),
        ExtraCodecs.POSITIVE_INT.fieldOf("weight").forGetter(target -> target.weight)
    ).apply(instance, PlantopiaSimpleBlockPlacer::new));

    protected final BlockStateProvider provider;
    protected final int weight;

    public PlantopiaSimpleBlockPlacer(BlockStateProvider provider, int weight) {
        this.provider = provider;
        this.weight = weight;
    }

    @Override
    protected PlantopiaBlockPlacerType<?> type() {
        return PlantopiaBlockPlacerTypes.SIMPLE.get();
    }

    @Override
    public int weight() {
        return weight;
    }

    @Override
    public boolean place(@NotNull Context context) {
        var random = context.random();
        var targetPos = context.targetPos();
        var level = context.level();
        var state = provider.getState(random, targetPos);
        var naturalBlockFeature = PlantopiaFeatureTypes.NATURAL_BLOCK.get();

        return naturalBlockFeature.place(level, state, targetPos, random, Block.UPDATE_CLIENTS);
    }
}
