package by.langvest.plantopia.block.special;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class PlantopiaCloverBlossomBlock extends FlowerBlock {
	protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 13.0D, 12.0D);

	public PlantopiaCloverBlossomBlock(Supplier<MobEffect> effectSupplier, int effectDuration, Properties properties) {
		super(effectSupplier, effectDuration, properties);
	}

	@Override
	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		Vec3 vec3 = state.getOffset(level, pos);
		return SHAPE.move(vec3.x, 0, vec3.z);
	}

	@Override
	public float getMaxVerticalOffset() {
		return 0.01F;
	}
}