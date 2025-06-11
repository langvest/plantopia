package by.langvest.plantopia.client.color;

import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaTripleBlockHalf;
import by.langvest.plantopia.meta.PlantopiaMetaRegistries;
import by.langvest.plantopia.meta.property.PlantopiaTintType;
import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class PlantopiaColors {
	private static final Set<Block> BLOCK_GRASS_COLOR_0 = Sets.newHashSet();
	private static final Set<Block> BLOCK_GRASS_COLOR_1 = Sets.newHashSet();
	private static final Set<Block> BLOCK_FOLIAGE_COLOR_0 = Sets.newHashSet();
	private static final Set<Block> BLOCK_FOLIAGE_COLOR_1 = Sets.newHashSet();
	private static final Set<Item> ITEM_GRASS_COLOR_0 = Sets.newHashSet();
	private static final Set<Item> ITEM_INHERIT_BLOCK_COLOR = Sets.newHashSet();

	public static void setup() {
		registerAll();

		pottedFernBlock(Blocks.POTTED_FERN);
		fireweedBlock(PlantopiaBlocks.FIREWEED.get());

		setAll();
	}

	@SuppressWarnings("unused")
	private static void add(@NotNull Set<Block> blockSet, Block... blocks) {
		blockSet.addAll(List.of(blocks));
	}

	@SuppressWarnings("unused")
	private static void add(@NotNull Set<Item> itemSet, ItemLike... itemLikes) {
		itemSet.addAll(Arrays.stream(itemLikes).map(ItemLike::asItem).toList());
	}

	private static void registerAll() {
		PlantopiaMetaRegistries.BLOCKS.forEach(blockMeta -> {
			if(!blockMeta.shouldApplyTint()) return;

			Block block = blockMeta.getBlock();
			PlantopiaTintType tintType = blockMeta.getTintType();

			if(blockMeta.shouldApplyTintToItem()) ITEM_INHERIT_BLOCK_COLOR.add(block.asItem());

			if(tintType == PlantopiaTintType.GRASS) {
				if(blockMeta.shouldApplyTintToParticles()) BLOCK_GRASS_COLOR_0.add(block);
				else BLOCK_GRASS_COLOR_1.add(block);
				return;
			}

			if(tintType == PlantopiaTintType.FOLIAGE) {
				if(blockMeta.shouldApplyTintToParticles()) BLOCK_FOLIAGE_COLOR_0.add(block);
				else BLOCK_FOLIAGE_COLOR_1.add(block);
			}
		});
	}

	private static void setAll() {
		BlockColors blockColors = Minecraft.getInstance().getBlockColors();
		ItemColors itemColors = Minecraft.getInstance().getItemColors();

		/* BLOCK GRASS COLOR 0 ******************************************/
		blockColors.register(
			(state, level, pos, tintIndex) -> grassTint(state, level, pos, tintIndex, 0),
			BLOCK_GRASS_COLOR_0.toArray(Block[]::new)
		);

		/* BLOCK GRASS COLOR 1 ******************************************/
		blockColors.register(
			(state, level, pos, tintIndex) -> grassTint(state, level, pos, tintIndex, 1),
			BLOCK_GRASS_COLOR_1.toArray(Block[]::new)
		);

		/* BLOCK FOLIAGE COLOR 0 ******************************************/
		blockColors.register(
			(state, level, pos, tintIndex) -> foliageTint(state, level, pos, tintIndex, 0),
			BLOCK_FOLIAGE_COLOR_0.toArray(Block[]::new)
		);

		/* BLOCK FOLIAGE COLOR 1 ******************************************/
		blockColors.register(
			(state, level, pos, tintIndex) -> foliageTint(state, level, pos, tintIndex, 1),
			BLOCK_FOLIAGE_COLOR_1.toArray(Block[]::new)
		);

		/* ITEM GRASS COLOR 0 ******************************************/
		itemColors.register(
			(itemStuck, tintIndex) -> grassTint(tintIndex, 0),
			ITEM_GRASS_COLOR_0.toArray(Item[]::new)
		);

		/* ITEM INHERIT BLOCK COLOR 0+ ******************************************/
		itemColors.register(
			(itemStack, tintIndex) -> {
				BlockItem item = (BlockItem)itemStack.getItem();
				BlockState state = item.getBlock().defaultBlockState();
				return blockColors.getColor(state, null, null, tintIndex);
			},
			ITEM_INHERIT_BLOCK_COLOR.toArray(Item[]::new)
		);
	}

	/* CUSTOM TINTS ******************************************/

	private static void fireweedBlock(@NotNull Block block) {
		BlockColors blockColors = Minecraft.getInstance().getBlockColors();

		ITEM_GRASS_COLOR_0.add(block.asItem());

		/* FIREWEED BLOCK GRASS COLOR 0-1 ******************************************/
		blockColors.register(
			(state, level, pos, tintIndex) -> {
				DoubleBlockHalf half = state.getValue(DoublePlantBlock.HALF);
				BlockPos baseBlockPos = getBaseBlockPos(state, pos);
				if(half == DoubleBlockHalf.UPPER && tintIndex == 1) return grassColor(level, baseBlockPos);
				if(half == DoubleBlockHalf.LOWER && tintIndex == 0) return grassColor(level, baseBlockPos);
				return noColor();
			},
			block
		);
	}

	@SuppressWarnings("SameParameterValue")
	private static void pottedFernBlock(@NotNull Block block) {
		BLOCK_GRASS_COLOR_1.add(block);
	}

	/* COLORS ******************************************/

	private static int noColor() {
		return -1;
	}

	private static int grassColor(BlockAndTintGetter level, BlockPos pos) {
		if(level == null || pos == null) return GrassColor.get(0.5D, 1.0D);
		return BiomeColors.getAverageGrassColor(level, pos);
	}

	private static int foliageColor(BlockAndTintGetter level, BlockPos pos) {
		if(level == null || pos == null) return FoliageColor.getDefaultColor();
		return BiomeColors.getAverageFoliageColor(level, pos);
	}

	/* TINTS ******************************************/

	private static int grassTint(int tintIndex, int targetTintIndex) {
		return grassTint(null, null, null, tintIndex, targetTintIndex);
	}

	private static int grassTint(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex, int targetTintIndex) {
		return tintIndex == targetTintIndex ? grassColor(level, getBaseBlockPos(state, pos)) : noColor();
	}

	private static int foliageTint(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex, int targetTintIndex) {
		return tintIndex == targetTintIndex ? foliageColor(level, getBaseBlockPos(state, pos)) : noColor();
	}

	/* HELPER METHODS ******************************************/

	@Nullable
	private static BlockPos getBaseBlockPos(BlockState state, BlockPos pos) {
		if(state == null || pos == null) return null;
		if(state.hasProperty(PlantopiaBlockStateProperties.TRIPLE_BLOCK_HALF)) {
			PlantopiaTripleBlockHalf half = state.getValue(PlantopiaBlockStateProperties.TRIPLE_BLOCK_HALF);
			if(half == PlantopiaTripleBlockHalf.UPPER) return pos.below(2);
			if(half == PlantopiaTripleBlockHalf.CENTRAL) return pos.below(1);
			return pos;
		}
		if(state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
			DoubleBlockHalf half = state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);
			if(half == DoubleBlockHalf.UPPER) return pos.below(1);
			return pos;
		}
		return pos;
	}
}