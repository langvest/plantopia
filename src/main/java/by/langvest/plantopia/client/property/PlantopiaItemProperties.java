package by.langvest.plantopia.client.property;

import by.langvest.plantopia.block.special.PlantopiaLuckyDaisyBlock;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.util.helper.PlantopiaItemHelper;
import by.langvest.toolkit.event.client.RegisterItemPropertiesEvent;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static by.langvest.plantopia.misc.PlantopiaItemPropertyTypes.*;

public class PlantopiaItemProperties {
    private static final Set<Item> LUCKY_DAISY = Sets.newHashSet();

    public static void setup(@NotNull RegisterItemPropertiesEvent event) {
        generateAll();

        event.registerAll(LUCKY_DAISY, PETAL_AMOUNT, PlantopiaItemProperties.luckyDaisyPetalAmount());
    }

    private static void generateAll() {
        PlantopiaMetaBuckets.BLOCK.forEach(blockMeta -> {
            var block = blockMeta.get();

            if(block instanceof PlantopiaLuckyDaisyBlock) {
                LUCKY_DAISY.add(block.asItem());
            }
        });
    }

    /* CUSTOM PROPERTIES ******************************************/

    @Contract(pure = true)
    private static @NotNull ClampedItemPropertyFunction luckyDaisyPetalAmount() {
        return (itemStack, level, entity, seed) -> {
            var amount = PlantopiaLuckyDaisyBlock.MAX_PETALS;
            var blockStateTag = PlantopiaItemHelper.getBlockStateData(itemStack);

            if(blockStateTag != null) {
                var valueName = blockStateTag.getString(PlantopiaLuckyDaisyBlock.AMOUNT.getName());

                if(!valueName.isEmpty()) {
                    amount = Integer.parseInt(valueName);
                }
            }

            return amount * 0.1F;
        };
    }
}
