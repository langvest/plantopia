package by.langvest.plantopia.datagen.model;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaLuckyDaisyBlock;
import by.langvest.plantopia.item.special.PlantopiaLuckyDaisyBlockItem;
import by.langvest.plantopia.item.special.PlantopiaRenderedIconItem;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import net.minecraft.client.renderer.block.model.BlockModel.GuiLight;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.*;

public class PlantopiaItemModelProvider extends ItemModelProvider {
	private static ModelFile builtinEntityModelCache = null;

	public PlantopiaItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, Plantopia.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		generateAll();

		luckyDaisyBlockItem(PlantopiaBlocks.WHITE_LUCKY_DAISY.get());
		luckyDaisyBlockItem(PlantopiaBlocks.PINK_LUCKY_DAISY.get());
	}

	private void generateAll() {
		PlantopiaMetaBuckets.ITEM.forEach(itemMeta -> {
			if(!itemMeta.shouldGenerateModel()) return;

			Item item = itemMeta.get();

			if(item instanceof PlantopiaRenderedIconItem) {
				entityItem(itemMeta);
				return;
			}

			simpleItem(itemMeta);
		});
	}

	/* MODELS GENERATION ******************************************/

	private void simpleItem(@NotNull PlantopiaItemMeta itemMeta) {
		String baseName = itemMeta.getName();

		ResourceLocation texture = texture(baseName);

		generatedItemModel(baseName, texture);
	}

	private void entityItem(@NotNull PlantopiaItemMeta itemMeta) {
		String baseName = itemMeta.getName();

		entityItemModel(baseName);
	}

	/* CUSTOM MODELS GENERATION ******************************************/

	private void luckyDaisyBlockItem(ItemLike item) {
		String baseName = nameOf(item);

		var stemTexture = texture("lucky_daisy_stem");
		var level1Texture = texture(baseName + "_1");
		var level2Texture = texture(baseName + "_2");
		var level3Texture = texture(baseName + "_3");

		var stemModel = generatedItemModel("lucky_daisy_stem", stemTexture);
		var level1Model = generatedItemModel(baseName + "_1", level1Texture);
		var level2Model = generatedItemModel(baseName + "_2", level2Texture);
		var level3Model = generatedItemModel(baseName + "_3", level3Texture);

		var builder = generatedItemModel(baseName, level3Texture);

		for(int amount = PlantopiaLuckyDaisyBlock.MIN_PETALS; amount <= PlantopiaLuckyDaisyBlock.MAX_PETALS; amount++) {
			ModelFile model = level3Model;

			if(amount == PlantopiaLuckyDaisyBlock.MIN_PETALS) model = stemModel;
			else if(amount <= 4) model = level1Model;
			else if(amount < PlantopiaLuckyDaisyBlock.MAX_PETALS) model = level2Model;

			builder.override()
				.predicate(PlantopiaLuckyDaisyBlockItem.PETAL_AMOUNT_PREDICATE, amount * 0.1F)
				.model(model)
				.end();
		}
	}

	/* ITEM MODELS ******************************************/

	public ItemModelBuilder generatedItemModel(String name, ResourceLocation... layers) {
		ItemModelBuilder itemModel = withExistingParent(name, "generated");
		int layerIndex = 0;
		if(layers != null) for(ResourceLocation layeredTexture : layers) itemModel.texture("layer" + layerIndex++, layeredTexture);
		return itemModel;
	}

	public ItemModelBuilder entityItemModel(String name) {
		return entityItemModel(name, GuiLight.FRONT);
	}

	public ItemModelBuilder entityItemModel(String name, GuiLight guiLight) {
		return getBuilder(name).parent(getBuiltInEntityModel()).guiLight(guiLight);
	}

	/* HELPER METHODS ******************************************/

	@Contract("_ -> new")
	private static @NotNull ResourceLocation texture(String name) {
		return plantopia(ModelProvider.ITEM_FOLDER, name);
	}

	@Contract("_ -> new")
	private static @NotNull ResourceLocation blockTexture(String name) {
		return plantopia(ModelProvider.BLOCK_FOLDER, name);
	}

	@Contract(" -> new")
	private static @NotNull ModelFile getBuiltInEntityModel() {
		if(builtinEntityModelCache != null) return builtinEntityModelCache;

		return builtinEntityModelCache = new ModelFile(minecraft("builtin/entity")) {
			@Override
			protected boolean exists() {
				return true;
			}
		};
	}

	@Contract("_ -> new")
	private static @NotNull ResourceLocation parent(String name) {
		return plantopia(ModelProvider.ITEM_FOLDER, name);
	}
}