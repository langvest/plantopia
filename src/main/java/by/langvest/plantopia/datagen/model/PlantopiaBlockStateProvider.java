package by.langvest.plantopia.datagen.model;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaQuarter;
import by.langvest.plantopia.block.PlantopiaTripleBlockHalf;
import by.langvest.plantopia.block.special.*;
import by.langvest.plantopia.meta.PlantopiaMetaRegistries;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.meta.property.PlantopiaModelType;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import static by.langvest.plantopia.util.helper.PlantopiaContentHelper.pottedBlockOf;
import static by.langvest.plantopia.util.helper.PlantopiaContentHelper.pottedNameOf;
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
		cobblestoneShardPetBlock(PlantopiaBlocks.COBBLESTONE_SHARD_PET.get());
		cobblestoneShardPetBlock(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD_PET.get());
		birchBaseBlock(PlantopiaBlocks.BIRCH_BASE_LOG.get());
		birchBaseBlock(PlantopiaBlocks.BIRCH_BASE_WOOD.get());
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
		tallLupineBlock(PlantopiaBlocks.TALL_RED_LUPINE.get());
		tallLupineBlock(PlantopiaBlocks.TALL_YELLOW_LUPINE.get());
		tallLupineBlock(PlantopiaBlocks.TALL_WHITE_LUPINE.get());
		tallLupineBlock(PlantopiaBlocks.TALL_PINK_LUPINE.get());
		tallLupineBlock(PlantopiaBlocks.TALL_PURPLE_LUPINE.get());
		tallLupineBlock(PlantopiaBlocks.TALL_BLUE_LUPINE.get());
		pollinatedDandelionBlock(PlantopiaBlocks.POLLINATED_DANDELION.get());
		hogweedBlock(PlantopiaBlocks.HOGWEED.get());
		infestedDirtBlock(PlantopiaBlocks.INFESTED_DIRT.get());
		infestedGrassBlock(PlantopiaBlocks.INFESTED_GRASS_BLOCK.get());
		thornyShrubBlock(PlantopiaBlocks.THORNY_SHRUB.get());
		quicksandBlock(PlantopiaBlocks.QUICKSAND.get());
		quicksandCauldronBlock(PlantopiaBlocks.QUICKSAND_CAULDRON.get());
	}

	private void generateAll() {
		PlantopiaMetaRegistries.BLOCKS.forEach(blockMeta -> {
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

			if(block instanceof BushBlock || type.isSimplePlantLike()) {
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

		if(blockMeta.shouldGenerateItem()) blockItemModel(baseName, model);
		simpleBlock(blockMeta.getBlock(), model);
	}

	private void flowerPotBlock(@NotNull PlantopiaBlockMeta blockMeta) {
		String baseName = blockMeta.getName();
		var block = (FlowerPotBlock)blockMeta.getBlock();
		var plant = block.getContent();
		var plantMeta = PlantopiaMetaRegistries.BLOCKS.getValue(plant);

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

		if(blockMeta.shouldGenerateItem()) generatedItemModel(baseName, topTexture);
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

		if(blockMeta.shouldGenerateItem()) generatedItemModel(baseName, topTexture);
		tripleHighBlock(blockMeta.getBlock(), topModel, middleModel, bottomModel);
	}

	private void bushBlock(@NotNull PlantopiaBlockMeta blockMeta) {
		String baseName = blockMeta.getName();
		boolean isTinted = blockMeta.isTinted();

		var texture = texture(baseName);

		var model = crossModel(baseName, texture, isTinted);

		if(blockMeta.shouldGenerateItem()) generatedItemModel(baseName, texture);
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

	private void cobblestoneShardPetBlock(Block block) {
		var originalBlock = ((PlantopiaCobblestoneShardPetBlock)block).getOriginalBlock();
		String baseName = nameOf(block);
		String originalBaseName = nameOf(originalBlock);

		var shardsTexture = texture(originalBaseName + "s");

		var model = cobblestoneShardPetTemplateModel(baseName, shardsTexture);

		getVariantBuilder(block).forAllStatesExcept(state -> {
			var facing = state.getValue(PlantopiaCobblestoneShardPetBlock.FACING);

			return ConfiguredModel.builder()
				.modelFile(model)
				.rotationY(((int) facing.toYRot() + 180) % 360)
				.build();
			},
			PlantopiaCobblestoneShardPetBlock.WATERLOGGED
		);
	}

	private void birchBaseBlock(Block block) {
		String baseName = nameOf(block);
		var blockMeta = PlantopiaMetaRegistries.BLOCKS.getValue(block);

		var topTexture = minecraftTexture("birch_log_top");
		var sideTexture = texture("birch_base_log");
		var bottomTexture = texture(baseName + "_bottom");

		if(blockMeta != null && blockMeta.getType().equals(MetaType.WOOD)) {
			topTexture = minecraftTexture("birch_log");
		}

		var model = cubeBottomTopModel(baseName, topTexture, sideTexture, bottomTexture);

		blockItemModel(baseName, model);
		directionalBlock(block, model);
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

	private void tallLupineBlock(Block block) {
		String baseName = nameOf(block);

		var topTexture = texture(baseName + "_top");
		var bottomTexture = texture(baseName + "_bottom");

		var topModel = tallLupineTemplateModel(baseName + "_top", topTexture);
		var bottomModel = tallLupineTemplateModel(baseName + "_bottom", bottomTexture);

		generatedItemModel(baseName, topTexture);
		doubleHighBlock(block, topModel, bottomModel);
	}

	private void pollinatedDandelionBlock(Block block) {
		var model = blockModel(Blocks.DANDELION);

		simpleBlock(block, model);
	}

	private void infestedDirtBlock(Block block) {
		String baseName = nameOf(block);

		var texture = texture(baseName);

		var model = cubeAllModel(baseName, texture);

		blockItemModel(baseName, model);
		rotatedBlock(block, model);
	}

	private void infestedGrassBlock(Block block) {
		String baseName = nameOf(block);

		var topTexture = minecraftTexture(nameOf(Blocks.GRASS_BLOCK) + "_top");
		var snowySideTexture = texture(baseName + "_snow");
		var bottomTexture = texture(nameOf(PlantopiaBlocks.INFESTED_DIRT));

		var model = existingModel(baseName);

		var snowyModel = cubeBottomTopModel(baseName + "_snow", topTexture, snowySideTexture, bottomTexture)
			.texture("particle", bottomTexture);

		blockItemModel(baseName, model);

		getVariantBuilder(block)
			.partialState().with(PlantopiaInfestedGrassBlock.SNOWY, false).modelForState()
			.modelFile(model).nextModel()
			.modelFile(model).rotationY(90).nextModel()
			.modelFile(model).rotationY(180).nextModel()
			.modelFile(model).rotationY(270).addModel()
			.partialState().with(PlantopiaInfestedGrassBlock.SNOWY, true).modelForState()
			.modelFile(snowyModel).addModel();
	}

	private void thornyShrubBlock(Block block) {
		String baseName = nameOf(block);

		var texture = texture(baseName);

		var model = cubeCrossModel(baseName, texture);

		generatedItemModel(baseName, texture);
		simpleBlock(block, model);
	}

	private void quicksandBlock(Block block) {
		String baseName = nameOf(block);

		var model = existingModel(baseName);

		rotatedBlock(block, model);
	}

	private void quicksandCauldronBlock(Block block) {
		var contentBlock = ((PlantopiaQuicksandCauldronBlock)block).getContentBlock();
		String baseName = nameOf(block);

		var contentTexture = texture(nameOf(contentBlock));

		variableBlock(block, LayeredCauldronBlock.LEVEL, level -> {
			BlockModelBuilder model;

			if(level == LayeredCauldronBlock.MAX_FILL_LEVEL) {
				model = models().withExistingParent(baseName + "_full", "template_cauldron_full");
			} else {
				model = models().withExistingParent(baseName + "_level" + level, "template_cauldron_level" + level);
			}

			return model.texture("content", contentTexture);
		});
	}

	private void hogweedBlock(Block block) {
		String baseName = nameOf(block);

		var topLeftTexture = texture(baseName + "_top_left");
		var topRightTexture = texture(baseName + "_top_right");
		var middleLeftTexture = texture(baseName + "_middle_left");
		var middleRightTexture = texture(baseName + "_middle_right");
		var bottomLeftTexture = texture(baseName + "_bottom_left");
		var bottomRightTexture = texture(baseName + "_bottom_right");

		var itemTexture = itemTexture(baseName);

		var topModel = wideCrossLeafModel(baseName + "_top", topLeftTexture, topRightTexture);
		var middleModel = wideCrossLeafModel(baseName + "_middle", middleLeftTexture, middleRightTexture);
		var bottomModel = wideCrossLeafModel(baseName + "_bottom", bottomLeftTexture, bottomRightTexture);

		generatedItemModel(baseName, itemTexture);

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

	private void rotatedBlock(Block block, ModelFile modelFile) {
		getVariantBuilder(block)
			.partialState().modelForState()
			.modelFile(modelFile).nextModel()
			.modelFile(modelFile).rotationY(90).nextModel()
			.modelFile(modelFile).rotationY(180).nextModel()
			.modelFile(modelFile).rotationY(270).addModel();
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

	private BlockModelBuilder cubeCrossModel(String name, ResourceLocation crossTexture) {
		return models().withExistingParent(name, parent("cube_cross"))
			.texture("cross", crossTexture);
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

	private BlockModelBuilder cobblestoneShardPetTemplateModel(String name, ResourceLocation shardsTexture) {
		return models().withExistingParent(name, parent("template_cobblestone_shard_pet"))
			.texture("shards", shardsTexture);
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

	private BlockModelBuilder tallLupineTemplateModel(String name, ResourceLocation crossTexture) {
		return models().withExistingParent(name, parent("template_tall_lupine"))
			.texture("cross", crossTexture);
	}

	private BlockModelBuilder cubeBottomTopModel(String name, ResourceLocation topTexture, ResourceLocation sideTexture, ResourceLocation bottomTexture) {
		return models().withExistingParent(name, "cube_bottom_top")
			.texture("top", topTexture)
			.texture("side", sideTexture)
			.texture("bottom", bottomTexture);
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
	private static @NotNull ResourceLocation minecraftTexture(String name) {
		return minecraftLocationFrom(ModelProvider.BLOCK_FOLDER, name);
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