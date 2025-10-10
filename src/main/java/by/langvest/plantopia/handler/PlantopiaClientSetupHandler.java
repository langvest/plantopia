package by.langvest.plantopia.handler;

import by.langvest.plantopia.Plantopia;
import by.langvest.toolkit.event.LifecycleEvent;
import by.langvest.toolkit.event.RegisterColorsEvent;
import by.langvest.toolkit.event.RegisterRenderLayersEvent;
import by.langvest.toolkit.event.RegisterRenderersEvent;
import by.langvest.toolkit.platform.EventEmitter;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = Plantopia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PlantopiaClientSetupHandler {
	private static final String ITEM_RENDER_PROPERTIES_FIELD_NAME = "renderProperties";
	
	@SubscribeEvent
	@SuppressWarnings("removal")
	public static void handleClientSetup(@NotNull FMLClientSetupEvent event) {
		var globalEmitter = EventEmitter.getDefaultInstance();

		event.enqueueWork(() -> {
			globalEmitter.emit(new LifecycleEvent.ClientSetupEvent());
			globalEmitter.emit(new RegisterRenderLayersEvent.BlockEvent(ItemBlockRenderTypes::setRenderLayer));
			globalEmitter.emit(new RegisterRenderersEvent.ItemEvent(PlantopiaClientSetupHandler::setItemRenderer));
		});
	}

    @SubscribeEvent
    @SuppressWarnings({"rawtypes", "unchecked"})
	public static void handleEntityRenderers(@NotNull EntityRenderersEvent.RegisterRenderers event) {
		var globalEmitter = EventEmitter.getDefaultInstance();

		globalEmitter.emit(new RegisterRenderersEvent.EntityEvent((entityType, provider) -> event.registerEntityRenderer(entityType, (EntityRendererProvider) provider)));
		globalEmitter.emit(new RegisterRenderersEvent.BlockEntityEvent((blockEntityType, provider) -> event.registerBlockEntityRenderer(blockEntityType, (BlockEntityRendererProvider) provider)));
	}

	@SubscribeEvent
	public static void handleBlockColors(@NotNull RegisterColorHandlersEvent.Block event) {
		var globalEmitter = EventEmitter.getDefaultInstance();
		var blockColors = event.getBlockColors();

		globalEmitter.emit(new RegisterColorsEvent.BlockEvent(blockColors));
	}

	@SubscribeEvent
	public static void handleItemColors(@NotNull RegisterColorHandlersEvent.Item event) {
		var globalEmitter = EventEmitter.getDefaultInstance();
		var itemColors = event.getItemColors();
		var blockColors = event.getBlockColors();

		globalEmitter.emit(new RegisterColorsEvent.ItemEvent(itemColors, blockColors));
	}
	
	private static void setItemRenderer(@NotNull Item item, BlockEntityWithoutLevelRenderer renderer) {
		try {
			Field field = Item.class.getDeclaredField(ITEM_RENDER_PROPERTIES_FIELD_NAME);
			field.setAccessible(true);
			field.set(item, new IClientItemExtensions() {
				@Override
				public BlockEntityWithoutLevelRenderer getCustomRenderer() {
					return renderer;
				}
			});
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(String.format(
				"Failed to set item renderer using reflection. The '%s' field was not found in the Item class. " +
				"This is a fragile process that can break with Forge updates. " +
				"Please contact the library author to update the item renderer registration.",
				ITEM_RENDER_PROPERTIES_FIELD_NAME
			), e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(String.format(
				"Failed to set item renderer using reflection due to a security manager disallowing access. " +
				"The '%s' field could not be modified.",
				ITEM_RENDER_PROPERTIES_FIELD_NAME
			), e);
		}
	}
}
