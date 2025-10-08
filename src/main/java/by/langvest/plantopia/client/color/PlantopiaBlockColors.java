package by.langvest.plantopia.client.color;

import by.langvest.toolkit.event.RegisterColorsEvent;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class PlantopiaBlockColors {
	private static final List<Pair<Set<Block>, BlockColor>> BLOCK_COLORS = Lists.newArrayList();

	public static void registerBlockColor(Block block, BlockColor blockColor) {
		BLOCK_COLORS.add(Pair.of(Set.of(block), blockColor));
	}

	public static void registerBlockColor(Set<Block> blocks, BlockColor blockColor) {
		BLOCK_COLORS.add(Pair.of(blocks, blockColor));
	}

	public static void setup(RegisterColorsEvent.@NotNull Block event) {
		PlantopiaColors.getInstance().addBlockColors();
		event.registerAll(BLOCK_COLORS);
	}
}
