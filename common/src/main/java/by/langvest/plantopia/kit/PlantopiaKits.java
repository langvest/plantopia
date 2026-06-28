package by.langvest.plantopia.kit;

import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.kit.special.PlantopiaJacarandaKit;
import by.langvest.plantopia.kit.special.PlantopiaMapleKit;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.property.PlantopiaOrderType;
import by.langvest.toolkit.event.RegisterEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class PlantopiaKits {
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
            .apply(PlantopiaKits::addCherrySounds)
            .build()
    );

    public static void setup(@NotNull RegisterEvent event) {}

    /* HELPER METHODS ***********************************************************************************/

    private static void addCherrySounds(PlantopiaTreeKitConfiguration.@NotNull Builder builder) {
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
    private static @NotNull Function<PlantopiaBlockMeta.MetaType, Boolean> leavesSelector() {
        return metaType -> metaType.instanceOf(PlantopiaBlockMeta.MetaType.LEAVES);
    }

    @Contract(pure = true)
    private static @NotNull Function<PlantopiaBlockMeta.MetaType, Boolean> saplingSelector() {
        return metaType -> metaType.instanceOf(PlantopiaBlockMeta.MetaType.SAPLING);
    }

    @Contract(pure = true)
    private static @NotNull Function<PlantopiaBlockMeta.MetaType, Boolean> woodFamilySelector() {
        return PlantopiaBlockMeta.MetaType::isWoodFamilyLike;
    }
}
