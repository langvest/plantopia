package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaOffsettableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class PlantopiaWaterlilyFlowerBlock extends FlowerBlock implements PlantopiaOffsettableBlock {
	protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 6.0D, 12.0D);

	public PlantopiaWaterlilyFlowerBlock(Supplier<MobEffect> effectSupplier, int effectDuration, Properties properties) {
		super(effectSupplier, effectDuration, properties);
	}

	@Override
	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		Vec3 vec3 = state.getOffset(level, pos);
		return SHAPE.move(vec3.x, 0, vec3.z);
	}

	@Override
	protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		return state.is(Blocks.FARMLAND)
			|| state.is(Blocks.BAMBOO)
			|| state.isFaceSturdy(level, pos, Direction.UP, SupportType.CENTER)
			|| (state.is(BlockTags.LEAVES) && state.isCollisionShapeFullBlock(level, pos));
	}

	@Override
	public Optional<OffsetFunction> getOffsetFunction(Optional<OffsetFunction> defaultOffsetFunction) {
		OffsetFunction offsetFunction = (state, level, pos) -> {
			var posBelow = pos.below();
			var stateBelow = level.getBlockState(posBelow);

			if(stateBelow.is(Blocks.BAMBOO)) {
				return stateBelow.getOffset(level, posBelow);
			}

			return Vec3.ZERO;
		};

		return Optional.of(offsetFunction);
	}
}
