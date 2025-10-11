package by.langvest.plantopia.item.special;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class PlantopiaLuckyDaisyBlockItem extends BlockItem {
	public PlantopiaLuckyDaisyBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	//	@Override
//	public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
//		super.appendHoverText(itemStack, level, tooltip, flag);
//
//		var tag = BlockItem.getBlockEntityData(itemStack);
//
//		if(tag != null && tag.contains("Color")) {
//			if(flag.isAdvanced()) {
//				tooltip.add(Component.translatable("item.color", String.format(Locale.ROOT, "#%06X", tag.getInt("Color"))).withStyle(ChatFormatting.GRAY));
//			}
//		} else if(flag.isCreative()) {
//			tooltip.add(Component.translatable(PlantopiaTemplateHelper.TOOLTIP_RANDOM_VARIANT_KEY).withStyle(ChatFormatting.GRAY));
//		}
//	}
}
