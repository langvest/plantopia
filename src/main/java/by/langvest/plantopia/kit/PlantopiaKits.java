package by.langvest.plantopia.kit;

import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.kit.special.PlantopiaMapleKit;
import by.langvest.plantopia.meta.property.PlantopiaOrderType;
import by.langvest.toolkit.event.RegisterEvent;
import org.jetbrains.annotations.NotNull;

public class PlantopiaKits {
    public static final PlantopiaMapleKit MAPLE = new PlantopiaMapleKit(
        "maple",
        PlantopiaTreeKitConfiguration.builder()
            .orderType(PlantopiaOrderType.MAPLE)
            .build()
    );

    public static void setup(@NotNull RegisterEvent event) {}
}
