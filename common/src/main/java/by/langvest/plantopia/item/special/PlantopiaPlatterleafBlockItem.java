package by.langvest.plantopia.item.special;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaPlatterleafBlockItem extends PlaceOnWaterBlockItem implements Equipable {
    public PlantopiaPlatterleafBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }
}
