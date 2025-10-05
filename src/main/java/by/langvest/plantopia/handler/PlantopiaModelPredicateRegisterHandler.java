package by.langvest.plantopia.handler;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.special.PlantopiaLuckyDaisyBlock;
import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.item.special.PlantopiaLuckyDaisyBlockItem;
import by.langvest.plantopia.util.helper.PlantopiaItemHelper;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Plantopia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlantopiaModelPredicateRegisterHandler {
	@SubscribeEvent
	public static void registerModelProperties(ModelEvent.RegisterAdditional event) {
		registerLuckyDaisyProperty(PlantopiaItems.WHITE_LUCKY_DAISY.get());
		registerLuckyDaisyProperty(PlantopiaItems.PINK_LUCKY_DAISY.get());
	}

	private static void registerLuckyDaisyProperty(@NotNull ItemLike item) {
		ItemProperties.register(
			item.asItem(),
			PlantopiaLuckyDaisyBlockItem.PETAL_AMOUNT_PREDICATE,
			(itemStack, level, entity, seed) -> {
				var amount = PlantopiaLuckyDaisyBlock.MAX_PETALS;
				var blockStateTag = PlantopiaItemHelper.getBlockStateData(itemStack);

				if(blockStateTag != null) {
					var valueName = blockStateTag.getString(PlantopiaLuckyDaisyBlock.AMOUNT.getName());

					if(!valueName.isEmpty()) {
						amount = Integer.parseInt(valueName);
					}
				}

				return amount * 0.1F;
			}
		);
	}
}
