package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import by.langvest.plantopia.block.PlantopiaNaturalBlock;
import by.langvest.plantopia.util.helper.PlantopiaMathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;

public class PlantopiaBranchingShrubBlock extends Block implements SimpleWaterloggedBlock, PlantopiaNaturalBlock {
	protected static final VoxelShape COLLISION_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty BASE = PlantopiaBlockStateProperties.BASE;

	public PlantopiaBranchingShrubBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(WATERLOGGED, false).setValue(BASE, true));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		var level = context.getLevel();
		var pos = context.getClickedPos();
		var posBelow = pos.below();
		var stateBelow = level.getBlockState(posBelow);

		var newState = defaultBlockState().setValue(BASE, !stateBelow.is(this));

		return copyWaterloggedFrom(level, pos, newState);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		return Shapes.block();
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		Vec3 vec3_1 = getCollisionOffset(pos.getX(), pos.getZ());
		Vec3 vec3_2 = getCollisionOffset(pos.getZ(), pos.getX());

		VoxelShape shape = Shapes.or(
			COLLISION_SHAPE.move(vec3_1.x, vec3_1.y, vec3_1.z),
			COLLISION_SHAPE.move(vec3_2.x, vec3_2.y, vec3_2.z)
		);

		if(context instanceof EntityCollisionContext entityCollisionContext) {
			Entity entity = entityCollisionContext.getEntity();

			if(entity == null) return shape;
			if(!(entity instanceof LivingEntity)) return shape;

			if(entity.position().y >= pos.getY() + 0.5D) {
				return Shapes.empty();
			}
		}

		return shape;
	}

	@Override
	public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		return state.getFluidState().isEmpty();
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull PathComputationType type) {
		return switch(type) {
			case LAND -> true;
			case AIR -> false;
			default -> super.isPathfindable(state, level, pos, type);
		};
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isCollisionShapeFullBlock(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		return false;
	}

	protected float getMaxHorizontalCollisionOffset() {
		return 0.4375f; // (7.0F / 16.0F)
	}

	protected Vec3 getCollisionOffset(int x, int z) {
		long seed = PlantopiaMathHelper.getSeed(x, 0, z);
		float maxHorizontalOffset = getMaxHorizontalCollisionOffset();
		return PlantopiaMathHelper.getXZOffset(seed, maxHorizontalOffset);
	}

	protected boolean isValidEnvironment(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		var isBase = state.getValue(BASE);
		BlockPos posBelow = pos.below();
		BlockState stateBelow = level.getBlockState(posBelow);

		if(isBase) {
			return mayPlaceOn(stateBelow, level, posBelow);
		}

		return stateBelow.is(this);
	}

	protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		return state.is(Blocks.CLAY) || state.is(BlockTags.DEAD_BUSH_MAY_PLACE_ON);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		return isValidEnvironment(state, level, pos);
	}

	@Override
	@SuppressWarnings("deprecation")
	public float getDestroyProgress(@NotNull BlockState state, @NotNull Player player, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		ItemStack mainHandItem = player.getMainHandItem();

		if(mainHandItem.canPerformAction(ToolActions.SWORD_DIG) || mainHandItem.canPerformAction(ToolActions.SHEARS_DIG)) {
			return 1.0F;
		}

		return super.getDestroyProgress(state, player, level, pos);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
		if(!state.canSurvive(level, pos)) {
			level.destroyBlock(pos, true);
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos facingPos) {
		if(!state.canSurvive(level, pos)) level.scheduleTick(pos, this, 1);
		if(state.getValue(WATERLOGGED)) level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		return super.updateShape(state, facing, facingState, level, pos, facingPos);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull FluidState getFluidState(@NotNull BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, BASE);
	}

	@Override
	public boolean generateAt(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull RandomSource random, int flags) {
		int height = 1 + random.nextIntBetweenInclusive(0, 2);

		if(random.nextDouble() < 0.35D) {
			height += random.nextIntBetweenInclusive(0, 1);
		}

		int successfulTries = 0;

		for(int i = 0; i < height; i++) {
			var candidatePos = pos.above(i);
			var targetState = level.getBlockState(candidatePos);

			if(!targetState.canBeReplaced()) break;

			var newState = copyWaterloggedFrom(level, candidatePos, state.setValue(BASE, i == 0));

			if(!newState.canSurvive(level, candidatePos)) break;

			level.setBlock(candidatePos, newState, flags);
			successfulTries++;
		}

		return successfulTries > 0;
	}
}
