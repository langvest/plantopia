package by.langvest.plantopia.fabric.handler;

import by.langvest.plantopia.Plantopia;
import by.langvest.toolkit.client.render.item.CustomItemRenderer;
import by.langvest.toolkit.client.render.item.CustomItemRenderers;
import by.langvest.toolkit.event.LifecycleEvent;
import by.langvest.toolkit.event.client.*;
import by.langvest.toolkit.platform.EventEmitter;
import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
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
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaClientSetupHandler {
    public static void setup() {
        var globalEventEmitter = EventEmitter.getDefaultInstance();
        var registryHelper = Plantopia.getPlatform().getRegistryHelper();

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

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entries) ->
            globalEventEmitter.emit(new ModifyCreativeModeTabEvent() {
                @Override
                public CreativeModeTab getTab() {
                    return group;
                }

                @Override
                public ResourceKey<CreativeModeTab> getTabKey() {
                    return registryHelper.getResourceKeyOrThrow(getTab());
                }

                @Override
                public void append(Collection<ItemStack> stacks, CreativeModeTab.TabVisibility tabVisibility) {
                    stacks.forEach(stack -> entries.accept(stack, tabVisibility));
                }

                @Override
                public void prepend(Collection<ItemStack> stacks, CreativeModeTab.TabVisibility tabVisibility) {
                    Lists.newLinkedList(stacks).descendingIterator().forEachRemaining(stack -> entries.prepend(stack, tabVisibility));
                }

                @Override
                public boolean addBefore(Predicate<ItemStack> targetPredicate, Collection<ItemStack> stacks, CreativeModeTab.TabVisibility tabVisibility) {
                    if (entries.getDisplayStacks().stream().anyMatch(targetPredicate)) {
                        entries.addBefore(targetPredicate, stacks, tabVisibility);
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean addAfter(Predicate<ItemStack> targetPredicate, Collection<ItemStack> stacks, CreativeModeTab.TabVisibility tabVisibility) {
                    if (entries.getDisplayStacks().stream().anyMatch(targetPredicate)) {
                        entries.addAfter(targetPredicate, stacks, tabVisibility);
                        return true;
                    }
                    return false;
                }
            }
        ));

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
