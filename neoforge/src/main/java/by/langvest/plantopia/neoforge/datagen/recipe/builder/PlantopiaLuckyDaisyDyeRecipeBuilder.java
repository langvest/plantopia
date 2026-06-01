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

import java.util.LinkedHashMap;
import java.util.Map;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.locationOf;

public class PlantopiaLuckyDaisyDyeRecipeBuilder extends CraftingRecipeBuilder implements RecipeBuilder {
    protected final RecipeCategory category = RecipeCategory.MISC;
    protected final Ingredient daisy;
    protected final Item result;
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    protected @Nullable String group;
    protected boolean showNotification = true;

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
        criteria.put(criterionName, criterion);
        return this;
    }

    @Override
    public @NotNull PlantopiaLuckyDaisyDyeRecipeBuilder group(@Nullable String groupName) {
        group = groupName;
        return this;
    }

    public PlantopiaLuckyDaisyDyeRecipeBuilder showNotification(boolean showNotification) {
        this.showNotification = showNotification;
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return result;
    }

    @Override
    public void save(@NotNull RecipeOutput recipeOutput, @NotNull ResourceLocation recipeId) {
        ensureValid(recipeId);

        var advancementBuilder = recipeOutput.advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
            .rewards(AdvancementRewards.Builder.recipe(recipeId))
            .requirements(AdvancementRequirements.Strategy.OR);

        criteria.forEach(advancementBuilder::addCriterion);

        recipeOutput.accept(
            new PlantopiaLuckyDaisyDyeRecipeBuilder.Result(
                recipeId,
                result,
                group == null ? "" : group,
                determineBookCategory(category),
                daisy,
                advancementBuilder.build(recipeId.withPrefix("recipes/" + category.getFolderName() + "/")),
                showNotification
            )
        );
    }

    private void ensureValid(ResourceLocation id) {
        if (criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    public static class Result extends CraftingRecipeBuilder.CraftingResult {
        private final ResourceLocation id;
        private final Item result;
        private final String group;
        private final Ingredient daisy;
        private final AdvancementHolder advancement;
        private final boolean showNotification;

        public Result(
            ResourceLocation id,
            Item result,
            String group,
            CraftingBookCategory category,
            Ingredient daisy,
            AdvancementHolder advancement,
            boolean showNotification
        ) {
            super(category);
            this.id = id;
            this.result = result;
            this.group = group;
            this.daisy = daisy;
            this.advancement = advancement;
            this.showNotification = showNotification;
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
            json.addProperty("show_notification", showNotification);
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