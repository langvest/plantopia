package by.langvest.plantopia.item.special;

import by.langvest.plantopia.item.PlantopiaItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class PlantopiaPlatterleafBlockItem extends PlaceOnWaterBlockItem {
	public PlantopiaPlatterleafBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public EquipmentSlot getEquipmentSlot(@NotNull ItemStack itemStack) {
		if(itemStack.is(PlantopiaItems.SMALL_PLATTERLEAF.get())) {
			return EquipmentSlot.HEAD;
		}

		return super.getEquipmentSlot(itemStack);
	}
}
