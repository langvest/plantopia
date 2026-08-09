package by.langvest.plantopia.worldgen.feature.treedecorator;

import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.worldgen.feature.PlantopiaTreeDecoratorTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaAlterBaseLogDecorator extends TreeDecorator {
    public static final Codec<PlantopiaAlterBaseLogDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BlockStateProvider.CODEC.fieldOf("provider").forGetter(it -> it.provider)
    ).apply(instance, PlantopiaAlterBaseLogDecorator::new));

    protected final BlockStateProvider provider;

    public PlantopiaAlterBaseLogDecorator(BlockStateProvider provider) {
        this.provider = provider;
    }

    @Override
    protected @NotNull TreeDecoratorType<?> type() {
        return PlantopiaTreeDecoratorTypes.ALTER_BASE_LOG.get();
    }

    @Override
    public void place(Context context) {
        for (var pos : context.logs()) {
            if (!context.level().isStateAtPosition(pos, state -> state.is(BlockTags.LOGS) || state.is(PlantopiaBlockTags.BALKS))) continue;
            context.setBlock(pos, provider.getState(context.random(), pos));
            break;
        }
    }
}
