package by.langvest.plantopia.kit.config;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import by.langvest.plantopia.meta.property.PlantopiaOrderType;
import by.langvest.toolkit.meta.MetaAccessor;
import by.langvest.toolkit.registry.RegistryObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@ParametersAreNonnullByDefault
public record PlantopiaTreeKitConfiguration(
    Function<BlockState, MapColor> plankMapColor,
    Function<BlockState, MapColor> trunkMapColor,
    Function<BlockState, MapColor> logMapColor,
    PressurePlateBlock.Sensitivity pressurePlateSensitivity,
    int buttonTicksToStayPressed,
    boolean canArrowsPressButton,
    ResourceKey<Level> dimensionType,
    BlockMiddleware blockMiddleware,
    ItemMiddleware itemMiddleware,
    Function<ResourceLocation, BlockSetType> blockSetTypeFactory,
    Function<Pair<ResourceLocation, BlockSetType>, WoodType> woodTypeFactory
) {
    @SuppressWarnings("unchecked")
    public <T extends Block> RegistryObject<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> factory, PlantopiaBlockMeta.MetaProperties metaProperties) {
        var entry = blockMiddleware.apply(new BlockMiddleware.Entry(name, factory, metaProperties));
        return (RegistryObject<T>) PlantopiaBlocks.registerBlock(entry.name, entry.factory, entry.metaProperties);
    }

    @SuppressWarnings("unchecked")
    public <T extends Item> RegistryObject<T> registerItem(String name, Function<Item.Properties, T> factory, PlantopiaItemMeta.MetaProperties metaProperties) {
        var entry = itemMiddleware.apply(new ItemMiddleware.Entry(name, factory, metaProperties));
        return (RegistryObject<T>) PlantopiaItems.registerItem(entry.name, entry.factory, entry.metaProperties);
    }

    @Contract(" -> new")
    public static @NotNull Builder builder() {
        return new Builder();
    }

    @FunctionalInterface
    public interface BlockMiddleware {
        Entry apply(Entry entry);

        @FunctionalInterface
        interface Matcher {
            boolean matches(Entry entry);
        }

        class Entry {
            public String name;
            public Function<BlockBehaviour.Properties, ? extends Block> factory;
            public PlantopiaBlockMeta.MetaProperties metaProperties;

            public Entry(String name, Function<BlockBehaviour.Properties, ? extends Block> factory, PlantopiaBlockMeta.MetaProperties metaProperties) {
                this.name = name;
                this.factory = factory;
                this.metaProperties = metaProperties;
            }

            public PlantopiaBlockMeta.MetaType metaType() {
                return MetaAccessor.getMetaTypeFrom(metaProperties);
            }

            public Entry modifyMeta(Function<PlantopiaBlockMeta.MetaProperties, PlantopiaBlockMeta.MetaProperties> refiner) {
                metaProperties = refiner.apply(metaProperties);
                return this;
            }
        }
    }

    @FunctionalInterface
    public interface ItemMiddleware {
        Entry apply(Entry entry);

        @FunctionalInterface
        interface Matcher {
            boolean matches(Entry entry);
        }

        class Entry {
            public String name;
            public Function<Item.Properties, ? extends Item> factory;
            public PlantopiaItemMeta.MetaProperties metaProperties;

            public Entry(String name, Function<Item.Properties, ? extends Item> factory, PlantopiaItemMeta.MetaProperties metaProperties) {
                this.name = name;
                this.factory = factory;
                this.metaProperties = metaProperties;
            }

            public PlantopiaItemMeta.MetaType metaType() {
                return MetaAccessor.getMetaTypeFrom(metaProperties);
            }

            public Entry modifyMeta(Function<PlantopiaItemMeta.MetaProperties, PlantopiaItemMeta.MetaProperties> refiner) {
                metaProperties = refiner.apply(metaProperties);
                return this;
            }
        }
    }

    /**
     * Builder for creating {@link PlantopiaTreeKitConfiguration} instances.
     */
    public static class Builder {
        private Function<BlockState, MapColor> woodMapColor = state -> MapColor.WOOD;
        private Function<BlockState, MapColor> trunkMapColor = state -> MapColor.PODZOL;
        private Function<BlockState, MapColor> logMapColor = state -> state.getValue(RotatedPillarBlock.AXIS).isVertical() ? woodMapColor.apply(state) : trunkMapColor.apply(state);
        private PressurePlateBlock.Sensitivity pressurePlateSensitivity = PressurePlateBlock.Sensitivity.EVERYTHING;
        private int buttonTicksToStayPressed = 30;
        private boolean canArrowsPressButton = true;
        private ResourceKey<Level> dimensionType = Level.OVERWORLD;
        private BlockMiddleware blockMiddleware = entry -> entry;
        private ItemMiddleware itemMiddleware = entry -> entry;
        private Function<ResourceLocation, BlockSetType> blockSetTypeFactory = identifier -> {
            var blockSetType = new BlockSetType(identifier.toString());
            return registerBlockSetType(blockSetType);
        };
        private Function<Pair<ResourceLocation, BlockSetType>, WoodType> woodTypeFactory = pair -> {
            var woodType = new WoodType(pair.getFirst().toString(), pair.getSecond());
            return registerWoodType(woodType);
        };

        /**
         * Registers the given {@link BlockSetType}.
         * @param blockSetType The block set type to register.
         * @return The registered block set type.
         */
        public static @NotNull BlockSetType registerBlockSetType(BlockSetType blockSetType) {
            return BlockSetType.register(blockSetType);
        }

        /**
         * Registers the given {@link WoodType} and adds it to the client-side sheets if on the client.
         * @param woodType The wood type to register.
         * @return The registered wood type.
         */
        public static @NotNull WoodType registerWoodType(WoodType woodType) {
            return WoodType.register(woodType);
        }

        public PlantopiaTreeKitConfiguration build() {
            return new PlantopiaTreeKitConfiguration(
                woodMapColor,
                trunkMapColor,
                logMapColor,
                pressurePlateSensitivity,
                buttonTicksToStayPressed,
                canArrowsPressButton,
                dimensionType,
                blockMiddleware,
                itemMiddleware,
                blockSetTypeFactory,
                woodTypeFactory
            );
        }

        public Builder apply(@NotNull Consumer<Builder> consumer) {
            consumer.accept(this);
            return this;
        }

        public Builder dimensionType(ResourceKey<Level> dimensionType) {
            this.dimensionType = dimensionType;
            return this;
        }

        /**
         * Use an existing {@link WoodType}. No registration will be performed.
         * @param woodType The existing wood type to use.
         * @return This builder.
         */
        public Builder woodType(WoodType woodType) {
            this.woodTypeFactory = pair -> woodType;
            return this;
        }

        /**
         * Create a new {@link WoodType} using the provided factory. The created wood type will be registered automatically.
         * @param factory The factory to create the wood type.
         * @return This builder.
         */
        public Builder woodType(Function<Pair<ResourceLocation, BlockSetType>, WoodType> factory) {
            this.woodTypeFactory = pair -> registerWoodType(factory.apply(pair));
            return this;
        }

        /**
         * Create and register a new {@link WoodType} with full control over the registration process.
         * @param factory A factory that receives the identifier and a registration function.
         * @return This builder.
         */
        public Builder woodType(BiFunction<Pair<ResourceLocation, BlockSetType>, Function<WoodType, WoodType>, WoodType> factory) {
            this.woodTypeFactory = pair -> factory.apply(pair, Builder::registerWoodType);
            return this;
        }

        /**
         * Use an existing {@link BlockSetType}. No registration will be performed.
         * @param blockSetType The existing block set type to use.
         * @return This builder.
         */
        public Builder blockSetType(BlockSetType blockSetType) {
            this.blockSetTypeFactory = identifier -> blockSetType;
            return this;
        }

        /**
         * Create a new {@link BlockSetType} using the provided factory. The created block set type will be registered automatically.
         * @param factory The factory to create the block set type.
         * @return This builder.
         */
        public Builder blockSetType(Function<ResourceLocation, BlockSetType> factory) {
            this.blockSetTypeFactory = identifier -> registerBlockSetType(factory.apply(identifier));
            return this;
        }

        /**
         * Create and register a new {@link BlockSetType} with full control over the registration process.
         * @param factory A factory that receives the identifier and a registration function.
         * @return This builder.
         */
        public Builder blockSetType(BiFunction<ResourceLocation, Function<BlockSetType, BlockSetType>, BlockSetType> factory) {
            this.blockSetTypeFactory = identifier -> factory.apply(identifier, Builder::registerBlockSetType);
            return this;
        }

        public Builder orderType(PlantopiaOrderType orderType) {
            return blockMeta(metaProperties -> metaProperties.order(orderType))
                .itemMeta(metaProperties -> metaProperties.order(orderType));
        }

        public Builder blockMeta(BlockMiddleware.Matcher matcher, Function<PlantopiaBlockMeta.MetaProperties, PlantopiaBlockMeta.MetaProperties> refiner) {
            return blockMiddleware(matcher, entry -> entry.modifyMeta(refiner));
        }

        public Builder blockMeta(Function<PlantopiaBlockMeta.MetaProperties, PlantopiaBlockMeta.MetaProperties> refiner) {
            return blockMiddleware(entry -> true, entry -> entry.modifyMeta(refiner));
        }

        public Builder blockMiddleware(BlockMiddleware.Matcher matcher, BlockMiddleware middleware) {
            var prevMiddleware = this.blockMiddleware;
            this.blockMiddleware = entry -> {
                var newEntry = prevMiddleware.apply(entry);
                if (matcher.matches(newEntry)) {
                    return middleware.apply(newEntry);
                }
                return newEntry;
            };
            return this;
        }

        public Builder itemMeta(ItemMiddleware.Matcher matcher, Function<PlantopiaItemMeta.MetaProperties, PlantopiaItemMeta.MetaProperties> refiner) {
            return itemMiddleware(matcher, entry -> entry.modifyMeta(refiner));
        }

        public Builder itemMeta(Function<PlantopiaItemMeta.MetaProperties, PlantopiaItemMeta.MetaProperties> refiner) {
            return itemMiddleware(entry -> true, entry -> entry.modifyMeta(refiner));
        }

        public Builder itemMiddleware(ItemMiddleware.Matcher matcher, ItemMiddleware middleware) {
            var prevMiddleware = this.itemMiddleware;
            this.itemMiddleware = entry -> {
                var newEntry = prevMiddleware.apply(entry);
                if (matcher.matches(newEntry)) {
                    return middleware.apply(newEntry);
                }
                return newEntry;
            };
            return this;
        }

        public Builder woodMapColor(MapColor color) {
            this.woodMapColor = state -> color;
            return this;
        }

        public Builder woodMapColor(Function<BlockState, MapColor> color) {
            this.woodMapColor = color;
            return this;
        }

        public Builder trunkMapColor(MapColor color) {
            this.trunkMapColor = state -> color;
            return this;
        }

        public Builder trunkMapColor(Function<BlockState, MapColor> color) {
            this.trunkMapColor = color;
            return this;
        }

        public Builder logMapColor(MapColor color) {
            this.logMapColor = state -> color;
            return this;
        }

        public Builder logMapColor(Function<BlockState, MapColor> color) {
            this.logMapColor = color;
            return this;
        }

        public Builder pressurePlateSensitivity(PressurePlateBlock.Sensitivity sensitivity) {
            this.pressurePlateSensitivity = sensitivity;
            return this;
        }

        public Builder buttonTicksToStayPressed(int ticks) {
            this.buttonTicksToStayPressed = ticks;
            return this;
        }

        public Builder arrowsCanPressButton() {
            this.canArrowsPressButton = true;
            return this;
        }

        public Builder arrowsCanNotPressButton() {
            this.canArrowsPressButton = false;
            return this;
        }
    }
}
