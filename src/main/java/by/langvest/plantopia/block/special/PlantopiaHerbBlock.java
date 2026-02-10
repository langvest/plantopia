package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaPollinableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.NotNull;

public class PlantopiaHerbBlock extends DoublePlantBlock implements PlantopiaPollinableBlock {
	public PlantopiaHerbBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isValidPollinationTarget(Level level, @NotNull BlockState state, BlockPos pos, LivingEntity entity) {
		return state.getValue(HALF) == DoubleBlockHalf.UPPER;
	}
}