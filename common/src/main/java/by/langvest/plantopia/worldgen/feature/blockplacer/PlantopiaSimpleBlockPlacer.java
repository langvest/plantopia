package by.langvest.plantopia.worldgen.feature.blockplacer;

import by.langvest.plantopia.worldgen.feature.PlantopiaBlockPlacerType;
import by.langvest.plantopia.worldgen.feature.PlantopiaBlockPlacerTypes;
import by.langvest.plantopia.worldgen.feature.special.PlantopiaNaturalBlockFeature;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaSimpleBlockPlacer extends PlantopiaBlockPlacer {
    public static final Codec<PlantopiaSimpleBlockPlacer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BlockStateProvider.CODEC.fieldOf("provider").forGetter(it -> it.provider),
        ExtraCodecs.POSITIVE_INT.fieldOf("weight").orElse(1).forGetter(it -> it.weight)
    ).apply(instance, PlantopiaSimpleBlockPlacer::new));

    protected final BlockStateProvider provider;
    protected final int weight;

    public PlantopiaSimpleBlockPlacer(BlockStateProvider provider) {
        this.provider = provider;
        this.weight = 1;
    }

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
    public boolean place(Context context) {
        var random = context.random();
        var targetPos = context.targetPos();
        var level = context.level();
        var state = provider.getState(random, targetPos);

        return PlantopiaNaturalBlockFeature.place(level, state, targetPos, random, Block.UPDATE_CLIENTS);
    }
}
