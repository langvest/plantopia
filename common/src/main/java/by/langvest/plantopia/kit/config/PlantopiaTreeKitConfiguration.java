package by.langvest.plantopia.kit.config;

import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import by.langvest.plantopia.meta.property.PlantopiaOrderType;
import by.langvest.toolkit.meta.MetaAccessor;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public record PlantopiaTreeKitConfiguration(
    MapColor plankMapColor,
    MapColor trunkMapColor,
    MapColor strippedTrunkMapColor,
    PressurePlateBlock.Sensitivity pressurePlateSensitivity,
    int buttonTicksToStayPressed,
    boolean canArrowsPressButton,
    ResourceKey<Level> dimensionType,
    Function<PlantopiaBlockMeta.MetaProperties, PlantopiaBlockMeta.MetaProperties> blockMetaModifier,
    Function<PlantopiaItemMeta.MetaProperties, PlantopiaItemMeta.MetaProperties> itemMetaModifier,
    Function<ResourceLocation, BlockSetType> blockSetTypeFactory,
    Function<Pair<ResourceLocation, BlockSetType>, WoodType> woodTypeFactory
) {
    @Contract(pure = true)
    public @NotNull Function<BlockState, MapColor> logMapColor() {
        return state -> state.getValue(BlockStateProperties.AXIS).isVertical() ? plankMapColor : trunkMapColor;
    }

    @Contract(pure = true)
    public @NotNull Function<BlockState, MapColor> strippedLogMapColor() {
        return state -> state.getValue(BlockStateProperties.AXIS).isVertical() ? plankMapColor : strippedTrunkMapColor;
    }

    public PlantopiaBlockMeta.MetaProperties applyMeta(PlantopiaBlockMeta.MetaType metaType) {
        return applyMeta(PlantopiaBlockMeta.MetaProperties.of(metaType));
    }

    public PlantopiaBlockMeta.MetaProperties applyMeta(PlantopiaBlockMeta.MetaProperties metaProperties) {
        return blockMetaModifier.apply(metaProperties);
    }

    public PlantopiaItemMeta.MetaProperties applyMeta(PlantopiaItemMeta.MetaType metaType) {
        return applyMeta(PlantopiaItemMeta.MetaProperties.of(metaType));
    }

    public PlantopiaItemMeta.MetaProperties applyMeta(PlantopiaItemMeta.MetaProperties metaProperties) {
        return itemMetaModifier.apply(metaProperties);
    }

    @Contract(" -> new")
    public static @NotNull Builder builder() {
        return new Builder();
    }

    /**
     * Builder for creating {@link PlantopiaTreeKitConfiguration} instances.
     */
    public static class Builder {
        private MapColor plankMapColor = MapColor.WOOD;
        private MapColor trunkMapColor = MapColor.PODZOL;
        private MapColor strippedTrunkMapColor = MapColor.WOOD;
        private PressurePlateBlock.Sensitivity pressurePlateSensitivity = PressurePlateBlock.Sensitivity.EVERYTHING;
        private int buttonTicksToStayPressed = 30;
        private boolean canArrowsPressButton = true;
        private ResourceKey<Level> dimensionType = Level.OVERWORLD;
        Function<PlantopiaBlockMeta.MetaProperties, PlantopiaBlockMeta.MetaProperties> blockMetaModifier = metaProperties -> metaProperties;
        Function<PlantopiaItemMeta.MetaProperties, PlantopiaItemMeta.MetaProperties> itemMetaModifier = metaProperties -> metaProperties;
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
                plankMapColor,
                trunkMapColor,
                strippedTrunkMapColor,
                pressurePlateSensitivity,
                buttonTicksToStayPressed,
                canArrowsPressButton,
                dimensionType,
                blockMetaModifier,
                itemMetaModifier,
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

        public Builder referType(WoodType woodType) {
            return woodType(woodType).blockSetType(woodType.setType());
        }

        public Builder referType(WoodType woodType, BlockSetType blockSetType) {
            return woodType(woodType).blockSetType(blockSetType);
        }

        public Builder orderType(PlantopiaOrderType orderType) {
            return blockMeta(metaProperties -> metaProperties.order(orderType))
                .itemMeta(metaProperties -> metaProperties.order(orderType));
        }

        public Builder blockMeta(Function<PlantopiaBlockMeta.MetaProperties, PlantopiaBlockMeta.MetaProperties> modifier) {
            return blockMeta(metaType -> true, modifier);
        }

        public Builder blockMeta(Predicate<PlantopiaBlockMeta.MetaType> predicate, Function<PlantopiaBlockMeta.MetaProperties, PlantopiaBlockMeta.MetaProperties> modifier) {
            var prevModifier = this.blockMetaModifier;
            this.blockMetaModifier = metaProperties -> {
                var newMetaProperties = prevModifier.apply(metaProperties);
                if (predicate.test(MetaAccessor.getMetaTypeFrom(newMetaProperties))) {
                    return modifier.apply(newMetaProperties);
                }
                return newMetaProperties;
            };
            return this;
        }

        public Builder itemMeta(Function<PlantopiaItemMeta.MetaProperties, PlantopiaItemMeta.MetaProperties> modifier) {
            return itemMeta(metaType -> true, modifier);
        }

        public Builder itemMeta(Predicate<PlantopiaItemMeta.MetaType> predicate, Function<PlantopiaItemMeta.MetaProperties, PlantopiaItemMeta.MetaProperties> modifier) {
            var prevModifier = this.itemMetaModifier;
            this.itemMetaModifier = metaProperties -> {
                var newMetaProperties = prevModifier.apply(metaProperties);
                if (predicate.test(MetaAccessor.getMetaTypeFrom(newMetaProperties))) {
                    return modifier.apply(newMetaProperties);
                }
                return newMetaProperties;
            };
            return this;
        }

        public Builder mapColors(MapColor plankColor, MapColor trunkColor) {
            return mapColors(plankColor, trunkColor, plankColor);
        }

        public Builder mapColors(MapColor plankColor, MapColor trunkColor, MapColor strippedTrunkColor) {
            return plankMapColor(plankColor).trunkMapColor(trunkColor).strippedTrunkMapColor(strippedTrunkColor);
        }

        public Builder plankMapColor(MapColor color) {
            this.plankMapColor = color;
            return this;
        }

        public Builder trunkMapColor(MapColor color) {
            this.trunkMapColor = color;
            return this;
        }

        public Builder strippedTrunkMapColor(MapColor color) {
            this.strippedTrunkMapColor = color;
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
