package by.langvest.plantopia.tab;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.util.helper.PlantopiaTemplateHelper;
import by.langvest.toolkit.event.RegisterEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

@ParametersAreNonnullByDefault
public class PlantopiaCreativeModeTabs {
    public static final ResourceKey<CreativeModeTab> MAIN = registerTab("main", () -> PlantopiaBlocks.FIREWEED.get().asItem().getDefaultInstance());

    private static @NotNull ResourceKey<CreativeModeTab> registerTab(String name, Supplier<ItemStack> iconSupplier) {
        return registerTab(plantopia(name), iconSupplier);
    }

    private static @NotNull ResourceKey<CreativeModeTab> registerTab(ResourceLocation identifier, Supplier<ItemStack> icon) {
        var key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, identifier);

        PlantopiaRegistries.CREATIVE_MODE_TAB.register(identifier, () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(icon)
            .title(Component.translatable(PlantopiaTemplateHelper.getCreativeModeTabTitleKey(identifier)))
            .build()
        );

        return key;
    }

    public static void setup(RegisterEvent event) {
        event.registerAll(Registries.CREATIVE_MODE_TAB, PlantopiaRegistries.CREATIVE_MODE_TAB);
    }
}
