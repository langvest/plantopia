package by.langvest.plantopia.item.special;

import by.langvest.plantopia.block.special.PlantopiaLuckyDaisyBlock;
import by.langvest.plantopia.client.lang.PlantopiaLangKey;
import by.langvest.plantopia.util.helper.PlantopiaItemHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlantopiaLuckyDaisyBlockItem extends BlockItem {
    public PlantopiaLuckyDaisyBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        var blockState = PlantopiaItemHelper.getBlockStateFrom(stack);
        int amount = PlantopiaLuckyDaisyBlock.MAX_PETALS;

        if (blockState != null && blockState.hasProperty(PlantopiaLuckyDaisyBlock.AMOUNT)) {
            amount = blockState.getValue(PlantopiaLuckyDaisyBlock.AMOUNT);
        }

        var component = getHoverComponent(amount);

        if (component != null) {
            tooltip.add(component.withStyle(ChatFormatting.GRAY));
        }
    }

    private static @Nullable MutableComponent getHoverComponent(int amount) {
        if (amount < PlantopiaLuckyDaisyBlock.MAX_PETALS) {
            if (amount > PlantopiaLuckyDaisyBlock.MIN_PETALS) {
                return Component.translatable(PlantopiaLangKey.TOOLTIP_REMAINING_PETALS, amount);
            } else {
                return Component.translatable(PlantopiaLangKey.TOOLTIP_NO_PETALS);
            }
        }

        return null;
    }
}
