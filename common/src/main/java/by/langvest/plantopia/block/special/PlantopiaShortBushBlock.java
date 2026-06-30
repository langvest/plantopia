package by.langvest.plantopia.block.special;

import by.langvest.plantopia.worldgen.feature.special.PlantopiaNaturalBlockFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaShortBushBlock extends BushBlock implements BonemealableBlock {
	protected static final VoxelShape SHAPE = Block.box(2.0F, 0.0F, 2.0F, 14.0F, 10.0F, 14.0F);
	protected @Nullable Supplier<BlockState> tallBushStateSupplier;

	public PlantopiaShortBushBlock(Properties properties) {
		super(properties);
	}

	public PlantopiaShortBushBlock(Properties properties, @Nullable Supplier<BlockState> tallBushStateSupplier) {
		super(properties);
		this.tallBushStateSupplier = tallBushStateSupplier;
	}

	@SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
		return tallBushStateSupplier != null;
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		PlantopiaNaturalBlockFeature.place(level, Objects.requireNonNull(tallBushStateSupplier).get(), pos, random, Block.UPDATE_ALL);
	}
}
