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
import by.langvest.plantopia.util.PlantopiaIdentifier;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.store.PlantopiaMetaStore;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
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

import static by.langvest.plantopia.util.PlantopiaContentHelper.*;

public class PlantopiaBlockStateProvider extends BlockStateProvider {
	private static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");
	private static final Set<Direction> HORIZONTAL_DIRECTIONS = ImmutableSet.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
	private final ExistingFileHelper existingFileHelper;

	public PlantopiaBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, Plantopia.MOD_ID, existingFileHelper);
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

			Block block = blockMeta.getBlock();

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

			if(block instanceof BushBlock) {
				bushBlock(blockMeta);
				return;
			}

			simpleBlock(blockMeta);
		});
	}

	/* MODELS GENERATION ******************************************/

	private void simpleBlock(@NotNull PlantopiaBlockMeta blockMeta) {
		String baseName = blockMeta.getName();

		ResourceLocation texture = texture(baseName);

		ModelFile model = cubeAllModel(baseName, texture);

		if(blockMeta.hasItem()) blockItemModel(baseName, model);
		simpleBlock(blockMeta.getBlock(), model);
	}

	private void flowerPotBlock(@NotNull PlantopiaBlockMeta blockMeta) {
		String baseName = blockMeta.getName();
		FlowerPotBlock block = (FlowerPotBlock)blockMeta.getBlock();
		Block plant = block.getContent();
		PlantopiaBlockMeta plantMeta = PlantopiaMetaStore.getBlock(plant);

		if(plantMeta != null && plantMeta.getModelType() == PlantopiaModelType.CUSTOM) return;

		ResourceLocation pottedPlantTexture = texture("potted_" + nameOf(plant));
		ResourceLocation plantTexture = isTextureExists(pottedPlantTexture) ? pottedPlantTexture : blockTexture(plant);

		ModelFile model = flowerPotCrossModel(baseName, plantTexture, blockMeta.isTinted());

		simpleBlock(block, model);
	}

	private void doublePlantBlock(@NotNull PlantopiaBlockMeta blockMeta) {
		String baseName = blockMeta.getName();
		boolean isTinted = blockMeta.isTinted();

		ResourceLocation topTexture = texture(baseName + "_top");
		ResourceLocation bottomTexture = texture(baseName + "_bottom");

		ModelFile topModel = crossModel(baseName + "_top", topTexture, isTinted);
		ModelFile bottomModel = crossModel(baseName + "_bottom", bottomTexture, isTinted);

		if(blockMeta.hasItem()) generatedItemModel(baseName, topTexture);
		doubleHighBlock(blockMeta.getBlock(), topModel, bottomModel);
	}

	private void triplePlantBlock(@NotNull PlantopiaBlockMeta blockMeta) {
		String baseName = blockMeta.getName();
		boolean isTinted = blockMeta.isTinted();

		ResourceLocation topTexture = texture(baseName + "_top");
		ResourceLocation middleTexture = texture(baseName + "_middle");
		ResourceLocation bottomTexture = texture(baseName + "_bottom");

		ModelFile topModel = crossModel(baseName + "_top", topTexture, isTinted);
		ModelFile middleModel = crossModel(baseName + "_middle", middleTexture, isTinted);
		ModelFile bottomModel = crossModel(baseName + "_bottom", bottomTexture, isTinted);

		if(blockMeta.hasItem()) generatedItemModel(baseName, topTexture);
		tripleHighBlock(blockMeta.getBlock(), topModel, middleModel, bottomModel);
	}

	private void bushBlock(@NotNull PlantopiaBlockMeta blockMeta) {
		String baseName = blockMeta.getName();
		boolean isTinted = blockMeta.isTinted();

		ResourceLocation texture = texture(baseName);

		ModelFile model = crossModel(baseName, texture, isTinted);

		if(blockMeta.hasItem()) generatedItemModel(baseName, texture);
		simpleBlock(blockMeta.getBlock(), model);
	}

	/* CUSTOM MODELS GENERATION ******************************************/

	private void fireweedBlock(Block block) {
		String baseName = nameOf(block);

		ResourceLocation topTexture = texture(baseName + "_top");
		ResourceLocation flowersTexture = texture(baseName + "_top_flowers");
		ResourceLocation bottomTexture = texture(baseName + "_bottom");

		ModelFile topModel = invertedTintedCrossWithOverlayModel(baseName + "_top", topTexture, flowersTexture);
		ModelFile bottomModel = tintedCrossModel(baseName + "_bottom", bottomTexture);

		generatedItemModel(baseName, topTexture, flowersTexture);
		doubleHighBlock(block, topModel, bottomModel);
	}

	private void giantFernBlock(Block block) {
		String baseName = nameOf(block);

		ResourceLocation topTexture = texture(baseName + "_top");
		ResourceLocation middleTexture = texture(baseName + "_middle");
		ResourceLocation bottomTexture = texture(baseName + "_bottom");

		ModelFile topModel = giantFernTemplateModel(baseName + "_top", topTexture);
		ModelFile middleModel = giantFernTemplateModel(baseName + "_middle", middleTexture);
		ModelFile bottomModel = giantFernTemplateModel(baseName + "_bottom", bottomTexture);

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

		Block pottedBlock = pottedBlockOf(block);
		if(pottedBlock != null) simpleBlock(pottedBlock, existingModel(nameOf(pottedBlock)));
	}

	private void pottedFernBlock(Block block) {
		if(!(block instanceof FlowerPotBlock flowerPotBlock)) return;
		Block plant = flowerPotBlock.getContent();
		tintedFlowerPotCrossModel(idOf(flowerPotBlock), blockTexture(plant));
	}

	private void cloverBlossomBlock(Block block) {
		String baseName = nameOf(block);

		ResourceLocation blossomTexture = texture(baseName);
		ResourceLocation stemItemTexture = itemTexture("clover_blossom_stem");

		ModelFile model = cloverBlossomTemplateModel(baseName, blossomTexture);

		generatedItemModel(baseName, blossomTexture, stemItemTexture);
		simpleBlock(block, model);

		Block pottedBlock = pottedBlockOf(block);
		if(pottedBlock != null) simpleBlock(pottedBlock, pottedCloverBlossomTemplateModel(nameOf(pottedBlock), blossomTexture));
	}

	private void cobblestoneShardBlock(Block block) {
		String baseName = nameOf(block);

		ResourceLocation shardsTexture = texture(baseName + "s");
		ResourceLocation itemTexture = itemTexture(baseName);

		ModelFile oneShardModel = oneCobblestoneShardTemplateModel("one_" + baseName, shardsTexture);
		ModelFile twoShardsModel = twoCobblestoneShardsTemplateModel("two_" + baseName + "s", shardsTexture);
		ModelFile threeShardsModel = threeCobblestoneShardsTemplateModel("three_" + baseName + "s", shardsTexture);
		ModelFile fourShardsModel = fourCobblestoneShardsTemplateModel("four_" + baseName + "s", shardsTexture);

		generatedItemModel(baseName, itemTexture);
		rotatedVariableBlock(block, PlantopiaCobblestoneShardBlock.SHARDS, oneShardModel, twoShardsModel, threeShardsModel, fourShardsModel);
	}

	private void bushBlock(Block block) {
		String baseName = nameOf(block);

		ResourceLocation bushTexture = texture(baseName);
		ResourceLocation stemTexture = texture(baseName + "_stem");

		ModelFile model = tintedCrossWithOverlayModel(baseName, bushTexture, stemTexture);

		generatedItemModel(baseName, bushTexture, stemTexture);
		simpleBlock(block, model);

		Block pottedBlock = pottedBlockOf(block);
		if(pottedBlock != null) simpleBlock(pottedBlock, tintedFlowerPotCrossWithOverlayModel(nameOf(pottedBlock), bushTexture, stemTexture));
	}

	private void foxgloveBlock(Block block) {
		String baseName = nameOf(block);

		ResourceLocation topTexture = texture("foxglove_top");
		ResourceLocation bottomTexture = texture("foxglove_bottom");
		ResourceLocation flowersTexture = texture(baseName + "_flowers");

		ModelFile topModel = foxgloveTopTemplateModel(baseName + "_top", topTexture, flowersTexture);
		ModelFile bottomModel = crossModel(baseName + "_bottom", bottomTexture);

		generatedItemModel(baseName, flowersTexture);
		doubleHighBlock(block, topModel, bottomModel);
	}

	private void hollyhockBlock(Block block) {
		String baseName = nameOf(block);

		ResourceLocation topTexture = texture("hollyhock_top");
		ResourceLocation bottomTexture = texture("hollyhock_bottom");
		ResourceLocation flowersTopTexture = texture(baseName + "_flowers_top");
		ResourceLocation flowersBottomTexture = texture(baseName + "_flowers_bottom");

		ModelFile topModel = hollyhockTopTemplateModel(baseName + "_top", topTexture, flowersTopTexture);
		ModelFile bottomModel = hollyhockBottomTemplateModel(baseName + "_bottom", bottomTexture, flowersBottomTexture);

		generatedItemModel(baseName, flowersTopTexture);
		doubleHighBlock(block, topModel, bottomModel);
	}

	private void pollinatedDandelionBlock(Block block) {
		ModelFile model = blockModel(Blocks.DANDELION);

		simpleBlock(block, model);
	}

	private void hogweedBlock(Block block) {
		String baseName = nameOf(block);

		ResourceLocation topLeftTexture = texture(baseName + "_top_left");
		ResourceLocation topRightTexture = texture(baseName + "_top_right");
		ResourceLocation middleLeftTexture = texture(baseName + "_middle_left");
		ResourceLocation middleRightTexture = texture(baseName + "_middle_right");
		ResourceLocation bottomLeftTexture = texture(baseName + "_bottom_left");
		ResourceLocation bottomRightTexture = texture(baseName + "_bottom_right");

		ModelFile topModel = wideCrossLeafModel(baseName + "_top", topLeftTexture, topRightTexture);
		ModelFile middleModel = wideCrossLeafModel(baseName + "_middle", middleLeftTexture, middleRightTexture);
		ModelFile bottomModel = wideCrossLeafModel(baseName + "_bottom", bottomLeftTexture, bottomRightTexture);

		generatedItemModel(baseName, topRightTexture);

		getVariantBuilder(block).forAllStates(state -> {
			PlantopiaTripleBlockHalf half = state.getValue(PlantopiaWideTriplePlantBlock.HALF);
			PlantopiaQuarter quarter = state.getValue(PlantopiaWideTriplePlantBlock.QUARTER);
			ModelFile modelFile = switch(half) {
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
			ModelFile modelFile = switch(half) {
				case UPPER -> topModel;
				case LOWER -> bottomModel;
			};
			return ConfiguredModel.builder().modelFile(modelFile).build();
		});
	}

	private void tripleHighBlock(Block block, ModelFile topModel, ModelFile middleModel, ModelFile bottomModel) {
		getVariantBuilder(block).forAllStates(state -> {
			PlantopiaTripleBlockHalf half = state.getValue(PlantopiaTriplePlantBlock.HALF);
			ModelFile modelFile = switch(half) {
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

			ModelFile model = existingModel(baseName + "_" + value);

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
		VariantBlockStateBuilder builder = getVariantBuilder(block);

		for(T value : property.getPossibleValues()) {
			if(modelFileIndex == modelFiles.length) break;
			ModelFile modelFile = modelFiles[modelFileIndex++];

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
			ModelFile modelFile = resolver.getModelFile(value);

			builder
				.partialState().with(property, value).modelForState()
				.modelFile(modelFile).addModel();
		}
	}

	/* BLOCK MODELS ******************************************/

	@Contract("_ -> new")
	private @NotNull ModelFile existingModel(String name) {
		return models().getExistingFile(new PlantopiaIdentifier(name));
	}

	private ModelFile blockModel(@NotNull Block block) {
		ResourceLocation location = locationOf(block);
		return models().getExistingFile(new ResourceLocation(location.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + location.getPath()));
	}

	private ModelFile cubeAllModel(String name, ResourceLocation texture) {
		return models().cubeAll(name, texture);
	}

	private ModelFile crossModel(String name, ResourceLocation crossTexture, boolean tinted) {
		if(tinted) return tintedCrossModel(name, crossTexture);
		return crossModel(name, crossTexture);
	}

	private ModelFile crossModel(String name, ResourceLocation crossTexture) {
		return models().cross(name, crossTexture);
	}

	private ModelFile tintedCrossModel(String name, ResourceLocation crossTexture) {
		return models().withExistingParent(name, "tinted_cross")
			.texture("cross", crossTexture);
	}

	private ModelFile flowerPotCrossModel(String name, ResourceLocation plantTexture, boolean tinted) {
		if(tinted) return tintedFlowerPotCrossModel(name, plantTexture);
		return flowerPotCrossModel(name, plantTexture);
	}

	private ModelFile flowerPotCrossModel(String name, ResourceLocation plantTexture) {
		return models().withExistingParent(name, "flower_pot_cross")
			.texture("plant", plantTexture);
	}

	private ModelFile tintedFlowerPotCrossModel(String name, ResourceLocation plantTexture) {
		return models().withExistingParent(name, parent("tinted_flower_pot_cross"))
			.texture("plant", plantTexture);
	}

	private ModelFile tintedFlowerPotCrossWithOverlayModel(String name, ResourceLocation plantTexture, ResourceLocation overlayTexture) {
		return models().withExistingParent(name, parent("tinted_flower_pot_cross_with_overlay"))
			.texture("plant", plantTexture)
			.texture("overlay", overlayTexture);
	}

	private ModelFile tintedCrossWithOverlayModel(String name, ResourceLocation crossTexture, ResourceLocation overlayTexture) {
		return models().withExistingParent(name, parent("tinted_cross_with_overlay"))
			.texture("cross", crossTexture)
			.texture("overlay", overlayTexture);
	}

	private ModelFile giantFernTemplateModel(String name, ResourceLocation crossTexture) {
		return models().withExistingParent(name, parent("template_giant_fern"))
			.texture("cross", crossTexture);
	}

	private ModelFile invertedTintedCrossWithOverlayModel(String name, ResourceLocation crossTexture, ResourceLocation overlayTexture) {
		return models().withExistingParent(name, parent("inverted_tinted_cross_with_overlay"))
			.texture("cross", crossTexture)
			.texture("overlay", overlayTexture);
	}

	private ModelFile wideCrossLeafModel(String name, ResourceLocation leftTexture, ResourceLocation rightTexture) {
		return models().withExistingParent(name, parent("wide_cross_leaf"))
			.texture("right", rightTexture)
			.texture("left", leftTexture);
	}

	private ModelFile cloverBlossomTemplateModel(String name, ResourceLocation blossomTexture) {
		return models().withExistingParent(name, parent("template_clover_blossom"))
			.texture("blossom", blossomTexture);
	}

	private ModelFile pottedCloverBlossomTemplateModel(String name, ResourceLocation blossomTexture) {
		return models().withExistingParent(name, parent("template_potted_clover_blossom"))
			.texture("blossom", blossomTexture);
	}

	private ModelFile oneCobblestoneShardTemplateModel(String name, ResourceLocation shardsTexture) {
		return models().withExistingParent(name, parent("template_one_cobblestone_shard"))
			.texture("shards", shardsTexture);
	}

	private ModelFile twoCobblestoneShardsTemplateModel(String name, ResourceLocation shardsTexture) {
		return models().withExistingParent(name, parent("template_two_cobblestone_shards"))
			.texture("shards", shardsTexture);
	}

	private ModelFile threeCobblestoneShardsTemplateModel(String name, ResourceLocation shardsTexture) {
		return models().withExistingParent(name, parent("template_three_cobblestone_shards"))
			.texture("shards", shardsTexture);
	}

	private ModelFile fourCobblestoneShardsTemplateModel(String name, ResourceLocation shardsTexture) {
		return models().withExistingParent(name, parent("template_four_cobblestone_shards"))
			.texture("shards", shardsTexture);
	}

	private ModelFile foxgloveTopTemplateModel(String name, ResourceLocation crossTexture, ResourceLocation flowersTexture) {
		return models().withExistingParent(name, parent("template_foxglove_top"))
			.texture("cross", crossTexture)
			.texture("flowers", flowersTexture);
	}

	private ModelFile hollyhockTopTemplateModel(String name, ResourceLocation crossTexture, ResourceLocation flowersTexture) {
		return models().withExistingParent(name, parent("template_hollyhock_top"))
			.texture("cross", crossTexture)
			.texture("flowers", flowersTexture);
	}

	private ModelFile hollyhockBottomTemplateModel(String name, ResourceLocation crossTexture, ResourceLocation flowersTexture) {
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
		return new PlantopiaIdentifier(ModelProvider.BLOCK_FOLDER + "/" + name);
	}

	@Contract("_ -> new")
	private static @NotNull ResourceLocation itemTexture(String name) {
		return new PlantopiaIdentifier(ModelProvider.ITEM_FOLDER + "/" + name);
	}

	@Contract("_ -> new")
	private static @NotNull ResourceLocation parent(String name) {
		return new PlantopiaIdentifier(ModelProvider.BLOCK_FOLDER + "/" + name);
	}
}