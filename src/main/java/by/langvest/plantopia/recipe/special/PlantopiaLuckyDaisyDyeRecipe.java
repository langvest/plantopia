package by.langvest.plantopia.recipe.special;

import by.langvest.plantopia.block.special.PlantopiaLuckyDaisyBlock;
import by.langvest.plantopia.recipe.PlantopiaRecipeSerializers;
import by.langvest.plantopia.util.helper.PlantopiaItemHelper;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PlantopiaLuckyDaisyDyeRecipe implements CraftingRecipe {
    private final ResourceLocation id;
    private final String group;
    private final CraftingBookCategory category;
    private final Ingredient daisy;
    private final ItemStack result;

    public PlantopiaLuckyDaisyDyeRecipe(ResourceLocation id, String group, CraftingBookCategory category, ItemStack result, Ingredient daisy) {
        this.id = id;
        this.group = group;
        this.category = category;
        this.daisy = daisy;
        this.result = result;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return PlantopiaRecipeSerializers.LUCKY_DAISY_DYE_SERIALIZER.get();
    }

    @Override
    public @NotNull String getGroup() {
        return group;
    }

    @Override
    public @NotNull CraftingBookCategory category() {
        return category;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return result;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, daisy);
    }

    @Override
    public boolean matches(@NotNull CraftingContainer container, @NotNull Level level) {
        ItemStack daisyStack = ItemStack.EMPTY;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stackInSlot = container.getItem(i);
            if (!stackInSlot.isEmpty()) {
                if (daisy.test(stackInSlot)) {
                    if (!daisyStack.isEmpty()) {
                        return false; // Found more than one daisy
                    }
                    daisyStack = stackInSlot;
                } else {
                    return false; // Found an item that is not our target daisy
                }
            }
        }
        return !daisyStack.isEmpty();
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer container, @NotNull RegistryAccess registryAccess) {
        ItemStack daisyStack = findLuckyDaisy(container);
        if (daisyStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        var blockState = PlantopiaItemHelper.getBlockStateFrom(daisyStack);
        if (blockState != null && blockState.hasProperty(PlantopiaLuckyDaisyBlock.AMOUNT)) {
            if (blockState.getValue(PlantopiaLuckyDaisyBlock.AMOUNT) == PlantopiaLuckyDaisyBlock.MIN_PETALS) {
                return Items.YELLOW_DYE.getDefaultInstance();
            }
        }

        return result.copy();
    }

    private ItemStack findLuckyDaisy(@NotNull CraftingContainer container) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stackInSlot = container.getItem(i);
            if (daisy.test(stackInSlot)) {
                return stackInSlot;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    public static class Serializer implements RecipeSerializer<PlantopiaLuckyDaisyDyeRecipe> {
        @Override
        public @NotNull PlantopiaLuckyDaisyDyeRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            var group = GsonHelper.getAsString(json, "group", "");
            var category = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
            var daisy = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "daisy"), false);
            var result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

            return new PlantopiaLuckyDaisyDyeRecipe(recipeId, group, category, result, daisy);
        }

        @Override
        public PlantopiaLuckyDaisyDyeRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            CraftingBookCategory category = buffer.readEnum(CraftingBookCategory.class);
            Ingredient daisy = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();

            return new PlantopiaLuckyDaisyDyeRecipe(recipeId, group, category, result, daisy);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull PlantopiaLuckyDaisyDyeRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeEnum(recipe.category);
            recipe.daisy.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}
