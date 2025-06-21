package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaFloweringWaterlilyBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class PlantopiaFloweringSmallPlatterleafBlock extends PlantopiaSmallPlatterleafBlock implements PlantopiaFloweringWaterlilyBlock {
	protected final Supplier<Item> waterlilyItem;

	public PlantopiaFloweringSmallPlatterleafBlock(Supplier<Item> waterlilyItem, Properties properties) {
		super(properties);
		this.waterlilyItem = waterlilyItem;
	}

	@Override
	public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean isClient) {
		return false;
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
		return getWaterlilyItem().getDefaultInstance();
	}

	@Override
	public Item getWaterlilyItem() {
		return waterlilyItem.get();
	}

	@Override
	public Block getOriginBlock() {
		return PlantopiaBlocks.SMALL_PLATTERLEAF.get();
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult blockHitResult) {
		return useShears(state, level, pos, player, hand, blockHitResult);
	}
}
