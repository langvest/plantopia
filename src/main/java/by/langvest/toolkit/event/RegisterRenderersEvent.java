package by.langvest.toolkit.event;

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
    public static class ItemEvent extends RegisterRenderersEvent {
        protected final Registrar<Item, BlockEntityWithoutLevelRenderer> registrar;

        public ItemEvent(Registrar<Item, BlockEntityWithoutLevelRenderer> registrar) {
            this.registrar = registrar;
        }

        public void register(Item item, BlockEntityWithoutLevelRenderer renderer) {
            registrar.register(item, renderer);
        }

        public void registerAll(@NotNull Set<Item> items, BlockEntityWithoutLevelRenderer renderType) {
            items.forEach(item -> registrar.register(item, renderType));
        }
    }

    public static class EntityEvent extends RegisterRenderersEvent {
        protected final Registrar<EntityType<? extends Entity>, EntityRendererProvider<? extends Entity>> registrar;

        public EntityEvent(Registrar<EntityType<? extends Entity>, EntityRendererProvider<? extends Entity>> registrar) {
            this.registrar = registrar;
        }

        public <T extends Entity> void register(EntityType<? extends T> entityType, EntityRendererProvider<T> provider) {
            registrar.register(entityType, provider);
        }

        public <T extends Entity> void registerAll(@NotNull Set<EntityType<? extends T>> entityTypes, EntityRendererProvider<T> provider) {
            entityTypes.forEach(entityType -> registrar.register(entityType, provider));
        }
    }

    public static class BlockEntityEvent extends RegisterRenderersEvent {
        protected final Registrar<BlockEntityType<? extends BlockEntity>, BlockEntityRendererProvider<? extends BlockEntity>> registrar;

        public BlockEntityEvent(Registrar<BlockEntityType<? extends BlockEntity>, BlockEntityRendererProvider<? extends BlockEntity>> registrar) {
            this.registrar = registrar;
        }

        public <T extends BlockEntity> void register(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> provider) {
            registrar.register(blockEntityType, provider);
        }

        public <T extends BlockEntity> void registerAll(@NotNull Set<BlockEntityType<? extends T>> blockEntityTypes, BlockEntityRendererProvider<T> provider) {
            blockEntityTypes.forEach(blockEntityType -> registrar.register(blockEntityType, provider));
        }
    }

    @FunctionalInterface
    public interface Registrar<T, R> {
        void register(T element, R renderer);
    }
}
