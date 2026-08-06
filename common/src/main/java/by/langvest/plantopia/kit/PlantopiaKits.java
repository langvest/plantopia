package by.langvest.plantopia.kit;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.kit.special.PlantopiaExtraVanillaTreeKit;
import by.langvest.plantopia.kit.special.PlantopiaExtraVanillaBirchKit;
import by.langvest.plantopia.kit.tree.deadwood.PlantopiaDeadwoodKit;
import by.langvest.plantopia.kit.tree.fir.PlantopiaFirKit;
import by.langvest.plantopia.kit.tree.jacaranda.PlantopiaJacarandaKit;
import by.langvest.plantopia.kit.tree.maple.PlantopiaMapleKit;
import by.langvest.plantopia.kit.tree.palm.PlantopiaPalmKit;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.property.PlantopiaOrderType;
import by.langvest.plantopia.tab.PlantopiaCreativeModeTabs;
import by.langvest.toolkit.event.RegisterEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.minecraft;

@ParametersAreNonnullByDefault
public class PlantopiaKits {
    public static final PlantopiaExtraVanillaTreeKit OAK = createExtraVanillaTreeKit(
        "oak",
        PlantopiaTreeKitConfiguration.builder()
            .mapColors(MapColor.WOOD, MapColor.PODZOL)
            .apply(PlantopiaKits::directBalksIntoBuildingBlocksGroup)
            .build()
    );

    public static final PlantopiaExtraVanillaTreeKit DARK_OAK = createExtraVanillaTreeKit(
        "dark_oak",
        PlantopiaTreeKitConfiguration.builder()
            .mapColors(MapColor.COLOR_BROWN, MapColor.COLOR_BROWN)
            .apply(PlantopiaKits::directBalksIntoBuildingBlocksGroup)
            .build()
    );

    public static final PlantopiaExtraVanillaTreeKit SPRUCE = createExtraVanillaTreeKit(
        "spruce",
        PlantopiaTreeKitConfiguration.builder()
            .mapColors(MapColor.PODZOL, MapColor.COLOR_BROWN)
            .apply(PlantopiaKits::directBalksIntoBuildingBlocksGroup)
            .build()
    );

    public static final PlantopiaExtraVanillaTreeKit ACACIA = createExtraVanillaTreeKit(
        "acacia",
        PlantopiaTreeKitConfiguration.builder()
            .mapColors(MapColor.COLOR_ORANGE, MapColor.STONE)
            .apply(PlantopiaKits::directBalksIntoBuildingBlocksGroup)
            .build()
    );

    public static final PlantopiaExtraVanillaTreeKit JUNGLE = createExtraVanillaTreeKit(
        "jungle",
        PlantopiaTreeKitConfiguration.builder()
            .mapColors(MapColor.DIRT, MapColor.PODZOL)
            .apply(PlantopiaKits::directBalksIntoBuildingBlocksGroup)
            .build()
    );

    public static final PlantopiaExtraVanillaTreeKit MANGROVE = createExtraVanillaTreeKit(
        "mangrove",
        PlantopiaTreeKitConfiguration.builder()
            .mapColors(MapColor.COLOR_RED, MapColor.PODZOL)
            .apply(PlantopiaKits::directBalksIntoBuildingBlocksGroup)
            .build()
    );

    public static final PlantopiaExtraVanillaTreeKit CHERRY = createExtraVanillaTreeKit(
        "cherry",
        PlantopiaTreeKitConfiguration.builder()
            .mapColors(MapColor.TERRACOTTA_WHITE, MapColor.TERRACOTTA_GRAY)
            .apply(PlantopiaKits::directBalksIntoBuildingBlocksGroup)
            .apply(PlantopiaKits::cherrySounds)
            .blockSetType(BlockSetType.CHERRY) // LanGvest: Override blockSetType from cherrySounds to reuse the vanilla instance.
            .build()
    );

    public static final PlantopiaExtraVanillaBirchKit BIRCH = new PlantopiaExtraVanillaBirchKit(
        "birch",
        () -> Blocks.BIRCH_PLANKS,
        () -> Blocks.BIRCH_LOG,
        () -> Blocks.BIRCH_WOOD,
        () -> Blocks.STRIPPED_BIRCH_LOG,
        () -> Blocks.STRIPPED_BIRCH_WOOD,
        PlantopiaTreeKitConfiguration.builder()
            .orderType(PlantopiaOrderType.BIRCH)
            .mapColors(MapColor.SAND, MapColor.QUARTZ)
            .blockMeta(balkSelector(), metaProperties -> metaProperties.group(CreativeModeTabs.BUILDING_BLOCKS, PlantopiaCreativeModeTabs.MAIN))
            .build()
    );

    public static final PlantopiaMapleKit MAPLE = new PlantopiaMapleKit(
        "maple",
        PlantopiaTreeKitConfiguration.builder()
            .orderType(PlantopiaOrderType.MAPLE)
            .build()
    );

