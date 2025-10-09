package by.langvest.plantopia.client.color;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaTripleBlockHalf;
import by.langvest.plantopia.block.entity.special.PlantopiaSeaShellBlockEntity;
import by.langvest.plantopia.block.special.PlantopiaSeaShellBlock;
import by.langvest.plantopia.block.special.PlantopiaTallReedsBlock;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.property.PlantopiaTintType;
import by.langvest.plantopia.util.helper.PlantopiaBlockHelper;
import by.langvest.toolkit.meta.SimpleMetaObject;
import com.google.common.collect.Sets;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.Collectors;

import static by.langvest.plantopia.client.color.PlantopiaBlockColors.registerBlockColor;
import static by.langvest.plantopia.client.color.PlantopiaItemColors.registerItemColor;

public class PlantopiaColors {
	private final Set<Block> BLOCK_GRASS_COLOR_0 = Sets.newHashSet();
	private final Set<Block> BLOCK_GRASS_COLOR_1 = Sets.newHashSet();
	private final Set<Block> BLOCK_FOLIAGE_COLOR_0 = Sets.newHashSet();
	private final Set<Block> BLOCK_FOLIAGE_COLOR_1 = Sets.newHashSet();
	private final Set<Block> BLOCK_LILY_PAD_COLOR_0 = Sets.newHashSet();
	private final Set<Block> BLOCK_LILY_PAD_COLOR_1 = Sets.newHashSet();
	private final Set<Block> BLOCK_WATERLILY_COLOR_0 = Sets.newHashSet();
	private final Set<Block> BLOCK_WATERLILY_COLOR_1 = Sets.newHashSet();
	private final Set<Item> ITEM_GRASS_COLOR_0 = Sets.newHashSet();
	private final Set<Item> ITEM_GRASS_COLOR_1 = Sets.newHashSet();
	private final Set<Item> ITEM_INHERIT_BLOCK_COLOR = Sets.newHashSet();

	private static PlantopiaColors instance;

	private PlantopiaColors() {
		addColors();
	}

	public static PlantopiaColors getInstance() {
		if(instance == null) {
			instance = new PlantopiaColors();
		}

		return instance;
	}

	public void addColors() {
		generateAll();
		seaShellBlocks();
		pottedFernBlock(Blocks.POTTED_FERN);
		fireweedBlock(PlantopiaBlocks.FIREWEED.get());
		tallReedsBlock(PlantopiaBlocks.TALL_REEDS.get());
	}

	public void addBlockColors() {
		registerBlockColor(BLOCK_GRASS_COLOR_0, (state, level, pos, tintIndex) -> grassTint(state, level, pos, tintIndex, 0));
		registerBlockColor(BLOCK_GRASS_COLOR_1, (state, level, pos, tintIndex) -> grassTint(state, level, pos, tintIndex, 1));
		registerBlockColor(BLOCK_FOLIAGE_COLOR_0, (state, level, pos, tintIndex) -> foliageTint(state, level, pos, tintIndex, 0));
		registerBlockColor(BLOCK_FOLIAGE_COLOR_1, (state, level, pos, tintIndex) -> foliageTint(state, level, pos, tintIndex, 1));
		registerBlockColor(BLOCK_LILY_PAD_COLOR_0, (state, level, pos, tintIndex) -> lilyPadTint(state, level, pos, tintIndex, 0));
		registerBlockColor(BLOCK_LILY_PAD_COLOR_1, (state, level, pos, tintIndex) -> lilyPadTint(state, level, pos, tintIndex, 1));
		registerBlockColor(BLOCK_WATERLILY_COLOR_0, (state, level, pos, tintIndex) -> waterlilyTint(state, level, pos, tintIndex, 0));
		registerBlockColor(BLOCK_WATERLILY_COLOR_1, (state, level, pos, tintIndex) -> waterlilyTint(state, level, pos, tintIndex, 1));
	}

	public void addItemColors(BlockColors blockColors) {
		registerItemColor(ITEM_GRASS_COLOR_0, (itemStuck, tintIndex) -> grassTint(tintIndex, 0));
		registerItemColor(ITEM_GRASS_COLOR_1, (itemStuck, tintIndex) -> grassTint(tintIndex, 1));
		registerItemColor(ITEM_INHERIT_BLOCK_COLOR, (itemStack, tintIndex) -> {
			BlockItem item = (BlockItem)itemStack.getItem();
			BlockState state = item.getBlock().defaultBlockState();
			return blockColors.getColor(state, null, null, tintIndex);
		});
	}

