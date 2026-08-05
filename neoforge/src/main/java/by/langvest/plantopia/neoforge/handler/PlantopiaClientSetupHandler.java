package by.langvest.plantopia.neoforge.handler;

import by.langvest.plantopia.Plantopia;
import by.langvest.toolkit.client.render.item.CustomItemRenderer;
import by.langvest.toolkit.client.render.item.CustomItemRenderers;
import by.langvest.toolkit.event.LifecycleEvent;
import by.langvest.toolkit.event.client.*;
import by.langvest.toolkit.platform.EventEmitter;
import com.google.common.collect.Lists;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Plantopia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class PlantopiaClientSetupHandler {
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
                public void register(Item item, CustomItemRenderer renderer) {
                    CustomItemRenderers.register(item, renderer);
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

        globalEventEmitter.emit(new RegisterColorsEvent.BlockEvent() {
            @Override
            protected void register(BlockColor color, Block... blocks) {
                event.register(color, blocks);
            }
        });
    }

    @SubscribeEvent
    public static void handleItemColors(@NotNull RegisterColorHandlersEvent.Item event) {
        var globalEventEmitter = EventEmitter.getDefaultInstance();

        globalEventEmitter.emit(new RegisterColorsEvent.ItemEvent() {
            @Override
            public BlockColors getBlockColors() {
                return event.getBlockColors();
            }

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

    @SubscribeEvent
    public static void handleCreativeModeTabModifications(BuildCreativeModeTabContentsEvent event) {
        var globalEventEmitter = EventEmitter.getDefaultInstance();

        globalEventEmitter.emit(new ModifyCreativeModeTabEvent() {
            @Override
            public CreativeModeTab getTab() {
                return event.getTab();
            }

            @Override
            public ResourceKey<CreativeModeTab> getTabKey() {
                return event.getTabKey();
            }

            @Override
            public void append(Collection<ItemStack> stacks, CreativeModeTab.TabVisibility tabVisibility) {
                stacks.forEach(stack -> event.accept(stack, tabVisibility));
            }

            @Override
            public void prepend(Collection<ItemStack> stacks, CreativeModeTab.TabVisibility tabVisibility) {
                Lists.newLinkedList(stacks).descendingIterator().forEachRemaining(stack -> event.getEntries().putFirst(stack, tabVisibility));
            }

            @Override
            public boolean addBefore(Predicate<ItemStack> targetPredicate, Collection<ItemStack> stacks, CreativeModeTab.TabVisibility tabVisibility) {
                var entries = event.getEntries();
                var optionalTarget = findFirstTarget(targetPredicate);
                if (optionalTarget.isEmpty()) return false;
                ItemStack target = optionalTarget.get();
                ItemStack lastAdded = null;
                for (var current : stacks) {
                    if (lastAdded == null) {
                        entries.putBefore(target, current, tabVisibility);
                    } else {
                        entries.putAfter(lastAdded, current, tabVisibility);
                    }
                    lastAdded = current;
                }
                return true;
            }

            @Override
            public boolean addAfter(Predicate<ItemStack> targetPredicate, Collection<ItemStack> stacks, CreativeModeTab.TabVisibility tabVisibility) {
                var entries = event.getEntries();
                var optionalTarget = findLastTarget(targetPredicate);
                if (optionalTarget.isEmpty()) return false;
                ItemStack lastAdded = optionalTarget.get();
                for (var current : stacks) {
                    entries.putAfter(lastAdded, current, tabVisibility);
                    lastAdded = current;
                }
                return true;
            }

            private @NotNull Optional<ItemStack> findFirstTarget(Predicate<ItemStack> predicate) {
                for (var entry : event.getEntries()) {
                    if (predicate.test(entry.getKey())) {
                        return Optional.of(entry.getKey());
                    }
                }
                return Optional.empty();
            }

            private @NotNull Optional<ItemStack> findLastTarget(Predicate<ItemStack> predicate) {
                ItemStack result = null;
                for (var entry : event.getEntries()) {
                    if (predicate.test(entry.getKey())) {
                        result = entry.getKey();
                    }
                }
                return Optional.ofNullable(result);
            }
        });
    }
}
