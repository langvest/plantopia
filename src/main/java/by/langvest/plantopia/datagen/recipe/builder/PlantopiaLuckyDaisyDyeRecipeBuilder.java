package by.langvest.plantopia.datagen.recipe.builder;

import by.langvest.plantopia.recipe.PlantopiaRecipeSerializers;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.CraftingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.locationOf;

public class PlantopiaLuckyDaisyDyeRecipeBuilder  extends CraftingRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category = RecipeCategory.MISC;
    private final Ingredient daisy;
    private final Item result;
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    @Nullable
    private String group;

    public PlantopiaLuckyDaisyDyeRecipeBuilder(Ingredient daisy, @NotNull ItemLike result) {
        this.daisy = daisy;
        this.result = result.asItem();
    }

    @Contract("_, _ -> new")
    public static @NotNull PlantopiaLuckyDaisyDyeRecipeBuilder of(Ingredient daisy, ItemLike result) {
        return new PlantopiaLuckyDaisyDyeRecipeBuilder(daisy, result);
    }

    @Override
    public @NotNull PlantopiaLuckyDaisyDyeRecipeBuilder unlockedBy(@NotNull String criterionName, @NotNull CriterionTriggerInstance criterionTrigger) {
        advancement.addCriterion(criterionName, criterionTrigger);
        return this;
    }

    @Override
    public @NotNull PlantopiaLuckyDaisyDyeRecipeBuilder group(@Nullable String groupName) {
        group = groupName;
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return result;
    }

    @Override
    public void save(@NotNull Consumer<FinishedRecipe> finishedRecipeConsumer, @NotNull ResourceLocation recipeId) {
        ensureValid(recipeId);

        advancement
            .parent(ROOT_RECIPE_ADVANCEMENT)
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
            .rewards(AdvancementRewards.Builder.recipe(recipeId))
            .requirements(RequirementsStrategy.OR);

        finishedRecipeConsumer.accept(
            new PlantopiaLuckyDaisyDyeRecipeBuilder.Result(
                recipeId,
                result,
                group == null ? "" : group,
                determineBookCategory(category),
                daisy,
                advancement,
                recipeId.withPrefix("recipes/" + category.getFolderName() + "/")
            )
        );
    }

    private void ensureValid(ResourceLocation id) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    public static class Result extends CraftingRecipeBuilder.CraftingResult {
        private final ResourceLocation id;
        private final Item result;
        private final String group;
        private final Ingredient daisy;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, Item result, String group, CraftingBookCategory category, Ingredient daisy, Advancement.Builder advancement, ResourceLocation advancementId) {
            super(category);
            this.id = id;
            this.result = result;
            this.group = group;
            this.daisy = daisy;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            super.serializeRecipeData(json);

            if (!group.isEmpty()) {
                json.addProperty("group", group);
            }

            json.add("daisy", daisy.toJson());

            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("item", locationOf(result).toString());

            json.add("result", jsonobject);
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return id;
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return PlantopiaRecipeSerializers.LUCKY_DAISY_DYE_SERIALIZER.get();
        }

        @Override
        public @Nullable JsonObject serializeAdvancement() {
            return advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return advancementId;
        }
    }
}