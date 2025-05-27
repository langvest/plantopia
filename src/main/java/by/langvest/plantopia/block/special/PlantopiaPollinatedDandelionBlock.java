package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaPollinableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PlantopiaPollinatedDandelionBlock extends BushBlock implements PlantopiaPollinableBlock {
	protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);
	public static final IntegerProperty COUNT = PlantopiaBlockStateProperties.POLLINATION_COUNT;
	public static final int MIN_POLLINATES = 1;
	public static final int MAX_POLLINATES = 4;

	public PlantopiaPollinatedDandelionBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(COUNT, MIN_POLLINATES));
	}

	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
		super.randomTick(state, level, pos, random);
		int pollinationCount = state.getValue(COUNT);
		if(pollinationCount != MAX_POLLINATES) return;
		if(!level.isAreaLoaded(pos, 1)) return;
		if(level.getRawBrightness(pos, 0) < 9) return;
		level.setBlock(pos, PlantopiaBlocks.FLUFFY_DANDELION.get().defaultBlockState(), 3);
	}

	@Override
	public boolean isRandomlyTicking(@NotNull BlockState state) {
		return state.getValue(COUNT) == MAX_POLLINATES;
	}

	@Override
	public @NotNull Item asItem() {
		return Items.DANDELION;
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		Vec3 vec3 = state.getOffset(level, pos);
		return SHAPE.move(vec3.x, vec3.y, vec3.z);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(COUNT);
	}

	@Override
	public boolean isValidPollinationTarget(Level level, @NotNull BlockState state, BlockPos pos, LivingEntity entity) {
		return state.getValue(COUNT) < MAX_POLLINATES;
	}

	@Override
	public void onPollinationStop(Level level, BlockState state, BlockPos pos, LivingEntity entity, boolean successfully) {
		if(!successfully) return;
		int newCount = Math.min(MAX_POLLINATES, state.getValue(COUNT) + 1);
		level.setBlock(pos, state.setValue(COUNT, newCount), 2);
	}

	public static void placeAt(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state, int flags) {
		level.setBlock(pos, state, flags);
	}
}