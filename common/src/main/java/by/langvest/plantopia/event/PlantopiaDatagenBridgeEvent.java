package by.langvest.plantopia.event;

import by.langvest.plantopia.util.PlantopiaTagSet;
import by.langvest.toolkit.event.Event;
import net.minecraft.data.BlockFamily;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class PlantopiaDatagenBridgeEvent extends Event {
    public static abstract class BlockTagEvent extends PlantopiaDatagenBridgeEvent {
        public abstract void provide(Bridge bridge);

        @Contract(value = "_ -> new", pure = true)
        public static @NotNull BlockTagEvent create(Consumer<Bridge> consumer) {
            return new BlockTagEvent() {
                @Override
                public void provide(Bridge bridge) {
                    consumer.accept(bridge);
                }
            };
        }

        public interface Bridge {
            PlantopiaTagSet<Block> getOrCreateTagSet(TagKey<Block> key);
        }
    }

    public static abstract class ItemTagEvent extends PlantopiaDatagenBridgeEvent {
        public abstract void provide(Bridge bridge);

        @Contract(value = "_ -> new", pure = true)
        public static @NotNull ItemTagEvent create(Consumer<Bridge> consumer) {
            return new ItemTagEvent() {
                @Override
                public void provide(Bridge bridge) {
                    consumer.accept(bridge);
                }
            };
        }

        public interface Bridge {
            PlantopiaTagSet<Item> getOrCreateTagSet(TagKey<Item> key);
        }
    }

    public static abstract class RecipeEvent extends PlantopiaDatagenBridgeEvent {
        public abstract void provide(Bridge bridge);

        @Contract(value = "_ -> new", pure = true)
        public static @NotNull RecipeEvent create(Consumer<Bridge> consumer) {
            return new RecipeEvent() {
                @Override
                public void provide(Bridge bridge) {
                    consumer.accept(bridge);
                }
            };
        }

        public interface Bridge {
            void planksFromLogs(ItemLike planks, TagKey<Item> logsTag, int count);

            default void planksFromLogs(ItemLike planks, TagKey<Item> logsTag) {
                planksFromLogs(planks, logsTag, 4);
            }

            void woodFromLogs(ItemLike wood, ItemLike log);

            void balksFromLogs(ItemLike wood, ItemLike log, int count);

            default void balksFromLogs(ItemLike balk, ItemLike log) {
                balksFromLogs(balk, log, 6);
            }

            void woodenBoat(ItemLike boat, ItemLike planks);

            void chestBoat(ItemLike chestBoat, ItemLike boat);

            void hangingSign(ItemLike hangingSign, ItemLike strippedLog);

            void blockFamily(BlockFamily blockFamily);
        }
    }

    public static abstract class BlockLootTableEvent extends PlantopiaDatagenBridgeEvent {
        public abstract void provide(Bridge bridge);

        @Contract(value = "_ -> new", pure = true)
        public static @NotNull BlockLootTableEvent create(Consumer<Bridge> consumer) {
            return new BlockLootTableEvent() {
                @Override
                public void provide(Bridge bridge) {
                    consumer.accept(bridge);
                }
            };
        }

        public interface Bridge {

        }
    }
}
