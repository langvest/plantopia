package by.langvest.plantopia.misc;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaItemPropertyTypes {
    public static final ResourceLocation PETAL_AMOUNT = createId("petal_amount");

    protected static @NotNull ResourceLocation createId(String name) {
        return plantopia(name);
    }
}
