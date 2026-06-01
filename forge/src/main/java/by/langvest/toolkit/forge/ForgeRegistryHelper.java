package by.langvest.toolkit.forge;

import by.langvest.toolkit.platform.Platform;
import by.langvest.toolkit.platform.RegistryHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.function.Supplier;

public class ForgeRegistryHelper extends RegistryHelper {
    public ForgeRegistryHelper(Platform platform) {
        super(platform);
    }

    @Override
    public void registerBrewable(Potion inputPotion, @NotNull ItemLike ingredient, Potion outputPotion) {
        BrewingRecipeRegistry.addRecipe(new IBrewingRecipe() {
            @Override
            public boolean isInput(@NotNull ItemStack inputStack) {
                return !inputStack.isEmpty() && PotionUtils.getPotion(inputStack) == inputPotion;
            }

            @Override
            public boolean isIngredient(@NotNull ItemStack ingredientStack) {
                return !ingredientStack.isEmpty() && ingredientStack.is(ingredient.asItem());
            }

            @Override
            public @NotNull ItemStack getOutput(@NotNull ItemStack inputStack, @NotNull ItemStack ingredientStack) {
                if (isInput(inputStack) && isIngredient(ingredientStack)) {
                    return PotionUtils.setPotion(inputStack.copy(), outputPotion);
                }

                return ItemStack.EMPTY;
            }
        });
    }

    @Override
    protected void addKnownRegistries(Map<ResourceLocation, RegistryEntry> registries) {
        super.addKnownRegistries(registries);
        addForgeRegistries(registries);
    }

    protected void addForgeRegistries(Map<ResourceLocation, RegistryEntry> registries) {

    }
}
