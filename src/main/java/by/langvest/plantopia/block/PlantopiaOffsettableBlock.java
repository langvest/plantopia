package by.langvest.plantopia.block;

import net.minecraft.world.level.block.state.BlockBehaviour.OffsetFunction;

import java.util.Optional;

public interface PlantopiaOffsettableBlock {
	default Optional<OffsetFunction> getOffsetFunction(Optional<OffsetFunction> defaultOffsetFunction) {
		return defaultOffsetFunction;
	}
}
