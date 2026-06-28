package by.langvest.toolkit.fabric;

import by.langvest.toolkit.platform.Platform;
import by.langvest.toolkit.platform.RegistryHelper;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class FabricRegistryHelper extends RegistryHelper {
    public FabricRegistryHelper(Platform platform) {
        super(platform);
    }

    @Override
    public void registerBrewable(Potion inputPotion, @NotNull ItemLike ingredient, Potion outputPotion) {
        FabricBrewingRecipeRegistry.registerPotionRecipe(inputPotion, Ingredient.of(ingredient), outputPotion);
    }

    @Override
    public void registerPottable(Block plantBlock, Block pottedBlock) {
        FlowerPotBlock.POTTED_BY_CONTENT.put(plantBlock, pottedBlock);
    }

    @Override
    protected void addKnownRegistries(Map<ResourceLocation, RegistryEntry> registries) {
        super.addKnownRegistries(registries);
        addFabricRegistries(registries);
    }

    protected void addFabricRegistries(Map<ResourceLocation, RegistryEntry> registries) {}
}
