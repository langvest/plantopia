package by.langvest.plantopia.neoforge.datagen.recipe;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.event.PlantopiaDatagenBridgeEvent;
import by.langvest.plantopia.neoforge.datagen.recipe.builder.PlantopiaLuckyDaisyDyeRecipeBuilder;
import by.langvest.plantopia.kit.PlantopiaKits;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.toolkit.platform.EventEmitter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.*;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;

public class PlantopiaRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static RecipeOutput output;
    private final PlantopiaDatagenBridgeEvent.RecipeEvent.Bridge bridge;

    public PlantopiaRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup, @NotNull EventEmitter eventEmitter) {
        super(output, registryLookup);
        this.bridge = createBridge();
        eventEmitter.subscribe(this::listenBridge);
    }

    protected PlantopiaDatagenBridgeEvent.RecipeEvent.Bridge createBridge() {
        return new PlantopiaDatagenBridgeEvent.RecipeEvent.Bridge() {
            @Override
            public void planksFromLogs(ItemLike planks, TagKey<Item> logsTag, int count) {
                PlantopiaRecipeProvider.planksFromLogs(planks, logsTag, count);
            }

            @Override
            public void balksFromLogs(ItemLike balk, ItemLike log, int count) {
                PlantopiaRecipeProvider.balksFromLogs(balk, log, count);
            }

            @Override
            public void woodFromLogs(ItemLike wood, ItemLike log) {
                PlantopiaRecipeProvider.woodFromLogs(wood, log);
            }

            @Override
            public void woodenBoat(ItemLike boat, ItemLike planks) {
                PlantopiaRecipeProvider.woodenBoat(boat, planks);
            }

            @Override
            public void chestBoat(ItemLike chestBoat, ItemLike boat) {
                PlantopiaRecipeProvider.chestBoat(chestBoat, boat);
            }

            @Override
            public void hangingSign(ItemLike hangingSign, ItemLike strippedLog) {
                PlantopiaRecipeProvider.hangingSign(hangingSign, strippedLog);
            }

            @Override
            public void blockFamily(BlockFamily blockFamily) {
                PlantopiaRecipeProvider.blockFamily(blockFamily);
            }
        };
    }

    protected void listenBridge(PlantopiaDatagenBridgeEvent.@NotNull RecipeEvent event) {
        event.provide(bridge);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        setRecipeOutput(output);
        generateAll();

        fullBlockRecipe(RecipeCategory.BUILDING_BLOCKS, Blocks.COBBLESTONE, PlantopiaBlocks.COBBLESTONE_SHARD.get());
        fullBlockRecipe(RecipeCategory.BUILDING_BLOCKS, Blocks.MOSSY_COBBLESTONE, PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD.get());

        stonecutterRecipe(RecipeCategory.MISC, PlantopiaBlocks.COBBLESTONE_SHARD.get(), Blocks.COBBLESTONE, 9);
        stonecutterRecipe(RecipeCategory.MISC, PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD.get(), Blocks.MOSSY_COBBLESTONE, 9);

        smeltingRecipe(RecipeCategory.MISC, Items.GREEN_DYE, PlantopiaBlocks.TINY_CACTUS.get(), 1.0F, 200);
        smeltingRecipe(RecipeCategory.MISC, Items.PINK_DYE, PlantopiaBlocks.FLOWERING_TINY_CACTUS.get(), 1.0F, 200);
        smeltingRecipe(RecipeCategory.MISC, Items.BROWN_DYE, PlantopiaBlocks.PINE_CONE.get(), 1.0F, 200);
        smeltingRecipe(RecipeCategory.MISC, Items.LIME_DYE, PlantopiaBlocks.BIRCH_CATKIN.get(), 1.0F, 200);

        woodFromLogs(PlantopiaBlocks.BIRCH_BASE_WOOD.get(), PlantopiaBlocks.BIRCH_BASE_LOG.get());

        leafLitterRecipe(PlantopiaBlocks.YELLOW_LEAF_LITTER.get(), PlantopiaKits.MAPLE.yellowLeaves.get());
        leafLitterRecipe(PlantopiaBlocks.ORANGE_LEAF_LITTER.get(), PlantopiaKits.MAPLE.orangeLeaves.get());
        leafLitterRecipe(PlantopiaBlocks.RED_LEAF_LITTER.get(), PlantopiaKits.MAPLE.redLeaves.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PlantopiaBlocks.SEA_MOSS_CARPET.get(), 3)
            .pattern("##")
            .define('#', PlantopiaBlocks.SEA_MOSS_BLOCK.get())
            .unlockedBy(getHasName(PlantopiaBlocks.SEA_MOSS_BLOCK.get()), has(PlantopiaBlocks.SEA_MOSS_BLOCK.get()))
            .save(output, plantopia(getSimpleRecipeName(PlantopiaBlocks.SEA_MOSS_CARPET.get())));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.PACKED_ICE, 1)
            .pattern("##")
            .pattern("##")
            .group(nameOf(Blocks.PACKED_ICE))
            .define('#', PlantopiaBlocks.ICICLE.get())
            .unlockedBy(getHasName(PlantopiaBlocks.ICICLE.get()), has(PlantopiaBlocks.ICICLE.get()))
            .save(output, plantopia(getSimpleRecipeName(Blocks.PACKED_ICE)));
    }

    private static void setRecipeOutput(@NotNull RecipeOutput output) {
        PlantopiaRecipeProvider.output = output;
    }

    private void generateAll() {
        var workScheduler = Plantopia.getPlatform().getWorkScheduler();

        workScheduler.executeWork("recipe_datagen");

        PlantopiaMetaBuckets.BLOCK.forEach(blockMeta -> {
            if (!blockMeta.shouldGenerateRecipe()) return;

            var type = blockMeta.getType();

            if (type.instanceOf(MetaType.LUCKY_DAISY)) {
                luckyDaisyDye(blockMeta);
                return;
            }

            if (type.instanceOf(MetaType.FLOWER)) {
                dyeFromFlower(blockMeta);
                return;
            }

            if (type.instanceOf(MetaType.SEA_SHELL)) {
                boneMealFromSeaShell(blockMeta);
            }
        });
    }

    /* RECIPES GENERATION ******************************************/

    public static void boneMealFromSeaShell(@NotNull PlantopiaBlockMeta blockMeta) {
        oneToOneConversionRecipe(RecipeCategory.MISC, Items.BONE_MEAL, blockMeta.get(), nameOf(Items.BONE_MEAL), 1);
    }

    public static void dyeFromFlower(@NotNull PlantopiaBlockMeta blockMeta) {
        var color = blockMeta.getColor();
        if (color == null) return;
        var dye = DyeItem.byColor(color);
        int amount = blockMeta.getBlockHeightType().getBaseHeight();
        oneToOneConversionRecipe(RecipeCategory.MISC, dye, blockMeta.get(), nameOf(dye), amount);
    }

    public static void luckyDaisyDye(@NotNull PlantopiaBlockMeta blockMeta) {
        var color = blockMeta.getColor();
        if (color == null) return;

        Item daisyItem = blockMeta.get().asItem();
        Item colorDye = DyeItem.byColor(color);

        PlantopiaLuckyDaisyDyeRecipeBuilder.of(Ingredient.of(daisyItem), colorDye)
            .group(nameOf(colorDye))
            .unlockedBy(getHasName(daisyItem), has(daisyItem))
            .save(output, plantopia(getConversionRecipeName(colorDye, daisyItem)));
    }

    /* RECIPE GENERATION HELPER METHODS ******************************************/

    public static void oneToOneConversionRecipe(RecipeCategory category, ItemLike result, ItemLike ingredient, String group, int resultAmount) {
        ShapelessRecipeBuilder.shapeless(category, result, resultAmount)
            .requires(ingredient)
            .group(group)
            .unlockedBy(getHasName(ingredient), has(ingredient))
            .save(output, plantopia(getConversionRecipeName(result, ingredient)));
    }

    public static void fullBlockRecipe(RecipeCategory category, ItemLike result, ItemLike ingredient) {
        ShapedRecipeBuilder.shaped(category, result)
            .define('#', ingredient)
            .pattern("###")
            .pattern("###")
            .pattern("###")
            .unlockedBy(getHasName(ingredient), has(ingredient))
            .save(output, plantopia(getSimpleRecipeName(result)));
    }

    public static void stonecutterRecipe(RecipeCategory category, ItemLike result, ItemLike ingredient, int resultAmount) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ingredient), category, result, resultAmount)
            .unlockedBy(getHasName(ingredient), has(ingredient))
            .save(output, plantopia(getConversionRecipeName(result, ingredient) + "_stonecutting"));
    }

    public static void smeltingRecipe(RecipeCategory category, ItemLike result, ItemLike ingredient, float experience, int cookingTime) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient), category, result, experience, cookingTime)
            .unlockedBy(getHasName(ingredient), has(ingredient))
            .save(output, plantopia(getConversionRecipeName(result, ingredient) + "_smelting"));
    }

    public static void leafLitterRecipe(ItemLike leafLitter, ItemLike leaves) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, leafLitter, 6)
            .pattern("##")
            .define('#', leaves)
            .group("leaf_litter")
            .unlockedBy(getHasName(leaves), has(leaves))
            .save(output, plantopia(getSimpleRecipeName(leafLitter)));
    }

    public static void planksFromLogs(ItemLike planks, TagKey<Item> logsTag, int count) {
        RecipeProvider.planksFromLogs(output, planks, logsTag, count);
    }

    public static void woodFromLogs(ItemLike wood, ItemLike log) {
        RecipeProvider.woodFromLogs(output, wood, log);
    }

    public static void balksFromLogs(ItemLike balk, ItemLike log, int count) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, balk, count)
            .define('#', log)
            .pattern("#")
            .pattern("#")
            .group("balk")
            .unlockedBy(getHasName(log), has(log))
            .save(output);
    }

    public static void woodenBoat(ItemLike boat, ItemLike planks) {
        RecipeProvider.woodenBoat(output, boat, planks);
    }

    public static void chestBoat(ItemLike chestBoat, ItemLike boat) {
        RecipeProvider.chestBoat(output, chestBoat, boat);
    }

    public static void hangingSign(ItemLike hangingSign, ItemLike strippedLog) {
        RecipeProvider.hangingSign(output, hangingSign, strippedLog);
    }

    public static void blockFamily(BlockFamily blockFamily) {
        RecipeProvider.generateRecipes(output, blockFamily);
    }
}
