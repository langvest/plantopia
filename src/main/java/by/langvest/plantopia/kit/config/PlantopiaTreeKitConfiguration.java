package by.langvest.plantopia.kit.config;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import by.langvest.plantopia.meta.property.PlantopiaOrderType;
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
    Function<PlantopiaBlockMeta.MetaProperties, PlantopiaBlockMeta.MetaProperties> blockMetaPropertiesFactory,
    Function<ResourceLocation, BlockSetType> blockSetTypeFactory,
    Function<Pair<ResourceLocation, BlockSetType>, WoodType> woodTypeFactory
) {
    public PlantopiaBlockMeta.MetaProperties applyBlockMeta(PlantopiaBlockMeta.MetaProperties metaProperties) {
        var newProperties = metaProperties;

        if (orderType != null) {
            newProperties = newProperties.order(orderType);
        }

        return blockMetaPropertiesFactory.apply(newProperties);
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

    public static class Builder {
        private Function<BlockState, MapColor> woodMapColor = state -> MapColor.WOOD;
        private Function<BlockState, MapColor> trunkMapColor = state -> MapColor.PODZOL;
        private Function<BlockState, MapColor> logMapColor = state -> state.getValue(RotatedPillarBlock.AXIS).isVertical() ? woodMapColor.apply(state) : trunkMapColor.apply(state);
        private PressurePlateBlock.Sensitivity pressurePlateSensitivity = PressurePlateBlock.Sensitivity.EVERYTHING;
        private int buttonTicksToStayPressed = 30;
        private boolean canArrowsPressButton = true;
        private ResourceKey<Level> dimensionType = Level.OVERWORLD;
        private @Nullable PlantopiaOrderType orderType = null;
        private Function<PlantopiaBlockMeta.MetaProperties, PlantopiaBlockMeta.MetaProperties> blockMetaPropertiesFactory = metaProperties -> metaProperties;
        private Function<ResourceLocation, BlockSetType> blockSetTypeFactory = identifier -> BlockSetType.register(new BlockSetType(identifier.toString()));
        private Function<Pair<ResourceLocation, BlockSetType>, WoodType> woodTypeFactory = pair -> {
            var identifier = pair.getFirst();
            var woodType = WoodType.register(new WoodType(identifier.toString(), pair.getSecond()));
            var workScheduler = Plantopia.getPlatform().getWorkScheduler();

            if (Plantopia.getPlatform().isClient()) {
                workScheduler.enqueueWork("client_setup", () -> Sheets.addWoodType(woodType));
            }

            return woodType;
        };

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
                blockMetaPropertiesFactory,
                blockSetTypeFactory,
                woodTypeFactory
            );
        }

        public Builder dimensionType(ResourceKey<Level> dimensionType) {
            this.dimensionType = dimensionType;
            return this;
        }

        public Builder woodType(WoodType woodType) {
            this.woodTypeFactory = pair -> woodType;
            return this;
        }

        public Builder woodType(Function<Pair<ResourceLocation, BlockSetType>, WoodType> factory) {
            this.woodTypeFactory = factory;
            return this;
        }

        public Builder blockSetType(BlockSetType blockSetType) {
            this.blockSetTypeFactory = baseName -> blockSetType;
            return this;
        }

        public Builder blockSetType(Function<ResourceLocation, BlockSetType> factory) {
            this.blockSetTypeFactory = factory;
            return this;
        }

        public Builder orderType(PlantopiaOrderType orderType) {
            this.orderType = orderType;
            return this;
        }

        public Builder metaProperties(Function<PlantopiaBlockMeta.MetaProperties, PlantopiaBlockMeta.MetaProperties> factory) {
            this.blockMetaPropertiesFactory = factory;
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
