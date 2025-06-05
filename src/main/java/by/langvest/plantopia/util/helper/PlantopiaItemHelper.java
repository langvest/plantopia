package by.langvest.plantopia.util.helper;

import by.langvest.plantopia.item.special.PlantopiaTripleHighBlockItem;
import by.langvest.plantopia.item.special.PlantopiaWideTripleHighBlockItem;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class PlantopiaItemHelper {
	public static @NotNull Supplier<BlockItem> getBlockItemSupplier(@NotNull PlantopiaBlockMeta blockMeta, Item.Properties properties) {
		int height = blockMeta.getBlockHeightType().getBaseHeight();
		int width = blockMeta.getBlockWidthType().getBaseWidth();

		if(height == 1) return () -> new BlockItem(blockMeta.getBlock(), properties);
		if(height == 2) return () -> new DoubleHighBlockItem(blockMeta.getBlock(), properties);
		if(height == 3 && width == 1) return () -> new PlantopiaTripleHighBlockItem(blockMeta.getBlock(), properties);
		if(height == 3 && width == 2) return () -> new PlantopiaWideTripleHighBlockItem(blockMeta.getBlock(), properties);
		return () -> new BlockItem(blockMeta.getBlock(), properties);
	}
}
