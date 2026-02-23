package by.langvest.plantopia.client.lang;

import by.langvest.plantopia.Plantopia;
import org.jetbrains.annotations.NotNull;

public class PlantopiaLangKey {
    public static final String TOOLTIP_RANDOM_VARIANT = createTooltipKey("random_variant");
    public static final String TOOLTIP_REMAINING_PETALS = createTooltipKey("remaining_petals");
    public static final String TOOLTIP_NO_PETALS = createTooltipKey("no_petals");

    public static @NotNull String createKey(String category, String ...path) {
        return category + "." + Plantopia.MOD_ID + "." + String.join(".", path);
    }

    public static @NotNull String createTooltipKey(String ...path) {
        return createKey("tooltip", path);
    }
}
