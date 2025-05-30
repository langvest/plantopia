package by.langvest.plantopia.worldgen.feature.treedecorator;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.worldgen.feature.PlantopiaTreeDecoratorTypes;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;

public class PlantopiaBirchBaseBlockDecorator extends TreeDecorator {
	public static final Codec<PlantopiaBirchBaseBlockDecorator> CODEC = Codec.unit(() -> PlantopiaBirchBaseBlockDecorator.INSTANCE);
	public static final PlantopiaBirchBaseBlockDecorator INSTANCE = new PlantopiaBirchBaseBlockDecorator();

	@Override
	protected @NotNull TreeDecoratorType<?> type() {
		return PlantopiaTreeDecoratorTypes.BIRCH_BASE_BLOCK.get();
	}

	@Override
	public void place(@NotNull Context context) {
		for(BlockPos blockPos : context.logs()) {
			if(!context.level().isStateAtPosition(blockPos, blockState -> blockState.is(Blocks.BIRCH_LOG))) continue;
			context.setBlock(blockPos, PlantopiaBlocks.BIRCH_BASE_LOG.get().defaultBlockState());
			break;
		}
	}
}
