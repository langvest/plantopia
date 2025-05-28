package by.langvest.plantopia.datagen.model;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.item.special.PlantopiaRenderedIconItem;
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

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.minecraftLocationFrom;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopiaLocationFrom;

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

	/* MODELS ******************************************/

	public void generatedItemModel(String name, ResourceLocation... layers) {
		ItemModelBuilder itemModel = withExistingParent(name, "generated");
		int layerIndex = 0;
		if(layers != null) for(ResourceLocation layeredTexture : layers) itemModel.texture("layer" + layerIndex++, layeredTexture);
	}

	public void entityItemModel(String name) {
		entityItemModel(name, GuiLight.FRONT);
	}

	public void entityItemModel(String name, GuiLight guiLight) {
		getBuilder(name).parent(getBuiltInEntityModel()).guiLight(guiLight);
	}

	/* HELPER METHODS ******************************************/

	@Contract("_ -> new")
	private static @NotNull ResourceLocation texture(String name) {
		return plantopiaLocationFrom(ModelProvider.ITEM_FOLDER, name);
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
}