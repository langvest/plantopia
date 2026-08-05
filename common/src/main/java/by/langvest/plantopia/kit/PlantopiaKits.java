package by.langvest.plantopia.kit;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.kit.tree.birch.PlantopiaBirchKit;
import by.langvest.plantopia.kit.tree.deadwood.PlantopiaDeadwoodKit;
import by.langvest.plantopia.kit.tree.fir.PlantopiaFirKit;
import by.langvest.plantopia.kit.tree.jacaranda.PlantopiaJacarandaKit;
import by.langvest.plantopia.kit.tree.maple.PlantopiaMapleKit;
import by.langvest.plantopia.kit.tree.oak.PlantopiaOakKit;
import by.langvest.plantopia.kit.tree.palm.PlantopiaPalmKit;
import by.langvest.plantopia.kit.tree.spruce.PlantopiaSpruceKit;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.property.PlantopiaOrderType;
import by.langvest.plantopia.util.PlantopiaDictionary;
import by.langvest.toolkit.event.RegisterEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaKits {
    public static final PlantopiaBirchKit BIRCH = new PlantopiaBirchKit(
        PlantopiaDictionary.BIRCH,
        PlantopiaTreeKitConfiguration.builder()
            .orderType(PlantopiaOrderType.BIRCH)
            .build()
    );

    public static final PlantopiaOakKit OAK = new PlantopiaOakKit(
        PlantopiaDictionary.OAK,
        PlantopiaTreeKitConfiguration.builder()
            .orderType(PlantopiaOrderType.OAK)
            .build()
    );

    public static final PlantopiaSpruceKit SPRUCE = new PlantopiaSpruceKit(
        PlantopiaDictionary.SPRUCE,
        PlantopiaTreeKitConfiguration.builder()
            .orderType(PlantopiaOrderType.SPRUCE)
            .build()
    );

    public static final PlantopiaMapleKit MAPLE = new PlantopiaMapleKit(
        PlantopiaDictionary.MAPLE,
        PlantopiaTreeKitConfiguration.builder()
            .orderType(PlantopiaOrderType.MAPLE)
            .apply(balksGoesAfterWood(PlantopiaDictionary.MAPLE))
            .build()
    );

    public static final PlantopiaJacarandaKit JACARANDA = new PlantopiaJacarandaKit(
        PlantopiaDictionary.JACARANDA,
        PlantopiaTreeKitConfiguration.builder()
            .orderType(PlantopiaOrderType.JACARANDA)
            .apply(PlantopiaKits::cherrySounds)
            .build()
    );

    public static final PlantopiaDeadwoodKit DEADWOOD = new PlantopiaDeadwoodKit(
        PlantopiaDictionary.DEADWOOD,
        PlantopiaTreeKitConfiguration.builder()
            .orderType(PlantopiaOrderType.DEADWOOD)
            .build()
    );

    public static final PlantopiaPalmKit PALM = new PlantopiaPalmKit(
        PlantopiaDictionary.PALM,
        PlantopiaTreeKitConfiguration.builder()
            .orderType(PlantopiaOrderType.PALM)
            .build()
    );

    public static final PlantopiaFirKit FIR = new PlantopiaFirKit(
        PlantopiaDictionary.FIR,
        PlantopiaTreeKitConfiguration.builder()
            .orderType(PlantopiaOrderType.FIR)
            .build()
    );

    public static void setup(@NotNull RegisterEvent event) {}

    /* HELPER METHODS ***********************************************************************************/

    @Contract(pure = true)
    private static @NotNull Consumer<PlantopiaTreeKitConfiguration.Builder> balksGoesAfterWood(String baseName) {
        var supposedWood = PlantopiaBlocks.supposeBlock(baseName + "_wood");
        var supposedStrippedWood = PlantopiaBlocks.supposeBlock("stripped_" + baseName + "_wood");
        return balksGoesAfterWood(supposedWood, supposedStrippedWood);
    }

    @Contract(pure = true)
    private static @NotNull Consumer<PlantopiaTreeKitConfiguration.Builder> balksGoesAfterWood(Supplier<Block> wood, Supplier<Block> strippedWood) {
        return builder -> builder.blockMiddleware(
            entry -> entry.metaType().instanceOf(PlantopiaBlockMeta.MetaType.BALK),
            entry -> {
                boolean isStripped = entry.name.contains("stripped_");
                var target = isStripped ? strippedWood : wood;
                return entry.modifyMeta(metaProperties -> metaProperties.goesAfter(target));
            }
        );
    }

    private static void cherrySounds(PlantopiaTreeKitConfiguration.Builder builder) {
        builder.blockSetType(copyBlockSetType(BlockSetType.CHERRY));
        builder.blockMeta(leavesMatcher(), metaProperties -> metaProperties.sound(SoundType.CHERRY_LEAVES));
        builder.blockMeta(saplingMatcher(), metaProperties -> metaProperties.sound(SoundType.CHERRY_SAPLING));
        builder.blockMeta(woodFamilyMatcher(), metaProperties -> metaProperties.sound(SoundType.CHERRY_WOOD));
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
    private static @NotNull PlantopiaTreeKitConfiguration.BlockMiddleware.Matcher leavesMatcher() {
        return entry -> entry.metaType().instanceOf(PlantopiaBlockMeta.MetaType.LEAVES);
    }

    @Contract(pure = true)
    private static @NotNull PlantopiaTreeKitConfiguration.BlockMiddleware.Matcher saplingMatcher() {
        return entry -> entry.metaType().instanceOf(PlantopiaBlockMeta.MetaType.SAPLING);
    }

    @Contract(pure = true)
    private static @NotNull PlantopiaTreeKitConfiguration.BlockMiddleware.Matcher woodFamilyMatcher() {
        return entry -> entry.metaType().isWoodFamilyLike();
    }
}
