package by.langvest.plantopia.datagen.model;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.item.special.PlantopiaRenderedIconItem;
import by.langvest.plantopia.item.special.PlantopiaWaterlilyItem;
import by.langvest.plantopia.meta.PlantopiaMetaRegistries;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import net.minecraft.client.renderer.block.model.BlockModel.GuiLight;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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
	}

	private void generateAll() {
		PlantopiaMetaRegistries.ITEMS.forEach(itemMeta -> {
			if(!itemMeta.shouldGenerateModel()) return;

			Item item = itemMeta.getItem();

			if(item instanceof PlantopiaWaterlilyItem) {
				waterlilyItem(item);
				return;
			}

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

	private void waterlilyItem(Item item) {
		String baseName = nameOf(item);

		var texture = blockTexture(baseName);

		waterlilyTemplateModel(baseName, texture);
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

	private ItemModelBuilder waterlilyTemplateModel(String name, ResourceLocation texture) {
		return withExistingParent(name, parent("template_waterlily"))
			.texture("layer0", texture);
	}

	/* HELPER METHODS ******************************************/

	@Contract("_ -> new")
	private static @NotNull ResourceLocation texture(String name) {
		return plantopiaLocationFrom(ModelProvider.ITEM_FOLDER, name);
	}

	@Contract("_ -> new")
	private static @NotNull ResourceLocation blockTexture(String name) {
		return plantopiaLocationFrom(ModelProvider.BLOCK_FOLDER, name);
	}

	@Contract(" -> new")
	private static @NotNull ModelFile getBuiltInEntityModel() {
		if(builtinEntityModelCache != null) return builtinEntityModelCache;

		return builtinEntityModelCache = new ModelFile(minecraftLocationFrom("builtin/entity")) {
			@Override
			protected boolean exists() {
				return true;
			}
		};
	}

	@Contract("_ -> new")
	private static @NotNull ResourceLocation parent(String name) {
		return plantopiaLocationFrom(ModelProvider.ITEM_FOLDER, name);
	}
}