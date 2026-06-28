package by.langvest.plantopia.client.gui;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public record PlantopiaHeartType(
    ResourceLocation full,
    ResourceLocation fullBlinking,
    ResourceLocation half,
    ResourceLocation halfBlinking,
    ResourceLocation hardcoreFull,
    ResourceLocation hardcoreFullBlinking,
    ResourceLocation hardcoreHalf,
    ResourceLocation hardcoreHalfBlinking
) {
    @Contract("_ -> new")
    public static @NotNull PlantopiaHeartType create(String name) {
        return new PlantopiaHeartType(
            plantopia("hud/heart/" + name + "_full"),
            plantopia("hud/heart/" + name + "_full_blinking"),
            plantopia("hud/heart/" + name + "_half"),
            plantopia("hud/heart/" + name + "_half_blinking"),
            plantopia("hud/heart/" + name + "_hardcore_full"),
            plantopia("hud/heart/" + name + "_hardcore_full_blinking"),
            plantopia("hud/heart/" + name + "_hardcore_half"),
            plantopia("hud/heart/" + name + "_hardcore_half_blinking")
        );
    }

    public ResourceLocation getSprite(boolean hardcore, boolean halfHeart, boolean blinking) {
        if (!hardcore) {
            if (halfHeart) {
                return blinking ? halfBlinking : half;
            } else {
                return blinking ? fullBlinking : full;
            }
        } else if (halfHeart) {
            return blinking ? hardcoreHalfBlinking : hardcoreHalf;
        } else {
            return blinking ? hardcoreFullBlinking : hardcoreFull;
        }
    }
}
