package by.langvest.plantopia.worldgen.feature.treedecorator;

import by.langvest.plantopia.worldgen.feature.PlantopiaTreeDecoratorTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaFruitDecorator extends TreeDecorator {
    public static final Codec<PlantopiaFruitDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.POSITIVE_FLOAT.fieldOf("tree_probability").forGetter(it -> it.treeProbability),
        ExtraCodecs.POSITIVE_FLOAT.fieldOf("fruit_probability").forGetter(it -> it.fruitProbability),
        BlockStateProvider.CODEC.fieldOf("provider").forGetter(it -> it.provider),
        Direction.VERTICAL_CODEC.fieldOf("direction").orElse(Direction.DOWN).forGetter(it -> it.direction)
    ).apply(instance, PlantopiaFruitDecorator::new));

    protected final float treeProbability;
    protected final float fruitProbability;
    protected final BlockStateProvider provider;
    protected final Direction direction;

    public PlantopiaFruitDecorator(float treeChance, float fruitChance, BlockStateProvider provider, Direction direction) {
        this.treeProbability = treeChance;
        this.fruitProbability = fruitChance;
        this.provider = provider;
        this.direction = direction;
    }

    @Override
    protected @NotNull TreeDecoratorType<?> type() {
        return PlantopiaTreeDecoratorTypes.FRUIT.get();
    }

    @Override
    public void place(Context context) {
        var random = context.random();
        if (random.nextFloat() >= treeProbability) return;

        var mutablePos = new BlockPos.MutableBlockPos();

        for (var pos : context.leaves()) {
            if (random.nextFloat() >= fruitProbability) continue;

            mutablePos.setWithOffset(pos, direction);
            if (!context.isAir(mutablePos)) continue;

            context.setBlock(mutablePos, provider.getState(random, mutablePos));
        }
    }
}
