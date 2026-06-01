package by.langvest.plantopia.block.special;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaShortFlowersBlock extends FlowerBlock {
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 6.5D, 14.0D);

	public PlantopiaShortFlowersBlock(Supplier<MobEffect> effectSupplier, int effectDuration, Properties properties) {
		super(effectSupplier.get(), effectDuration, properties);
	}

	@Override
	public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		var vec3 = state.getOffset(level, pos);
		return SHAPE.move(vec3.x, vec3.y, vec3.z);
	}
}