package by.langvest.plantopia.fabric.handler;

import by.langvest.toolkit.client.render.item.CustomItemRenderer;
import by.langvest.toolkit.client.render.item.CustomItemRenderers;
import by.langvest.toolkit.event.LifecycleEvent;
import by.langvest.toolkit.event.client.*;
import by.langvest.toolkit.platform.EventEmitter;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class PlantopiaClientSetupHandler {
    public static void init() {
        var globalEventEmitter = EventEmitter.getDefaultInstance();

        globalEventEmitter.emit(new RegisterParticleProvidersEvent() {
            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> particleType, ParticleProvider<T> provider) {
                ParticleFactoryRegistry.getInstance().register(particleType, provider);
            }

            @Override
            public <T extends ParticleOptions> void registerSprite(ParticleType<T> particleType, ParticleProvider.Sprite<T> sprite) {
                ParticleFactoryRegistry.getInstance().register(particleType, sprite::createParticle);
            }

            @Override
            public <T extends ParticleOptions> void registerSpriteSet(ParticleType<T> particleType, ParticleEngine.SpriteParticleRegistration<T> registration) {
                ParticleFactoryRegistry.getInstance().register(particleType, registration::create);
            }
        });

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            globalEventEmitter.emit(new LifecycleEvent.ClientSetupEvent());

            globalEventEmitter.emit(new RegisterRenderLayersEvent.BlockEvent() {
                @Override
                public void register(Block block, RenderType renderType) {
                    BlockRenderLayerMap.INSTANCE.putBlock(block, renderType);
                }
            });

            globalEventEmitter.emit(new RegisterItemPropertiesEvent() {
                @Override
                public void registerGeneric(ResourceLocation propertyIdentifier, ClampedItemPropertyFunction property) {
                    ItemProperties.registerGeneric(propertyIdentifier, property);
                }

                @Override
                public void register(Item item, ResourceLocation propertyIdentifier, ClampedItemPropertyFunction property) {
                    ItemProperties.register(item, propertyIdentifier, property);
                }
            });

            globalEventEmitter.emit(new RegisterLayerDefinitionsEvent.EntityEvent() {
                @Override
                public void register(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier) {
                    EntityModelLayerRegistry.registerModelLayer(layerLocation, supplier::get);
                }
            });

            globalEventEmitter.emit(new RegisterRenderersEvent.ItemEvent() {
                @Override
                public void register(Item item, CustomItemRenderer renderer) {
                    CustomItemRenderers.register(item, renderer);
                }
            });

            globalEventEmitter.emit(new RegisterRenderersEvent.EntityEvent() {
                @Override
                public <T extends Entity> void register(EntityType<? extends T> entityType, EntityRendererProvider<T> provider) {
                    EntityRendererRegistry.register(entityType, provider);
                }
            });

            globalEventEmitter.emit(new RegisterRenderersEvent.BlockEntityEvent() {
                @Override
                public <T extends BlockEntity> void register(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> provider) {
                    BlockEntityRenderers.register(blockEntityType, provider);
                }
            });

            globalEventEmitter.emit(new RegisterColorsEvent.BlockEvent() {
                @Override
                protected void register(BlockColor color, Block... blocks) {
                    ColorProviderRegistry.BLOCK.register(color, blocks);
                }
            });

            globalEventEmitter.emit(new RegisterColorsEvent.ItemEvent() {
                @Override
                public BlockColors getBlockColors() {
                    return client.getBlockColors();
                }

                @Override
                protected void register(ItemColor color, Item... items) {
                    ColorProviderRegistry.ITEM.register(color, items);
                }
            });
        });
    }
}
