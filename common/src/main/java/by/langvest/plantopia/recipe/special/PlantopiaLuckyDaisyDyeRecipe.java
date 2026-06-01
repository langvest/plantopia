package by.langvest.plantopia.recipe.special;

import by.langvest.plantopia.block.special.PlantopiaLuckyDaisyBlock;
import by.langvest.plantopia.recipe.PlantopiaRecipeSerializers;
import by.langvest.plantopia.util.helper.PlantopiaItemHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaLuckyDaisyDyeRecipe implements CraftingRecipe {
    private final String group;
    private final CraftingBookCategory category;
    private final ItemStack result;
    private final Ingredient daisy;
    private final boolean showNotification;

    public PlantopiaLuckyDaisyDyeRecipe(String group, CraftingBookCategory category, ItemStack result, Ingredient daisy, boolean showNotification) {
        this.group = group;
        this.category = category;
        this.daisy = daisy;
        this.result = result;
        this.showNotification = showNotification;
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
    public @NotNull ItemStack getResultItem(RegistryAccess registryAccess) {
        return result;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, daisy);
    }

    @Override
    public boolean showNotification() {
        return showNotification;
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
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
    public @NotNull ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
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

    private ItemStack findLuckyDaisy(CraftingContainer container) {
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
        public static final Codec<PlantopiaLuckyDaisyDyeRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("group").orElse("").forGetter(it -> it.group),
            CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(it -> it.category),
            ItemStack.CODEC.fieldOf("result").forGetter(it -> it.result),
            Ingredient.CODEC.fieldOf("daisy").forGetter(it -> it.daisy),
            Codec.BOOL.fieldOf("show_notification").forGetter(it -> it.showNotification)
        ).apply(instance, PlantopiaLuckyDaisyDyeRecipe::new));

        @Override
        public @NotNull Codec<PlantopiaLuckyDaisyDyeRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull PlantopiaLuckyDaisyDyeRecipe fromNetwork(FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            CraftingBookCategory category = buffer.readEnum(CraftingBookCategory.class);
            Ingredient daisy = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            boolean showNotification = buffer.readBoolean();

            return new PlantopiaLuckyDaisyDyeRecipe(group, category, result, daisy, showNotification);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, PlantopiaLuckyDaisyDyeRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeEnum(recipe.category);
            recipe.daisy.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeBoolean(recipe.showNotification);
        }
    }
}
