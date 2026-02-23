package by.langvest.plantopia.item.special;

import by.langvest.plantopia.blockentity.PlantopiaBlockEntities;
import by.langvest.plantopia.blockentity.special.PlantopiaSeaShellBlockEntity;
import by.langvest.plantopia.client.lang.PlantopiaLangKey;
import by.langvest.plantopia.item.PlantopiaUpdateUseOnContext;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class PlantopiaSeaShellBlockItem extends BlockItem implements PlantopiaUpdateUseOnContext {
    public PlantopiaSeaShellBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public UseOnContext updateUseOnContext(@NotNull UseOnContext context, RandomSource syncRandom) {
        var itemStack = context.getItemInHand();

        if (itemStack.hasTag()) return context;

        var player = context.getPlayer();
        var tag = new CompoundTag();
        var newItemStack = itemStack.copy();

        tag.putInt("Color", PlantopiaSeaShellBlockEntity.generateRandomColor(syncRandom));

        BlockItem.setBlockEntityData(newItemStack, PlantopiaBlockEntities.SEA_SHELL.get(), tag);

        if (player != null && !player.isCreative()) itemStack.shrink(1);

        return new UseOnContext(
            context.getLevel(),
            context.getPlayer(),
            context.getHand(),
            newItemStack,
            context.getHitResult()
        );
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemStack, level, tooltip, flag);

        var tag = BlockItem.getBlockEntityData(itemStack);

        if (tag != null && tag.contains("Color")) {
            if (flag.isAdvanced()) {
                tooltip.add(Component.translatable("item.color", String.format(Locale.ROOT, "#%06X", tag.getInt("Color"))).withStyle(ChatFormatting.GRAY));
            }
        } else if (flag.isCreative()) {
            tooltip.add(Component.translatable(PlantopiaLangKey.TOOLTIP_RANDOM_VARIANT).withStyle(ChatFormatting.GRAY));
        }
    }
}
