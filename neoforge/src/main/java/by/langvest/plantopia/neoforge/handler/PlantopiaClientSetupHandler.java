package by.langvest.plantopia.neoforge.handler;

import by.langvest.plantopia.Plantopia;
import by.langvest.toolkit.event.LifecycleEvent;
import by.langvest.toolkit.event.client.*;
import by.langvest.toolkit.platform.EventEmitter;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.minecraft;

@Mod.EventBusSubscriber(modid = Plantopia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class PlantopiaClientSetupHandler {
    private static final String ITEM_RENDER_PROPERTIES_FIELD_NAME = "renderProperties";

    @SubscribeEvent
    public static void handleClientSetup(@NotNull FMLClientSetupEvent event) {
        var globalEventEmitter = EventEmitter.getDefaultInstance();

        event.enqueueWork(() -> {
            globalEventEmitter.emit(new LifecycleEvent.ClientSetupEvent());

            globalEventEmitter.emit(new RegisterRenderLayersEvent.BlockEvent() {
                @Override
                public void register(Block block, RenderType renderType) {
                    ItemBlockRenderTypes.setRenderLayer(block, renderType);
                }
            });

            globalEventEmitter.emit(new RegisterRenderersEvent.ItemEvent() {
                @Override
                public void register(Item item, BlockEntityWithoutLevelRenderer renderer) {
                    PlantopiaClientSetupHandler.setItemRenderer(item, renderer);
                }
            });

            globalEventEmitter.emit(new RegisterItemPropertiesEvent() {
                @Override
                public void registerCustomModelData(ItemPropertyFunction property) {
                    ItemProperties.registerGeneric(minecraft("custom_model_data"), property);
                }

                @Override
                public void registerGeneric(ResourceLocation propertyIdentifier, ClampedItemPropertyFunction property) {
                    ItemProperties.registerGeneric(propertyIdentifier, property);
                }

                @Override
                public void registerGeneric(ResourceLocation propertyIdentifier, ItemPropertyFunction property) {
                    ItemProperties.registerGeneric(propertyIdentifier, property);
                }

                @Override
                public void register(Item item, ResourceLocation propertyIdentifier, ClampedItemPropertyFunction property) {
                    ItemProperties.register(item, propertyIdentifier, property);
                }

                @Override
                public void register(Item item, ResourceLocation propertyIdentifier, ItemPropertyFunction property) {
                    ItemProperties.register(item, propertyIdentifier, property);
                }
            });
        });
    }

    @SubscribeEvent
    public static void handleLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        var globalEventEmitter = EventEmitter.getDefaultInstance();

        globalEventEmitter.emit(new RegisterLayerDefinitionsEvent.EntityEvent() {
            @Override
            public void register(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier) {
                event.registerLayerDefinition(layerLocation, supplier);
            }
        });
    }

    @SubscribeEvent
    public static void handleEntityRenderers(@NotNull EntityRenderersEvent.RegisterRenderers event) {
        var globalEventEmitter = EventEmitter.getDefaultInstance();

        globalEventEmitter.emit(new RegisterRenderersEvent.EntityEvent() {
            @Override
            public <T extends Entity> void register(EntityType<? extends T> entityType, EntityRendererProvider<T> provider) {
                event.registerEntityRenderer(entityType, provider);
            }
        });

        globalEventEmitter.emit(new RegisterRenderersEvent.BlockEntityEvent() {
            @Override
            public <T extends BlockEntity> void register(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> provider) {
                event.registerBlockEntityRenderer(blockEntityType, provider);
            }
        });
    }

    @SubscribeEvent
    public static void handleBlockColors(@NotNull RegisterColorHandlersEvent.Block event) {
        var globalEventEmitter = EventEmitter.getDefaultInstance();
        var blockColors = event.getBlockColors();

        globalEventEmitter.emit(new RegisterColorsEvent.BlockEvent(blockColors) {
            @Override
            protected void register(BlockColor color, Block... blocks) {
                event.register(color, blocks);
            }
        });
    }

    @SubscribeEvent
    public static void handleItemColors(@NotNull RegisterColorHandlersEvent.Item event) {
        var globalEventEmitter = EventEmitter.getDefaultInstance();
        var itemColors = event.getItemColors();
        var blockColors = event.getBlockColors();

        globalEventEmitter.emit(new RegisterColorsEvent.ItemEvent(itemColors, blockColors) {
            @Override
            protected void register(ItemColor color, Item... items) {
                event.register(color, items);
            }
        });
    }

    @SubscribeEvent
    public static void handleParticles(@NotNull net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent event) {
        var globalEventEmitter = EventEmitter.getDefaultInstance();

        globalEventEmitter.emit(new RegisterParticleProvidersEvent() {
            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> particleType, ParticleProvider<T> provider) {
                event.registerSpecial(particleType, provider);
            }

            @Override
            public <T extends ParticleOptions> void registerSprite(ParticleType<T> particleType, ParticleProvider.Sprite<T> sprite) {
                event.registerSprite(particleType, sprite);
            }

            @Override
            public <T extends ParticleOptions> void registerSpriteSet(ParticleType<T> particleType, ParticleEngine.SpriteParticleRegistration<T> registration) {
                event.registerSpriteSet(particleType, registration);
            }
        });
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
