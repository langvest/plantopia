package by.langvest.plantopia.kit.config;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import by.langvest.plantopia.meta.property.PlantopiaOrderType;
import by.langvest.toolkit.meta.MetaAccessor;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public record PlantopiaTreeKitConfiguration(
    Function<BlockState, MapColor> woodMapColor,
    Function<BlockState, MapColor> trunkMapColor,
    Function<BlockState, MapColor> logMapColor,
    PressurePlateBlock.Sensitivity pressurePlateSensitivity,
    int buttonTicksToStayPressed,
    boolean canArrowsPressButton,
    ResourceKey<Level> dimensionType,
    @Nullable PlantopiaOrderType orderType,
    Function<PlantopiaBlockMeta.MetaProperties, PlantopiaBlockMeta.MetaProperties> blockMetaFactory,
    Function<ResourceLocation, BlockSetType> blockSetTypeFactory,
    Function<Pair<ResourceLocation, BlockSetType>, WoodType> woodTypeFactory
) {
    public PlantopiaBlockMeta.MetaProperties applyBlockMeta(PlantopiaBlockMeta.MetaProperties metaProperties) {
        var newProperties = metaProperties;

        if (orderType != null) {
            newProperties = newProperties.order(orderType);
        }

        return blockMetaFactory.apply(newProperties);
    }

    public PlantopiaItemMeta.MetaProperties applyItemMeta(PlantopiaItemMeta.MetaProperties metaProperties) {
        var newProperties = metaProperties;

        if (orderType != null) {
            newProperties = newProperties.order(orderType);
        }

        return newProperties;
    }

    @Contract(" -> new")
    public static @NotNull Builder builder() {
        return new Builder();
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
        private @Nullable PlantopiaOrderType orderType = null;
        private Function<PlantopiaBlockMeta.MetaProperties, PlantopiaBlockMeta.MetaProperties> blockMetaFactory = metaProperties -> metaProperties;
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
            var workScheduler = Plantopia.getPlatform().getWorkScheduler();

            if (Plantopia.getPlatform().isClient()) {
                workScheduler.enqueueWork("client_setup", () -> Sheets.addWoodType(woodType));
            }

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
                orderType,
                blockMetaFactory,
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
            this.orderType = orderType;
            return this;
        }

        public Builder blockMeta(Function<PlantopiaBlockMeta.MetaProperties, PlantopiaBlockMeta.MetaProperties> refiner) {
            return blockMeta(metaType -> true, refiner);
        }

        public Builder blockMeta(Function<PlantopiaBlockMeta.MetaType, Boolean> selector, Function<PlantopiaBlockMeta.MetaProperties, PlantopiaBlockMeta.MetaProperties> refiner) {
            var prevBlockMetaFactory = this.blockMetaFactory;
            this.blockMetaFactory = metaProperties -> {
                var newProperties = prevBlockMetaFactory.apply(metaProperties);
                var metaType = MetaAccessor.getMetaTypeFrom(newProperties);
                if (selector.apply(metaType)) {
                    return refiner.apply(newProperties);
                }
                return newProperties;
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
