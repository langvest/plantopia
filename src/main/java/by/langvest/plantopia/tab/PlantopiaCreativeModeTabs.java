package by.langvest.plantopia.tab;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.meta.PlantopiaMetaStore;
import by.langvest.plantopia.util.helper.PlantopiaTemplateHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopiaLocationFrom;

public class PlantopiaCreativeModeTabs {
	private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Plantopia.MOD_ID);

	public static final ResourceKey<CreativeModeTab> PLANTOPIA = registerTab(Plantopia.MOD_ID, () -> new ItemStack(PlantopiaBlocks.FIREWEED.get()));

	private static @NotNull ResourceKey<CreativeModeTab> registerTab(String name, Supplier<ItemStack> iconSupplier) {
		var key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, plantopiaLocationFrom(name));

		CREATIVE_MODE_TAB_REGISTER.register(name, () -> CreativeModeTab.builder()
			.icon(iconSupplier)
			.title(Component.translatable(PlantopiaTemplateHelper.getCreativeModeTabTitleKey(name)))
			.displayItems((parameters, output) -> PlantopiaMetaStore.getItems()
				.forEach(itemMeta -> {
					List<ResourceKey<CreativeModeTab>> groups = itemMeta.getGroups();

					if(groups.contains(key)) output.accept(itemMeta.getItem());
				})
			)
			.build()
		);

		return key;
	}

	public static void setup(IEventBus bus) {
		CREATIVE_MODE_TAB_REGISTER.register(bus);
	}
}