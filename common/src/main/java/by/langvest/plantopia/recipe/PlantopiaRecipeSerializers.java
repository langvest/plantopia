package by.langvest.plantopia.recipe;

import by.langvest.plantopia.recipe.special.PlantopiaLuckyDaisyDyeRecipe;
import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaRecipeSerializers {
    public static final RegistryObject<RecipeSerializer<? extends CraftingRecipe>> LUCKY_DAISY_DYE_SERIALIZER = registerSerializer("crafting_lucky_daisy_dye", PlantopiaLuckyDaisyDyeRecipe.Serializer::new);

    private static <T extends RecipeSerializer<? extends CraftingRecipe>> RegistryObject<T> registerSerializer(String name, Supplier<T> supplier) {
        return PlantopiaRegistries.RECIPE_SERIALIZER.register(plantopia(name), supplier);
    }

    public static void setup(@NotNull RegisterEvent event) {
        event.registerAll(Registries.RECIPE_SERIALIZER, PlantopiaRegistries.RECIPE_SERIALIZER);
    }
}
