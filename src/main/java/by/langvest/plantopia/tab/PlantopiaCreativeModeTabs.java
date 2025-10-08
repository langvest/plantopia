package by.langvest.plantopia.tab;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.util.helper.PlantopiaTemplateHelper;
import by.langvest.toolkit.event.RegistryEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaCreativeModeTabs {
	public static final ResourceKey<CreativeModeTab> MAIN = registerTab("main", () -> PlantopiaBlocks.FIREWEED.get().asItem().getDefaultInstance());

	private static @NotNull ResourceKey<CreativeModeTab> registerTab(String name, Supplier<ItemStack> iconSupplier) {
		return registerTab(plantopia(name), iconSupplier);
	}

	private static @NotNull ResourceKey<CreativeModeTab> registerTab(ResourceLocation identifier, Supplier<ItemStack> iconSupplier) {
		var key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, identifier);

		PlantopiaRegistries.CREATIVE_MODE_TAB.register(identifier, () -> CreativeModeTab.builder()
			.icon(iconSupplier)
			.title(Component.translatable(PlantopiaTemplateHelper.getCreativeModeTabTitleKey(identifier)))
			.displayItems((parameters, output) -> PlantopiaMetaBuckets.ITEM
				.getAll()
				.stream()
				.sorted(Comparator.comparingInt(itemMeta -> itemMeta.getOrderType().getOrder()))
				.forEach(itemMeta -> {
					List<ResourceKey<CreativeModeTab>> groups = itemMeta.getGroups();

					if(groups.contains(key)) output.accept(itemMeta.get());
				})
			)
			.build()
		);

		return key;
	}

	public static void setup(@NotNull RegistryEvent event) {
		event.registerAll(Registries.CREATIVE_MODE_TAB, PlantopiaRegistries.CREATIVE_MODE_TAB);
	}
}