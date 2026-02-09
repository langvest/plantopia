package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PlantopiaTinyCactusBlock extends BushBlock implements BonemealableBlock {
	protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 9.0D, 12.0D);

	public PlantopiaTinyCactusBlock(Properties properties) {
		super(properties);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		var offset = state.getOffset(level, pos);

		return SHAPE.move(offset.x, offset.y, offset.z);
	}

	@Override
	protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		return super.mayPlaceOn(state, level, pos) || state.is(BlockTags.SAND);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
		entity.hurt(level.damageSources().cactus(), 1.0F);
	}

	@Override
	public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull PathComputationType type) {
		return false;
	}

	@Override
	public float getMaxHorizontalOffset() {
		return super.getMaxHorizontalOffset() * 0.6F;
	}

	@Override
	public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean isClient) {
		return state.is(PlantopiaBlocks.TINY_CACTUS.get());
	}

	@Override
	public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
		return level.random.nextFloat() < 0.45F;
	}

	@Override
	public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
		level.setBlock(pos, PlantopiaBlocks.FLOWERING_TINY_CACTUS.get().defaultBlockState(), 2);
	}
}