    public static final PlantopiaJacarandaKit JACARANDA = new PlantopiaJacarandaKit(
        "jacaranda",
        PlantopiaTreeKitConfiguration.builder()
            .orderType(PlantopiaOrderType.JACARANDA)
            .apply(PlantopiaKits::cherrySounds)
            .build()
    );

    public static final PlantopiaDeadwoodKit DEADWOOD = new PlantopiaDeadwoodKit(
        "deadwood",
        PlantopiaTreeKitConfiguration.builder()
            .orderType(PlantopiaOrderType.DEADWOOD)
            .build()
    );

    public static final PlantopiaPalmKit PALM = new PlantopiaPalmKit(
        "palm",
        PlantopiaTreeKitConfiguration.builder()
            .orderType(PlantopiaOrderType.PALM)
            .build()
    );

    public static final PlantopiaFirKit FIR = new PlantopiaFirKit(
        "fir",
        PlantopiaTreeKitConfiguration.builder()
            .orderType(PlantopiaOrderType.FIR)
            .build()
    );

    public static void setup(@NotNull RegisterEvent event) {}

    /* HELPER METHODS ***********************************************************************************/

    private static @NotNull PlantopiaExtraVanillaTreeKit createExtraVanillaTreeKit(String baseName, PlantopiaTreeKitConfiguration config) {
        var registryHelper = Plantopia.getPlatform().getRegistryHelper();
        var blockRegistry = registryHelper.getKnownRegistryOrThrow(Registries.BLOCK);

        return createExtraVanillaTreeKit(
            baseName,
            blockRegistry.getValueDelegate(minecraft(baseName + "_planks")),
            blockRegistry.getValueDelegate(minecraft(baseName + "_log")),
            blockRegistry.getValueDelegate(minecraft(baseName + "_wood")),
            blockRegistry.getValueDelegate(minecraft("stripped_" + baseName + "_log")),
            blockRegistry.getValueDelegate(minecraft("stripped_" + baseName + "_wood")),
            config
        );
    }

    private static @NotNull PlantopiaExtraVanillaTreeKit createExtraVanillaTreeKit(
        String baseName,
        Supplier<Block> planks,
        Supplier<Block> log,
        Supplier<Block> wood,
        Supplier<Block> strippedLog,
        Supplier<Block> strippedWood,
        PlantopiaTreeKitConfiguration config
    ) {
        return new PlantopiaExtraVanillaTreeKit(
            baseName,
            planks,
            log,
            wood,
            strippedLog,
            strippedWood,
            config
        );
    }

    private static void directBalksIntoBuildingBlocksGroup(PlantopiaTreeKitConfiguration.Builder builder) {
        builder.blockMeta(balkSelector(), metaProperties -> metaProperties.group(CreativeModeTabs.BUILDING_BLOCKS));
    }

    private static void cherrySounds(PlantopiaTreeKitConfiguration.Builder builder) {
        builder.blockSetType(copyBlockSetType(BlockSetType.CHERRY));
        builder.blockMeta(leavesSelector(), metaProperties -> metaProperties.sound(SoundType.CHERRY_LEAVES));
        builder.blockMeta(saplingSelector(), metaProperties -> metaProperties.sound(SoundType.CHERRY_SAPLING));
        builder.blockMeta(woodFamilySelector(), metaProperties -> metaProperties.sound(SoundType.CHERRY_WOOD));
    }

    @Contract(pure = true)
    private static @NotNull Function<ResourceLocation, BlockSetType> copyBlockSetType(BlockSetType sourceBlockSetType) {
        return identifier -> new BlockSetType(
            identifier.toString(),
            sourceBlockSetType.canOpenByHand(),
            sourceBlockSetType.soundType(),
            sourceBlockSetType.doorClose(),
            sourceBlockSetType.doorOpen(),
            sourceBlockSetType.trapdoorClose(),
            sourceBlockSetType.trapdoorOpen(),
            sourceBlockSetType.pressurePlateClickOff(),
            sourceBlockSetType.pressurePlateClickOn(),
            sourceBlockSetType.buttonClickOff(),
            sourceBlockSetType.buttonClickOn()
        );
    }

    @Contract(pure = true)
    private static @NotNull Predicate<PlantopiaBlockMeta.MetaType> balkSelector() {
        return metaType -> metaType.instanceOf(PlantopiaBlockMeta.MetaType.BALK);
    }

    @Contract(pure = true)
    private static @NotNull Predicate<PlantopiaBlockMeta.MetaType> leavesSelector() {
        return metaType -> metaType.instanceOf(PlantopiaBlockMeta.MetaType.LEAVES);
    }

    @Contract(pure = true)
    private static @NotNull Predicate<PlantopiaBlockMeta.MetaType> saplingSelector() {
        return metaType -> metaType.instanceOf(PlantopiaBlockMeta.MetaType.SAPLING);
    }

    @Contract(pure = true)
    private static @NotNull Predicate<PlantopiaBlockMeta.MetaType> woodFamilySelector() {
        return PlantopiaBlockMeta.MetaType::isWoodFamilyLike;
    }
}