	private void generateAll() {
		PlantopiaMetaBuckets.BLOCK.forEach(blockMeta -> {
			if(!blockMeta.shouldApplyTint()) return;

			var block = blockMeta.get();
			var tintType = blockMeta.getTintType();

			if(blockMeta.shouldApplyTintToItem()) ITEM_INHERIT_BLOCK_COLOR.add(block.asItem());

			if(tintType == PlantopiaTintType.GRASS) {
				if(blockMeta.shouldApplyTintToParticles()) BLOCK_GRASS_COLOR_0.add(block);
				else BLOCK_GRASS_COLOR_1.add(block);
				return;
			}

			if(tintType == PlantopiaTintType.LILY_PAD) {
				if(blockMeta.shouldApplyTintToParticles()) BLOCK_LILY_PAD_COLOR_0.add(block);
				else BLOCK_LILY_PAD_COLOR_1.add(block);
				return;
			}

			if(tintType == PlantopiaTintType.WATERLILY) {
				if(blockMeta.shouldApplyTintToParticles()) BLOCK_WATERLILY_COLOR_0.add(block);
				else BLOCK_WATERLILY_COLOR_1.add(block);
				return;
			}

			if(tintType == PlantopiaTintType.FOLIAGE) {
				if(blockMeta.shouldApplyTintToParticles()) BLOCK_FOLIAGE_COLOR_0.add(block);
				else BLOCK_FOLIAGE_COLOR_1.add(block);
			}
		});
	}

	private void seaShellBlocks() {
		Set<Block> seaShellBlocks = PlantopiaMetaBuckets.BLOCK.stream()
			.map(SimpleMetaObject::get)
			.filter(block -> block instanceof PlantopiaSeaShellBlock)
			.collect(Collectors.toSet());

		Set<Item> seaShellItems = seaShellBlocks.stream()
			.map(Block::asItem)
			.collect(Collectors.toSet());

		registerBlockColor(seaShellBlocks, (state, level, pos, tintIndex) -> {
			if(tintIndex == 1 && level != null && pos != null && level.getBlockEntity(pos) instanceof PlantopiaSeaShellBlockEntity seaShellBlockEntity) {
				return seaShellBlockEntity.getColor();
			}

			return noColor();
		});

		registerItemColor(seaShellItems, (itemStack, tintIndex) -> {
			if(tintIndex == 1) {
				var tag = BlockItem.getBlockEntityData(itemStack);

				if(tag != null && tag.contains("Color")) {
					return tag.getInt("Color");
				} else {
					return PlantopiaSeaShellBlockEntity.DEFAULT_COLOR;
				}
			}

			return noColor();
		});
	}

	/* CUSTOM TINTS ******************************************/

	private void fireweedBlock(@NotNull Block block) {
		ITEM_GRASS_COLOR_0.add(block.asItem());

		registerBlockColor(block, (state, level, pos, tintIndex) -> {
			var half = state.getValue(DoublePlantBlock.HALF);
			var baseBlockPos = getBaseBlockPos(state, pos);

			if(half == DoubleBlockHalf.UPPER && tintIndex == 1) return grassColor(level, baseBlockPos);
			if(half == DoubleBlockHalf.LOWER && tintIndex == 0) return grassColor(level, baseBlockPos);
			return noColor();
		});
	}

	private void tallReedsBlock(@NotNull Block block) {
		ITEM_GRASS_COLOR_0.add(block.asItem());

		registerBlockColor(block, (state, level, pos, tintIndex) -> {
			var half = state.getValue(PlantopiaTallReedsBlock.HALF);
			var baseBlockPos = getBaseBlockPos(state, pos);

			if(half == PlantopiaTripleBlockHalf.UPPER && tintIndex == 1) return grassColor(level, baseBlockPos);
			if(half == PlantopiaTripleBlockHalf.CENTRAL && tintIndex == 0) return grassColor(level, baseBlockPos);
			if(half == PlantopiaTripleBlockHalf.LOWER && tintIndex == 1) return grassColor(level, baseBlockPos);
			return noColor();
		});
	}

	@SuppressWarnings("SameParameterValue")
	private void pottedFernBlock(@NotNull Block block) {
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

	private static int lilyPadColor(BlockAndTintGetter level, BlockPos pos) {
		if(level == null || pos == null) return 7455580;
		return 2129968;
	}

	private static int waterlilyColor(BlockAndTintGetter level, BlockPos pos) {
		return 7779877;
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

	private static int lilyPadTint(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex, int targetTintIndex) {
		return tintIndex == targetTintIndex ? lilyPadColor(level, getBaseBlockPos(state, pos)) : noColor();
	}

	private static int waterlilyTint(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex, int targetTintIndex) {
		return tintIndex == targetTintIndex ? waterlilyColor(level, getBaseBlockPos(state, pos)) : noColor();
	}

	/* HELPER METHODS ******************************************/

	@Nullable
	private static BlockPos getBaseBlockPos(BlockState state, BlockPos pos) {
		if(state == null || pos == null) return null;

		return PlantopiaBlockHelper.getBaseBlockPos(state, pos);
	}
}