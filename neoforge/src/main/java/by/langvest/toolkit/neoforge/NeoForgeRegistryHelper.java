package by.langvest.toolkit.neoforge;

import by.langvest.toolkit.platform.Platform;
import by.langvest.toolkit.platform.RegistryHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.neoforge.common.brewing.BrewingRecipeRegistry;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class NeoForgeRegistryHelper extends RegistryHelper {
    public NeoForgeRegistryHelper(Platform platform) {
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
    public void registerPottable(Block plantBlock, Block pottedBlock) {
        FlowerPotBlock flowerPotBlock = (FlowerPotBlock) Blocks.FLOWER_POT;
        flowerPotBlock.addPlant(getResourceKeyOrThrow(plantBlock).location(), () -> pottedBlock);
    }

    @Override
    protected void addKnownRegistries(Map<ResourceLocation, RegistryEntry> registries) {
        super.addKnownRegistries(registries);
        addNeoForgeRegistries(registries);
    }

    protected void addNeoForgeRegistries(Map<ResourceLocation, RegistryEntry> registries) {}
}
