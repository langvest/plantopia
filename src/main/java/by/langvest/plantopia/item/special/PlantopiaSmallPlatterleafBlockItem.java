package by.langvest.plantopia.item.special;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.level.block.Block;

public class PlantopiaSmallPlatterleafBlockItem extends PlaceOnWaterBlockItem {
	public PlantopiaSmallPlatterleafBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public EquipmentSlot getEquipmentSlot(ItemStack itemStack) {
		return EquipmentSlot.HEAD;
	}
}
