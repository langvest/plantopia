package by.langvest.plantopia.worldgen.feature.treedecorator;

import by.langvest.plantopia.worldgen.feature.PlantopiaTreeDecoratorTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaFruitDecorator extends TreeDecorator {
    public static final Codec<PlantopiaFruitDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BlockStateProvider.CODEC.fieldOf("provider").forGetter(it -> it.provider),
        IntProvider.CODEC.fieldOf("tree_chance").forGetter(it -> it.treeChance),
        IntProvider.CODEC.fieldOf("fruit_chance").forGetter(it -> it.fruitChance)
    ).apply(instance, PlantopiaFruitDecorator::new));

    protected final BlockStateProvider provider;
    protected final IntProvider treeChance;
    protected final IntProvider fruitChance;

    public PlantopiaFruitDecorator(BlockStateProvider provider, IntProvider treeChance, IntProvider fruitChance) {
        this.provider = provider;
        this.treeChance = treeChance;
        this.fruitChance = fruitChance;
    }

    @Override
    protected @NotNull TreeDecoratorType<?> type() {
        return PlantopiaTreeDecoratorTypes.FRUIT.get();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean onAverageOnceEvery(IntProvider chance, RandomSource random) {
        return random.nextFloat() < (1.0F / chance.sample(random));
    }

    @Override
    public void place(Context context) {
        var random = context.random();
        if (!onAverageOnceEvery(treeChance, random)) return;

        var mutablePos = BlockPos.ZERO.mutable();
        for (var pos : context.leaves()) {
            if (!onAverageOnceEvery(fruitChance, random)) continue;

            mutablePos.set(pos.getX(), pos.getY() - 1, pos.getZ());
            if (!context.isAir(mutablePos)) continue;

            context.setBlock(mutablePos, provider.getState(random, mutablePos));
        }
    }
}
