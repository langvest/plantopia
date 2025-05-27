package by.langvest.plantopia.datagen.model;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaQuarter;
import by.langvest.plantopia.block.PlantopiaTripleBlockHalf;
import by.langvest.plantopia.block.special.PlantopiaCloverBlock;
import by.langvest.plantopia.block.special.PlantopiaCobblestoneShardBlock;
import by.langvest.plantopia.block.special.PlantopiaTriplePlantBlock;
import by.langvest.plantopia.block.special.PlantopiaWideTriplePlantBlock;
import by.langvest.plantopia.meta.property.PlantopiaModelType;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.PlantopiaMetaStore;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static by.langvest.plantopia.util.helper.PlantopiaContentHelper.*;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.*;

public class PlantopiaBlockStateProvider extends BlockStateProvider {
	private static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");
	private static final Set<Direction> HORIZONTAL_DIRECTIONS = ImmutableSet.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
	private final ExistingFileHelper existingFileHelper;

	public PlantopiaBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, Plantopia.MOD_ID, existingFileHelper);
		this.existingFileHelper = existingFileHelper;
	}

	@Override
	protected void registerStatesAndModels() {
		generateAll();

		pottedFernBlock(Blocks.POTTED_FERN);
		fireweedBlock(PlantopiaBlocks.FIREWEED.get());
		giantFernBlock(PlantopiaBlocks.GIANT_FERN.get());
		cloverBlock(PlantopiaBlocks.CLOVER.get());
		bigCloverBlock(PlantopiaBlocks.BIG_CLOVER.get());
		cloverBlossomBlock(PlantopiaBlocks.WHITE_CLOVER_BLOSSOM.get());
		cloverBlossomBlock(PlantopiaBlocks.PINK_CLOVER_BLOSSOM.get());
		cobblestoneShardBlock(PlantopiaBlocks.COBBLESTONE_SHARD.get());
		cobblestoneShardBlock(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD.get());
		bushBlock(PlantopiaBlocks.BUSH.get());
		foxgloveBlock(PlantopiaBlocks.RED_FOXGLOVE.get());
		foxgloveBlock(PlantopiaBlocks.ORANGE_FOXGLOVE.get());
		foxgloveBlock(PlantopiaBlocks.YELLOW_FOXGLOVE.get());
		foxgloveBlock(PlantopiaBlocks.WHITE_FOXGLOVE.get());
		foxgloveBlock(PlantopiaBlocks.PINK_FOXGLOVE.get());
		foxgloveBlock(PlantopiaBlocks.MAGENTA_FOXGLOVE.get());
		hollyhockBlock(PlantopiaBlocks.RED_HOLLYHOCK.get());
		hollyhockBlock(PlantopiaBlocks.ORANGE_HOLLYHOCK.get());
		hollyhockBlock(PlantopiaBlocks.YELLOW_HOLLYHOCK.get());
		hollyhockBlock(PlantopiaBlocks.WHITE_HOLLYHOCK.get());
		hollyhockBlock(PlantopiaBlocks.PINK_HOLLYHOCK.get());
		hollyhockBlock(PlantopiaBlocks.MAGENTA_HOLLYHOCK.get());
		pollinatedDandelionBlock(PlantopiaBlocks.POLLINATED_DANDELION.get());
		hogweedBlock(PlantopiaBlocks.HOGWEED.get());
	}

	private void generateAll() {
		PlantopiaMetaStore.getBlocks().forEach(blockMeta -> {
			if(!blockMeta.shouldGenerateModel()) return;

			var block = blockMeta.getBlock();
			var type = blockMeta.getType();

			if(block instanceof FlowerPotBlock) {
				flowerPotBlock(blockMeta);
				return;
			}

			if(block instanceof PlantopiaTriplePlantBlock) {
				triplePlantBlock(blockMeta);
				return;
			}

			if(block instanceof DoublePlantBlock) {
				doublePlantBlock(blockMeta);
				return;
			}

			if(block instanceof BushBlock || type.isPlantLike()) {
				bushBlock(blockMeta);
				return;
			}

			simpleBlock(blockMeta);
		});
	}

	/* MODELS GENERATION ******************************************/

	private void simpleBlock(@NotNull PlantopiaBlockMeta blockMeta) {
		String baseName = blockMeta.getName();

		var texture = texture(baseName);

		var model = cubeAllModel(baseName, texture);

		if(blockMeta.hasItem()) blockItemModel(baseName, model);
		simpleBlock(blockMeta.getBlock(), model);
	}

	private void flowerPotBlock(@NotNull PlantopiaBlockMeta blockMeta) {
		String baseName = blockMeta.getName();
		var block = (FlowerPotBlock)blockMeta.getBlock();
		var plant = block.getContent();
		var plantMeta = PlantopiaMetaStore.getBlock(plant);

		if(plantMeta != null && plantMeta.getModelType() == PlantopiaModelType.CUSTOM) return;

		var pottedPlantTexture = texture(pottedNameOf(plant));
		var plantTexture = isTextureExists(pottedPlantTexture) ? pottedPlantTexture : blockTexture(plant);

		var model = flowerPotCrossModel(baseName, plantTexture, blockMeta.isTinted());

		simpleBlock(block, model);
	}

	private void doublePlantBlock(@NotNull PlantopiaBlockMeta blockMeta) {
		String baseName = blockMeta.getName();
		boolean isTinted = blockMeta.isTinted();

		var topTexture = texture(baseName + "_top");
		var bottomTexture = texture(baseName + "_bottom");

		var topModel = crossModel(baseName + "_top", topTexture, isTinted);
		var bottomModel = crossModel(baseName + "_bottom", bottomTexture, isTinted);

		if(blockMeta.hasItem()) generatedItemModel(baseName, topTexture);
		doubleHighBlock(blockMeta.getBlock(), topModel, bottomModel);
	}

	private void triplePlantBlock(@NotNull PlantopiaBlockMeta blockMeta) {
		String baseName = blockMeta.getName();
		boolean isTinted = blockMeta.isTinted();

		var topTexture = texture(baseName + "_top");
		var middleTexture = texture(baseName + "_middle");
		var bottomTexture = texture(baseName + "_bottom");

		var topModel = crossModel(baseName + "_top", topTexture, isTinted);
		var middleModel = crossModel(baseName + "_middle", middleTexture, isTinted);
		var bottomModel = crossModel(baseName + "_bottom", bottomTexture, isTinted);

		if(blockMeta.hasItem()) generatedItemModel(baseName, topTexture);
		tripleHighBlock(blockMeta.getBlock(), topModel, middleModel, bottomModel);
	}

	private void bushBlock(@NotNull PlantopiaBlockMeta blockMeta) {
		String baseName = blockMeta.getName();
		boolean isTinted = blockMeta.isTinted();

		var texture = texture(baseName);

		var model = crossModel(baseName, texture, isTinted);

		if(blockMeta.hasItem()) generatedItemModel(baseName, texture);
		simpleBlock(blockMeta.getBlock(), model);
	}

	/* CUSTOM MODELS GENERATION ******************************************/

	private void fireweedBlock(Block block) {
		String baseName = nameOf(block);

		var topTexture = texture(baseName + "_top");
		var flowersTexture = texture(baseName + "_top_flowers");
		var bottomTexture = texture(baseName + "_bottom");

		var topModel = invertedTintedCrossWithOverlayModel(baseName + "_top", topTexture, flowersTexture);
		var bottomModel = tintedCrossModel(baseName + "_bottom", bottomTexture);

		generatedItemModel(baseName, topTexture, flowersTexture);
		doubleHighBlock(block, topModel, bottomModel);
	}

	private void giantFernBlock(Block block) {
		String baseName = nameOf(block);

		var topTexture = texture(baseName + "_top");
		var middleTexture = texture(baseName + "_middle");
		var bottomTexture = texture(baseName + "_bottom");

		var topModel = giantFernTemplateModel(baseName + "_top", topTexture);
		var middleModel = giantFernTemplateModel(baseName + "_middle", middleTexture);
		var bottomModel = giantFernTemplateModel(baseName + "_bottom", bottomTexture);

		generatedItemModel(baseName, topTexture);
		tripleHighBlock(block, topModel, middleModel, bottomModel);
	}

	private void cloverBlock(Block block) {
		String baseName = nameOf(block);

		generatedItemModel(baseName, itemTexture(baseName));
		directionalMultipartBlock(block, PlantopiaCloverBlock.AMOUNT);
	}

	private void bigCloverBlock(Block block) {
		String baseName = nameOf(block);

		generatedItemModel(baseName, texture(baseName));
		simpleBlock(block, existingModel(baseName));

		var pottedBlock = pottedBlockOf(block);

		if(pottedBlock != null) {
			simpleBlock(pottedBlock, existingModel(nameOf(pottedBlock)));
		}
	}

	private void pottedFernBlock(Block block) {
		if(!(block instanceof FlowerPotBlock flowerPotBlock)) return;

		var plant = flowerPotBlock.getContent();

		tintedFlowerPotCrossModel(idOf(flowerPotBlock), blockTexture(plant));
	}

	private void cloverBlossomBlock(Block block) {
		String baseName = nameOf(block);

		var blossomTexture = texture(baseName);
		var stemItemTexture = itemTexture("clover_blossom_stem");

		var model = cloverBlossomTemplateModel(baseName, blossomTexture);

		generatedItemModel(baseName, blossomTexture, stemItemTexture);
		simpleBlock(block, model);

		var pottedBlock = pottedBlockOf(block);

		if(pottedBlock != null) {
			simpleBlock(pottedBlock, pottedCloverBlossomTemplateModel(nameOf(pottedBlock), blossomTexture));
		}
	}

	private void cobblestoneShardBlock(Block block) {
		String baseName = nameOf(block);

		var shardsTexture = texture(baseName + "s");
		var itemTexture = itemTexture(baseName);

		var oneShardModel = oneCobblestoneShardTemplateModel("one_" + baseName, shardsTexture);
		var twoShardsModel = twoCobblestoneShardsTemplateModel("two_" + baseName + "s", shardsTexture);
		var threeShardsModel = threeCobblestoneShardsTemplateModel("three_" + baseName + "s", shardsTexture);
		var fourShardsModel = fourCobblestoneShardsTemplateModel("four_" + baseName + "s", shardsTexture);

		generatedItemModel(baseName, itemTexture);
		rotatedVariableBlock(block, PlantopiaCobblestoneShardBlock.SHARDS, oneShardModel, twoShardsModel, threeShardsModel, fourShardsModel);
	}

	private void bushBlock(Block block) {
		String baseName = nameOf(block);

		var bushTexture = texture(baseName);
		var stemTexture = texture(baseName + "_stem");

		var model = tintedCrossWithOverlayModel(baseName, bushTexture, stemTexture);

		generatedItemModel(baseName, bushTexture, stemTexture);
		simpleBlock(block, model);

		var pottedBlock = pottedBlockOf(block);

		if(pottedBlock != null) {
			simpleBlock(pottedBlock, tintedFlowerPotCrossWithOverlayModel(nameOf(pottedBlock), bushTexture, stemTexture));
		}
	}

	private void foxgloveBlock(Block block) {
		String baseName = nameOf(block);

		var topTexture = texture("foxglove_top");
		var bottomTexture = texture("foxglove_bottom");
		var flowersTexture = texture(baseName + "_flowers");

		var topModel = foxgloveTopTemplateModel(baseName + "_top", topTexture, flowersTexture);
		var bottomModel = crossModel(baseName + "_bottom", bottomTexture);

		generatedItemModel(baseName, flowersTexture);
		doubleHighBlock(block, topModel, bottomModel);
	}

	private void hollyhockBlock(Block block) {
		String baseName = nameOf(block);

		var topTexture = texture("hollyhock_top");
		var bottomTexture = texture("hollyhock_bottom");
		var flowersTopTexture = texture(baseName + "_flowers_top");
		var flowersBottomTexture = texture(baseName + "_flowers_bottom");

		var topModel = hollyhockTopTemplateModel(baseName + "_top", topTexture, flowersTopTexture);
		var bottomModel = hollyhockBottomTemplateModel(baseName + "_bottom", bottomTexture, flowersBottomTexture);

		generatedItemModel(baseName, flowersTopTexture);
		doubleHighBlock(block, topModel, bottomModel);
	}

	private void pollinatedDandelionBlock(Block block) {
		var model = blockModel(Blocks.DANDELION);

		simpleBlock(block, model);
	}

	private void hogweedBlock(Block block) {
		String baseName = nameOf(block);

		var topLeftTexture = texture(baseName + "_top_left");
		var topRightTexture = texture(baseName + "_top_right");
		var middleLeftTexture = texture(baseName + "_middle_left");
		var middleRightTexture = texture(baseName + "_middle_right");
		var bottomLeftTexture = texture(baseName + "_bottom_left");
		var bottomRightTexture = texture(baseName + "_bottom_right");

		var topModel = wideCrossLeafModel(baseName + "_top", topLeftTexture, topRightTexture);
		var middleModel = wideCrossLeafModel(baseName + "_middle", middleLeftTexture, middleRightTexture);
		var bottomModel = wideCrossLeafModel(baseName + "_bottom", bottomLeftTexture, bottomRightTexture);

		generatedItemModel(baseName, topRightTexture);

		getVariantBuilder(block).forAllStates(state -> {
			PlantopiaTripleBlockHalf half = state.getValue(PlantopiaWideTriplePlantBlock.HALF);
			PlantopiaQuarter quarter = state.getValue(PlantopiaWideTriplePlantBlock.QUARTER);

			var modelFile = switch(half) {
				case UPPER -> topModel;
				case CENTRAL -> middleModel;
				case LOWER -> bottomModel;
			};

			int rotation = switch(quarter) {
				case SOUTH_WEST -> 0;
				case WEST_NORTH -> 90;
				case NORTH_EAST -> 180;
				case EAST_SOUTH -> 270;
			};

			return ConfiguredModel.builder().modelFile(modelFile).rotationY(rotation).build();
		});
	}

	/* MODEL GENERATION HELPER METHODS ******************************************/

	private void doubleHighBlock(Block block, ModelFile topModel, ModelFile bottomModel) {
		getVariantBuilder(block).forAllStates(state -> {
			DoubleBlockHalf half = state.getValue(DoublePlantBlock.HALF);

			var modelFile = switch(half) {
				case UPPER -> topModel;
				case LOWER -> bottomModel;
			};

			return ConfiguredModel.builder().modelFile(modelFile).build();
		});
	}

	private void tripleHighBlock(Block block, ModelFile topModel, ModelFile middleModel, ModelFile bottomModel) {
		getVariantBuilder(block).forAllStates(state -> {
			PlantopiaTripleBlockHalf half = state.getValue(PlantopiaTriplePlantBlock.HALF);

			var modelFile = switch(half) {
				case UPPER -> topModel;
				case CENTRAL -> middleModel;
				case LOWER -> bottomModel;
			};

			return ConfiguredModel.builder().modelFile(modelFile).build();
		});
	}

	private void directionalMultipartBlock(Block block, @NotNull IntegerProperty property) {
		String baseName = nameOf(block);
		Integer maxValue = Collections.max(property.getPossibleValues());
		MultiPartBlockStateBuilder builder = getMultipartBuilder(block);

		property.getPossibleValues().forEach(value -> {
			ArrayList<Integer> values = Lists.newArrayList();

			for(int i = value; i <= maxValue; i++) values.add(i);

			var model = existingModel(baseName + "_" + value);

			HORIZONTAL_DIRECTIONS.forEach(direction ->
				builder.part()
					.modelFile(model).rotationY((((int)direction.toYRot()) + 180) % 360).addModel()
					.condition(BlockStateProperties.HORIZONTAL_FACING, direction)
					.condition(property, values.toArray(Integer[]::new))
					.end()
			);
		});
	}

	private <T extends Comparable<T>> void rotatedVariableBlock(Block block, @NotNull Property<T> property, ModelFile ...modelFiles) {
		int modelFileIndex = 0;
		var builder = getVariantBuilder(block);

		for(T value : property.getPossibleValues()) {
			if(modelFileIndex == modelFiles.length) break;

			var modelFile = modelFiles[modelFileIndex++];

			builder
				.partialState().with(property, value).modelForState()
				.modelFile(modelFile).nextModel()
				.modelFile(modelFile).rotationY(90).nextModel()
				.modelFile(modelFile).rotationY(180).nextModel()
				.modelFile(modelFile).rotationY(270).addModel();
		}
	}

	private interface ModelFileResolver<T extends Comparable<T>> {
		ModelFile getModelFile(T value);
	}

	private <T extends Comparable<T>> void variableBlock(Block block, @NotNull Property<T> property, ModelFileResolver<T> resolver) {
		VariantBlockStateBuilder builder = getVariantBuilder(block);

		for(T value : property.getPossibleValues()) {
			var modelFile = resolver.getModelFile(value);

			builder
				.partialState().with(property, value).modelForState()
				.modelFile(modelFile).addModel();
		}
	}

	/* BLOCK MODELS ******************************************/

	@Contract("_ -> new")
	private @NotNull ModelFile.ExistingModelFile existingModel(String name) {
		return models().getExistingFile(plantopiaLocationFrom(name));
	}

	private ModelFile.ExistingModelFile blockModel(@NotNull Block block) {
		var blockLocation = locationOf(block);
		var modelLocation = locationFrom(blockLocation.getNamespace(), ModelProvider.BLOCK_FOLDER, blockLocation.getPath());
		return models().getExistingFile(modelLocation);
	}

	private BlockModelBuilder cubeAllModel(String name, ResourceLocation texture) {
		return models().cubeAll(name, texture);
	}

	private BlockModelBuilder crossModel(String name, ResourceLocation crossTexture, boolean tinted) {
		if(tinted) return tintedCrossModel(name, crossTexture);
		return crossModel(name, crossTexture);
	}

	private BlockModelBuilder crossModel(String name, ResourceLocation crossTexture) {
		return models().cross(name, crossTexture);
	}

	private BlockModelBuilder tintedCrossModel(String name, ResourceLocation crossTexture) {
		return models().withExistingParent(name, "tinted_cross")
			.texture("cross", crossTexture);
	}

	private BlockModelBuilder flowerPotCrossModel(String name, ResourceLocation plantTexture, boolean tinted) {
		if(tinted) return tintedFlowerPotCrossModel(name, plantTexture);
		return flowerPotCrossModel(name, plantTexture);
	}

	private BlockModelBuilder flowerPotCrossModel(String name, ResourceLocation plantTexture) {
		return models().withExistingParent(name, "flower_pot_cross")
			.texture("plant", plantTexture);
	}

	private BlockModelBuilder tintedFlowerPotCrossModel(String name, ResourceLocation plantTexture) {
		return models().withExistingParent(name, parent("tinted_flower_pot_cross"))
			.texture("plant", plantTexture);
	}

	private BlockModelBuilder tintedFlowerPotCrossWithOverlayModel(String name, ResourceLocation plantTexture, ResourceLocation overlayTexture) {
		return models().withExistingParent(name, parent("tinted_flower_pot_cross_with_overlay"))
			.texture("plant", plantTexture)
			.texture("overlay", overlayTexture);
	}

	private BlockModelBuilder tintedCrossWithOverlayModel(String name, ResourceLocation crossTexture, ResourceLocation overlayTexture) {
		return models().withExistingParent(name, parent("tinted_cross_with_overlay"))
			.texture("cross", crossTexture)
			.texture("overlay", overlayTexture);
	}

	private BlockModelBuilder giantFernTemplateModel(String name, ResourceLocation crossTexture) {
		return models().withExistingParent(name, parent("template_giant_fern"))
			.texture("cross", crossTexture);
	}

	private BlockModelBuilder invertedTintedCrossWithOverlayModel(String name, ResourceLocation crossTexture, ResourceLocation overlayTexture) {
		return models().withExistingParent(name, parent("inverted_tinted_cross_with_overlay"))
			.texture("cross", crossTexture)
			.texture("overlay", overlayTexture);
	}

	private BlockModelBuilder wideCrossLeafModel(String name, ResourceLocation leftTexture, ResourceLocation rightTexture) {
		return models().withExistingParent(name, parent("wide_cross_leaf"))
			.texture("right", rightTexture)
			.texture("left", leftTexture);
	}

	private BlockModelBuilder cloverBlossomTemplateModel(String name, ResourceLocation blossomTexture) {
		return models().withExistingParent(name, parent("template_clover_blossom"))
			.texture("blossom", blossomTexture);
	}

	private BlockModelBuilder pottedCloverBlossomTemplateModel(String name, ResourceLocation blossomTexture) {
		return models().withExistingParent(name, parent("template_potted_clover_blossom"))
			.texture("blossom", blossomTexture);
	}

	private BlockModelBuilder oneCobblestoneShardTemplateModel(String name, ResourceLocation shardsTexture) {
		return models().withExistingParent(name, parent("template_one_cobblestone_shard"))
			.texture("shards", shardsTexture);
	}

	private BlockModelBuilder twoCobblestoneShardsTemplateModel(String name, ResourceLocation shardsTexture) {
		return models().withExistingParent(name, parent("template_two_cobblestone_shards"))
			.texture("shards", shardsTexture);
	}

	private BlockModelBuilder threeCobblestoneShardsTemplateModel(String name, ResourceLocation shardsTexture) {
		return models().withExistingParent(name, parent("template_three_cobblestone_shards"))
			.texture("shards", shardsTexture);
	}

	private BlockModelBuilder fourCobblestoneShardsTemplateModel(String name, ResourceLocation shardsTexture) {
		return models().withExistingParent(name, parent("template_four_cobblestone_shards"))
			.texture("shards", shardsTexture);
	}

	private BlockModelBuilder foxgloveTopTemplateModel(String name, ResourceLocation crossTexture, ResourceLocation flowersTexture) {
		return models().withExistingParent(name, parent("template_foxglove_top"))
			.texture("cross", crossTexture)
			.texture("flowers", flowersTexture);
	}

	private BlockModelBuilder hollyhockTopTemplateModel(String name, ResourceLocation crossTexture, ResourceLocation flowersTexture) {
		return models().withExistingParent(name, parent("template_hollyhock_top"))
			.texture("cross", crossTexture)
			.texture("flowers", flowersTexture);
	}

	private BlockModelBuilder hollyhockBottomTemplateModel(String name, ResourceLocation crossTexture, ResourceLocation flowersTexture) {
		return models().withExistingParent(name, parent("template_hollyhock_bottom"))
			.texture("cross", crossTexture)
			.texture("flowers", flowersTexture);
	}

	/* ITEM MODELS ******************************************/

	public void generatedItemModel(String name, ResourceLocation... layers) {
		ItemModelBuilder itemModel = itemModels().withExistingParent(name, "generated");
		int layerIndex = 0;
		if(layers != null) for(ResourceLocation layeredTexture : layers) itemModel.texture("layer" + layerIndex++, layeredTexture);
	}

	public void blockItemModel(String name, @NotNull ModelFile modelFile) {
		itemModels().withExistingParent(name, modelFile.getLocation());
	}

	/* HELPER METHODS ******************************************/

	private boolean isTextureExists(@NotNull ResourceLocation texture) {
		return existingFileHelper.exists(texture, TEXTURE);
	}

	@Contract("_ -> new")
	private static @NotNull ResourceLocation texture(String name) {
		return plantopiaLocationFrom(ModelProvider.BLOCK_FOLDER, name);
	}

	@Contract("_ -> new")
	private static @NotNull ResourceLocation itemTexture(String name) {
		return plantopiaLocationFrom(ModelProvider.ITEM_FOLDER, name);
	}

	@Contract("_ -> new")
	private static @NotNull ResourceLocation parent(String name) {
		return plantopiaLocationFrom(ModelProvider.BLOCK_FOLDER, name);
	}
}