package by.langvest.plantopia.neoforge.datagen.recipe.builder;

import by.langvest.plantopia.recipe.PlantopiaRecipeSerializers;
import com.google.gson.JsonObject;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.locationOf;

public class PlantopiaLuckyDaisyDyeRecipeBuilder extends CraftingRecipeBuilder implements RecipeBuilder {
    protected final RecipeCategory category = RecipeCategory.MISC;
    protected final Ingredient daisy;
    protected final Item result;
    protected final Advancement.Builder builder = Advancement.Builder.recipeAdvancement();
    @Nullable
    protected String group;
    protected boolean hasAtLeastOneCriteria = false;

    public PlantopiaLuckyDaisyDyeRecipeBuilder(Ingredient daisy, @NotNull ItemLike result) {
        this.daisy = daisy;
        this.result = result.asItem();
    }

    @Contract("_, _ -> new")
    public static @NotNull PlantopiaLuckyDaisyDyeRecipeBuilder of(Ingredient daisy, ItemLike result) {
        return new PlantopiaLuckyDaisyDyeRecipeBuilder(daisy, result);
    }

    @Override
    public @NotNull PlantopiaLuckyDaisyDyeRecipeBuilder unlockedBy(@NotNull String criterionName, @NotNull Criterion<?> criterion) {
        builder.addCriterion(criterionName, criterion);
        hasAtLeastOneCriteria = true;
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
    public void save(@NotNull RecipeOutput recipeOutput, @NotNull ResourceLocation recipeId) {
        ensureValid(recipeId);

        builder
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
            .rewards(AdvancementRewards.Builder.recipe(recipeId))
            .requirements(AdvancementRequirements.Strategy.OR);

        recipeOutput.accept(
            new PlantopiaLuckyDaisyDyeRecipeBuilder.Result(
                recipeId,
                result,
                group == null ? "" : group,
                determineBookCategory(category),
                daisy,
                builder,
                recipeId.withPrefix("recipes/" + category.getFolderName() + "/")
            )
        );
    }

    private void ensureValid(ResourceLocation id) {
        if (!hasAtLeastOneCriteria) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    public static class Result extends CraftingRecipeBuilder.CraftingResult {
        private final ResourceLocation id;
        private final Item result;
        private final String group;
        private final Ingredient daisy;
        private final AdvancementHolder advancement;

        public Result(ResourceLocation id, Item result, String group, CraftingBookCategory category, Ingredient daisy, Advancement.@NotNull Builder builder, ResourceLocation location) {
            super(category);
            this.id = id;
            this.result = result;
            this.group = group;
            this.daisy = daisy;
            this.advancement = builder.build(location);
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            super.serializeRecipeData(json);

            if (!group.isEmpty()) {
                json.addProperty("group", group);
            }

            json.add("daisy", daisy.toJson(false));

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("item", locationOf(result).toString());

            json.add("result", jsonObject);
        }

        @Override
        public @NotNull ResourceLocation id() {
            return id;
        }

        @Override
        public @NotNull RecipeSerializer<?> type() {
            return PlantopiaRecipeSerializers.LUCKY_DAISY_DYE_SERIALIZER.get();
        }

        @Override
        public @NotNull AdvancementHolder advancement() {
            return advancement;
        }
    }
}