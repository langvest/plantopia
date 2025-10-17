package by.langvest.toolkit.event.client;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class RegisterRenderersEvent extends ClientEvent {
    public static abstract class ItemEvent extends RegisterRenderersEvent {
        public abstract void register(Item item, BlockEntityWithoutLevelRenderer renderer);

        public void registerAll(@NotNull Set<Item> items, BlockEntityWithoutLevelRenderer renderType) {
            for(var item : items) {
                register(item, renderType);
            }
        }
    }

    public static abstract class EntityEvent extends RegisterRenderersEvent {
        public abstract <T extends Entity> void register(EntityType<? extends T> entityType, EntityRendererProvider<T> provider);

        public <T extends Entity> void registerAll(@NotNull Set<EntityType<? extends T>> entityTypes, EntityRendererProvider<T> provider) {
            for(var entityType : entityTypes) {
                register(entityType, provider);
            }
        }
    }

    public static abstract class BlockEntityEvent extends RegisterRenderersEvent {
        public abstract <T extends BlockEntity> void register(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> provider);

        public <T extends BlockEntity> void registerAll(@NotNull Set<BlockEntityType<? extends T>> blockEntityTypes, BlockEntityRendererProvider<T> provider) {
            for(var blockEntityType : blockEntityTypes) {
                register(blockEntityType, provider);
            }
        }
    }
}
