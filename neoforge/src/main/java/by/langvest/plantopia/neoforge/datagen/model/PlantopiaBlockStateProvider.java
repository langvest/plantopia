package by.langvest.plantopia.neoforge.datagen.model;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.*;
import by.langvest.plantopia.block.special.*;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaContentHelper.pottedBlockOf;
import static by.langvest.plantopia.util.helper.PlantopiaContentHelper.pottedNameOf;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.*;

public class PlantopiaBlockStateProvider extends BlockStateProvider {
    private static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");
    private static final ExistingFileHelper.ResourceType MODEL = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".json", "models");
    private static final Set<Direction> HORIZONTAL_DIRECTIONS = ImmutableSet.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
    private static final int ANGLE_OFFSET = 180;
    private final ExistingFileHelper existingFileHelper;

    public PlantopiaBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Plantopia.MOD_ID, existingFileHelper);
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {
        generateAll();

        pottedFernBlock(Blocks.POTTED_FERN);
        cattailBlock(PlantopiaBlocks.CATTAIL.get());
        giantFernBlock(PlantopiaBlocks.GIANT_FERN.get());
        cloverBlock(PlantopiaBlocks.CLOVER.get());
        azollaBlock(PlantopiaBlocks.AZOLLA.get());
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
        mallowBlock(PlantopiaBlocks.RED_MALLOW.get());
        mallowBlock(PlantopiaBlocks.PURPLE_MALLOW.get());
        mallowBlock(PlantopiaBlocks.YELLOW_MALLOW.get());
        mallowBlock(PlantopiaBlocks.WHITE_MALLOW.get());
        mallowBlock(PlantopiaBlocks.PINK_MALLOW.get());
        mallowBlock(PlantopiaBlocks.MAGENTA_MALLOW.get());
        lupineBlock(PlantopiaBlocks.RED_LUPINE.get());
        lupineBlock(PlantopiaBlocks.YELLOW_LUPINE.get());
        lupineBlock(PlantopiaBlocks.WHITE_LUPINE.get());
        lupineBlock(PlantopiaBlocks.PINK_LUPINE.get());
        lupineBlock(PlantopiaBlocks.PURPLE_LUPINE.get());
        lupineBlock(PlantopiaBlocks.BLUE_LUPINE.get());
        pollinatedDandelionBlock(PlantopiaBlocks.POLLINATED_DANDELION.get());
        hogweedBlock(PlantopiaBlocks.HOGWEED.get());
        infestedDirtBlock(PlantopiaBlocks.INFESTED_DIRT.get());
        infestedGrassBlock(PlantopiaBlocks.INFESTED_GRASS_BLOCK.get());
        branchingShrubBlock(PlantopiaBlocks.BRANCHING_SHRUB.get());
        thornyShrubBlock(PlantopiaBlocks.THORNY_SHRUB.get());
        quicksandBlock(PlantopiaBlocks.QUICKSAND.get());
        quicksandCauldronBlock(PlantopiaBlocks.QUICKSAND_CAULDRON.get());
        seaMossCarpetBlock(PlantopiaBlocks.SEA_MOSS_CARPET.get());
        smallPlatterleafBlock(PlantopiaBlocks.SMALL_PLATTERLEAF.get());
        bigPlatterleafBlock(PlantopiaBlocks.BIG_PLATTERLEAF.get());
        sedgeBlock(PlantopiaBlocks.SEDGE.get());
        seaweedBlock(PlantopiaBlocks.SEAWEED.get());
        snowdropBlock(PlantopiaBlocks.SNOWDROP.get());
        coveredSnowdropBlock(PlantopiaBlocks.COVERED_SNOWDROP.get());
        frozenReedsBlock(PlantopiaBlocks.FROZEN_REED.get());
        luckyDaisyBlock(PlantopiaBlocks.WHITE_LUCKY_DAISY.get());
        luckyDaisyBlock(PlantopiaBlocks.PINK_LUCKY_DAISY.get());
        tinyCactusBlock(PlantopiaBlocks.TINY_CACTUS.get());
        tinyCactusBlock(PlantopiaBlocks.FLOWERING_TINY_CACTUS.get());
        icicleBlock(PlantopiaBlocks.ICICLE.get());
        iceCrustBlock(PlantopiaBlocks.ICE_CRUST.get());
        bushWithOverlayBlock(PlantopiaBlocks.FLUFFY_GRASS.get());
        bushWithOverlayBlock(PlantopiaBlocks.TALL_FLUFFY_GRASS.get());
        bushWithOverlayBlock(PlantopiaBlocks.SPIKY_GRASS.get());
        bushWithOverlayBlock(PlantopiaBlocks.TALL_SPIKY_GRASS.get());
        treeFruitBlock(PlantopiaBlocks.BIRCH_CATKIN.get());
        treeFruitBlock(PlantopiaBlocks.PINECONE.get());

        checkAll();
    }

    private void generateAll() {
        PlantopiaMetaBuckets.BLOCK.forEach(blockMeta -> {
            if (!blockMeta.shouldGenerateModel()) return;

            var block = blockMeta.get();
            var type = blockMeta.getType();

            if (block instanceof PlantopiaLeafLitterBlock) {
                leafLitterBlock(blockMeta);
                return;
            }

            if (block instanceof HugeMushroomBlock) {
                hugeMushroomBlock(blockMeta);
                return;
            }

            if (block instanceof PlantopiaHerbBlock) {
                herbBlock(blockMeta);
                return;
            }

            if (block instanceof PlantopiaSeaShellBlock) {
                seaShellBlock(blockMeta);
                return;
            }

            if (block instanceof PlantopiaHangingMossBlock) {
                hangingMossBlock(blockMeta);
                return;
            }

            if (block instanceof PlantopiaWaterlilyFlowerBlock) {
                waterlilyFlowerBlock(blockMeta);
                return;
            }

            if (block instanceof PlantopiaFloweringWaterlilyBlock) {
                floweringWaterlilyBlock(blockMeta);
                return;
            }

            if (block instanceof FlowerPotBlock) {
                flowerPotBlock(blockMeta);
                return;
            }

            if (block instanceof PlantopiaTriplePlantBlock) {
                triplePlantBlock(blockMeta);
                return;
            }

            if (block instanceof DoublePlantBlock) {
                doublePlantBlock(blockMeta);
                return;
            }

            if (block instanceof BushBlock || type.instanceOf(MetaType.PLANT)) {
                bushBlock(blockMeta);
                return;
            }

            if (block instanceof RotatedPillarBlock) {
                rotatedPillarBlock(blockMeta);
                return;
            }

            if (block instanceof StairBlock) {
                stairsBlock(blockMeta);
                return;
            }

            if (block instanceof SlabBlock) {
                slabBlock(blockMeta);
                return;
            }

            if (block instanceof FenceBlock) {
                fenceBlock(blockMeta);
                return;
            }

            if (block instanceof FenceGateBlock) {
                fenceGateBlock(blockMeta);
                return;
            }

            if (block instanceof DoorBlock) {
                doorBlock(blockMeta);
                return;
            }

            if (block instanceof TrapDoorBlock) {
                trapdoorBlock(blockMeta);
                return;
            }

            if (block instanceof PressurePlateBlock) {
                pressurePlateBlock(blockMeta);
                return;
            }

            if (block instanceof ButtonBlock) {
                buttonBlock(blockMeta);
                return;
            }

            if (type.isSignLike()) {
                signBlock(blockMeta);
                return;
            }

            simpleBlock(blockMeta);
        });
    }

    private void checkAll() {
        PlantopiaMetaBuckets.BLOCK.forEach(blockMeta -> {
            var name = blockMeta.getName();
            var namespace = blockMeta.getNamespace();
            var type = blockMeta.getType();

            if (type == MetaType.POTTED) {
                var modelLocation = locationFrom(namespace, ModelProvider.BLOCK_FOLDER, name);

                if (!isModelExists(modelLocation)) {
                    throw new IllegalStateException("Model '" + modelLocation + "' is not presented!");
                }
            }
        });
    }

    /* MODELS GENERATION ******************************************/

    private void generatedBlockItem(@NotNull PlantopiaBlockMeta blockMeta, Supplier<ItemModelBuilder> fallbackModel) {
        if (!blockMeta.shouldGenerateItem()) return;

        String baseName = blockMeta.getName();

        var customItemTexture = itemTexture(baseName);

        if (isTextureExists(customItemTexture)) {
            generatedItemModel(baseName, customItemTexture);
        } else {
            fallbackModel.get();
        }
    }

    private void simpleBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();

        var texture = texture(baseName);

        var model = cubeAllModel(baseName, texture);

        generatedBlockItem(blockMeta, () -> blockItemModel(baseName, model));
        simpleBlock(blockMeta.get(), model);
    }

    private void rotatedPillarBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();
        RotatedPillarBlock block = (RotatedPillarBlock) blockMeta.get();
        var isWood = blockMeta.getType().equals(MetaType.WOOD);
        var parentBlock = blockMeta.getParent();

        var endTexture = texture(baseName + "_top");
        var sideTexture = isWood && parentBlock != null ? blockTexture(parentBlock) : texture(baseName);

        if (isWood) {
            endTexture = sideTexture;
        }

        axisBlock(block, sideTexture, endTexture);
        blockItemModel(baseName, blockModelLocation(block));
    }

    private void stairsBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();
        StairBlock block = (StairBlock) blockMeta.get();
        var parentBlock = blockMeta.getParent();

        var texture = parentBlock != null ? blockTexture(parentBlock) : texture(baseName);

        stairsBlock(block, texture);
        blockItemModel(baseName, blockModelLocation(block));
    }

    private void slabBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();
        SlabBlock block = (SlabBlock) blockMeta.get();
        var parentBlock = blockMeta.getParent();

        var texture = parentBlock != null ? blockTexture(parentBlock) : texture(baseName);

        var doubleSlabModel = parentBlock != null ? blockModel(parentBlock) : cubeAllModel(baseName + "_double", texture);

        slabBlock(block, doubleSlabModel.getLocation(), texture);
        blockItemModel(baseName, blockModelLocation(block));
    }

    private void fenceBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();
        FenceBlock block = (FenceBlock) blockMeta.get();
        var parentBlock = blockMeta.getParent();

        var texture = parentBlock != null ? blockTexture(parentBlock) : texture(baseName);

        itemModels().fenceInventory(baseName, texture);
        fenceBlock(block, texture);
    }

    private void fenceGateBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();
        FenceGateBlock block = (FenceGateBlock) blockMeta.get();
        var parentBlock = blockMeta.getParent();

        var texture = parentBlock != null ? blockTexture(parentBlock) : texture(baseName);

        fenceGateBlock(block, texture);
        blockItemModel(baseName, blockModelLocation(block));
    }

    private void doorBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();
        DoorBlock block = (DoorBlock) blockMeta.get();

        var itemTexture = itemTexture(baseName);
        var topTexture = texture(baseName + "_top");
        var bottomTexture = texture(baseName + "_bottom");

        generatedItemModel(baseName, itemTexture);
        doorBlock(block, bottomTexture, topTexture);
    }

    private void trapdoorBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();
        TrapDoorBlock block = (TrapDoorBlock) blockMeta.get();

        var orientable = blockMeta.getType().instanceOf(MetaType.WOODEN_TRAPDOOR);
        var texture = texture(baseName);

        trapdoorBlock(block, texture, orientable);
        blockItemModel(baseName, blockModelLocation(block).withSuffix("_bottom"));
    }

    private void pressurePlateBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();
        PressurePlateBlock block = (PressurePlateBlock) blockMeta.get();
        var parentBlock = blockMeta.getParent();

        var texture = parentBlock != null ? blockTexture(parentBlock) : texture(baseName);

        pressurePlateBlock(block, texture);
        blockItemModel(baseName, blockModelLocation(block));
    }

    private void buttonBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();
        ButtonBlock block = (ButtonBlock) blockMeta.get();
        var parentBlock = blockMeta.getParent();

        var texture = parentBlock != null ? blockTexture(parentBlock) : texture(baseName);

        itemModels().buttonInventory(baseName, texture);
        buttonBlock(block, texture);
    }

    private void signBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();
        var block = blockMeta.get();
        var parentBlock = blockMeta.getParent();
        var isWall = block instanceof WallSignBlock || block instanceof WallHangingSignBlock;
        var baseSignName = isWall ? baseName.replace("_wall_", "_") : baseName;

        var particleTexture = parentBlock != null ? blockTexture(parentBlock) : texture(baseName);

        var modelLocation = blockModelLocation(plantopia(baseSignName));

        ModelFile model;
        if (isModelExists(modelLocation)) {
            model = existingModel(modelLocation);
        } else {
            model = models().sign(baseSignName, particleTexture);
        }

        simpleBlock(block, model);
    }

    private void flowerPotBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();
        var block = (FlowerPotBlock) blockMeta.get();
        var plant = block.getContent();
        var shouldGeneratePlantModel = metaOf(plant).map(PlantopiaBlockMeta::shouldGenerateModel).orElse(true);

        if (!shouldGeneratePlantModel) return;

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

        generatedBlockItem(blockMeta, () -> generatedItemModel(baseName, topTexture));
        doubleHighBlock(blockMeta.get(), topModel, bottomModel);
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

        generatedBlockItem(blockMeta, () -> generatedItemModel(baseName, topTexture));
        tripleHighBlock(blockMeta.get(), topModel, middleModel, bottomModel);
    }

    private void bushBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();
        boolean isTinted = blockMeta.isTinted();

        var texture = texture(baseName);

        var model = crossModel(baseName, texture, isTinted);

        generatedBlockItem(blockMeta, () -> generatedItemModel(baseName, texture));
        simpleBlock(blockMeta.get(), model);
    }

    private void waterlilyFlowerBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();

        var flowerTexture = texture(baseName);
        var overlayTexture = texture("lily_pad_flower_overlay");

        var model = models().withExistingParent(baseName, parent("template_waterlily"))
            .texture("flower", flowerTexture);

        waterlilyFlowerTemplateItemModel(baseName, flowerTexture, overlayTexture);
        simpleBlock(blockMeta.get(), model);
    }

    private void floweringWaterlilyBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();
        var waterlilyBlock = (PlantopiaFloweringWaterlilyBlock) blockMeta.get();
        var flowerBlock = waterlilyBlock.getFlowerBlock();
        var originBlock = waterlilyBlock.getOriginBlock();

        var flowerTexture = texture(nameOf(flowerBlock));

        var model = models().withExistingParent(baseName, parent("template_flowering_" + nameOf(originBlock)))
            .texture("flower", flowerTexture);

        rotatedBlock(blockMeta.get(), model);
    }

    private void seaShellBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();

        var model = existingPlantopiaModel(baseName);
        var itemTexture = itemTexture(baseName);
        var itemTextureOverlay = itemTexture(baseName + "_overlay");

        generatedItemModel(baseName, itemTexture, itemTextureOverlay);

        getVariantBuilder(blockMeta.get())
            .forAllStatesExcept(
                state -> ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + ANGLE_OFFSET) % 360)
                    .build(),
                BlockStateProperties.WATERLOGGED
            );
    }

    private void hangingMossBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();

        var baseTexture = texture(baseName);
        var tipTexture = texture(baseName + "_tip");

        var baseModel = crossModel(baseName, baseTexture);
        var tipModel = crossModel(baseName + "_tip", tipTexture);

        generatedItemModel(baseName, baseTexture);

        getVariantBuilder(blockMeta.get())
            .forAllStatesExcept(
                state -> ConfiguredModel.builder()
                    .modelFile(state.getValue(PlantopiaHangingMossBlock.TIP) ? tipModel : baseModel)
                    .build(),
                BlockStateProperties.WATERLOGGED
            );
    }

    private void leafLitterBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();

        var texture = texture(baseName);

        generatedItemModel(baseName, texture);
        directionalMultipartBlock(
            blockMeta.get(),
            PlantopiaLeafLitterBlock.AMOUNT,
            value -> models().withExistingParent(baseName + "_" + value, parent("template_leaf_litter_" + value))
                .texture("texture", texture)
        );
    }

    private void hugeMushroomBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();

        var texture = texture(baseName);

        var model = singleFaceTemplateModel(baseName, texture);
        var insideModel = existingMinecraftModel("mushroom_block_inside");

        if (blockMeta.hasItem()) {
            var inventoryModel = cubeAllModel(baseName + "_inventory", texture);

            blockItemModel(baseName, inventoryModel);
        }

        var builder = getMultipartBuilder(blockMeta.get());

        builder.part().modelFile(model).rotationX(270).uvLock(true).addModel().condition(BlockStateProperties.UP, true);
        builder.part().modelFile(insideModel).rotationX(270).addModel().condition(BlockStateProperties.UP, false);
        builder.part().modelFile(model).rotationX(90).uvLock(true).addModel().condition(BlockStateProperties.DOWN, true);
        builder.part().modelFile(insideModel).rotationX(90).addModel().condition(BlockStateProperties.DOWN, false);
        builder.part().modelFile(model).addModel().condition(BlockStateProperties.NORTH, true);
        builder.part().modelFile(insideModel).addModel().condition(BlockStateProperties.NORTH, false);
        builder.part().modelFile(model).rotationY(180).uvLock(true).addModel().condition(BlockStateProperties.SOUTH, true);
        builder.part().modelFile(insideModel).rotationY(180).addModel().condition(BlockStateProperties.SOUTH, false);
        builder.part().modelFile(model).rotationY(90).uvLock(true).addModel().condition(BlockStateProperties.EAST, true);
        builder.part().modelFile(insideModel).rotationY(90).addModel().condition(BlockStateProperties.EAST, false);
        builder.part().modelFile(model).rotationY(270).uvLock(true).addModel().condition(BlockStateProperties.WEST, true);
        builder.part().modelFile(insideModel).rotationY(270).addModel().condition(BlockStateProperties.WEST, false);
    }

    private void herbBlock(@NotNull PlantopiaBlockMeta blockMeta) {
        String baseName = blockMeta.getName();

        var topTexture = texture(baseName + "_top");
        var topFlowersTexture = texture(baseName + "_top_flowers");
        var bottomTexture = texture(baseName + "_bottom");
        var bottomFlowersTexture = texture(baseName + "_bottom_flowers");

        var topModelLocation = plantopia(ModelProvider.BLOCK_FOLDER, baseName + "_top");
        var bottomModelLocation = plantopia(ModelProvider.BLOCK_FOLDER, baseName + "_bottom");

        ModelFile topModel;
        ModelFile bottomModel;

        if (isModelExists(topModelLocation)) {
            topModel = existingModel(topModelLocation);
        } else {
            topModel = invertedTintedCrossWithOverlayModel(baseName + "_top", topTexture, topFlowersTexture);
        }

        if (isModelExists(bottomModelLocation)) {
            bottomModel = existingModel(bottomModelLocation);
        } else if (isTextureExists(bottomFlowersTexture)) {
            bottomModel = tintedCrossWithOverlayModel(baseName + "_bottom", bottomTexture, bottomFlowersTexture);
        } else {
            bottomModel = tintedCrossModel(baseName + "_bottom", bottomTexture);
        }

        generatedItemModel(baseName, topTexture, topFlowersTexture);
        doubleHighBlock(blockMeta.get(), topModel, bottomModel);
    }

    /* CUSTOM MODELS GENERATION ******************************************/

    private void icicleBlock(Block block) {
        String baseName = nameOf(block);

        getVariantBuilder(block).forAllStatesExcept(state -> {
            var direction = state.getValue(PlantopiaIcicleBlock.TIP_DIRECTION);
            var thickness = state.getValue(PlantopiaIcicleBlock.THICKNESS);
            String suffix = "_" + direction + "_" + thickness;
            var texture = texture(baseName + "_" + thickness);
            var model = crossWithAOModel(baseName + suffix, direction, texture);

            return ConfiguredModel.builder()
                .modelFile(model)
                .build();
        }, PlantopiaIcicleBlock.WATERLOGGED);
    }

    private void bushWithOverlayBlock(Block block) {
        String baseName = nameOf(block);

        var plantTexture = texture(baseName);
        var overlayTexture = texture(baseName + "_overlay");

        var model = tintedCrossWithOverlayModel(baseName, plantTexture, overlayTexture);

        generatedItemModel(baseName, plantTexture, overlayTexture);
        simpleBlock(block, model);
    }

    private void treeFruitBlock(Block block) {
        String baseName = nameOf(block);

        var texture = texture(baseName);
        var itemTexture = itemTexture(baseName);
        var model = crossWithAOModel(baseName, Direction.UP, texture);

        generatedItemModel(baseName, itemTexture);
        simpleBlock(block, model);
    }

    private void iceCrustBlock(Block block) {
        String baseName = nameOf(block);
        var itemModel = existingPlantopiaModel(baseName + "_inventory");
        var faceModel = existingPlantopiaModel(baseName);
        var floatingFaceModel = existingPlantopiaModel(baseName + "_floating");

        blockItemModel(baseName, itemModel);

        var builder = getMultipartBuilder(block);

        builder.part().modelFile(faceModel).addModel()
            .condition(BlockStateProperties.NORTH, true);
        builder.part().modelFile(faceModel).rotationY(90).uvLock(true).addModel()
            .condition(BlockStateProperties.EAST, true);
        builder.part().modelFile(faceModel).rotationY(180).uvLock(true).addModel()
            .condition(BlockStateProperties.SOUTH, true);
        builder.part().modelFile(faceModel).rotationY(270).uvLock(true).addModel()
            .condition(BlockStateProperties.WEST, true);
        builder.part().modelFile(faceModel).rotationX(270).uvLock(true).addModel()
            .condition(BlockStateProperties.UP, true);
        builder.part().modelFile(faceModel).rotationX(90).uvLock(true).addModel()
            .condition(BlockStateProperties.DOWN, true)
            .condition(PlantopiaIceCrustBlock.FLOATING, false);
        builder.part().modelFile(floatingFaceModel).addModel()
            .condition(BlockStateProperties.DOWN, true)
            .condition(PlantopiaIceCrustBlock.FLOATING, true);
    }

    private void cattailBlock(Block block) {
        String baseName = nameOf(block);

        var topTexture = texture(baseName + "_top");
        var topOverlayTexture = texture(baseName + "_top_overlay");
        var bottomTexture = texture(baseName + "_bottom");
        var bottomOverlayTexture = texture(baseName + "_bottom_overlay");

        var topModel = tintedCrossWithOverlayModel(baseName + "_top", topTexture, topOverlayTexture);
        var bottomModel = tintedCrossWithOverlayModel(baseName + "_bottom", bottomTexture, bottomOverlayTexture);

        generatedItemModel(baseName, topTexture, topOverlayTexture);
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

    private void sedgeBlock(Block block) {
        String baseName = nameOf(block);

        var topTexture = texture(baseName + "_top");
        var topOverlayTexture = texture(baseName + "_top_overlay");
        var middleTexture = texture(baseName + "_middle");
        var bottomTexture = texture(baseName + "_bottom");

        var topModel = sedgeWithOverlayTemplateModel(baseName + "_top", topTexture, topOverlayTexture);
        var middleModel = sedgeTemplateModel(baseName + "_middle", middleTexture);
        var bottomModel = sedgeTemplateModel(baseName + "_bottom", bottomTexture);

        generatedItemModel(baseName, topTexture, topOverlayTexture);
        tripleHighBlock(block, topModel, middleModel, bottomModel);
    }

    private void cloverBlock(Block block) {
        String baseName = nameOf(block);

        generatedItemModel(baseName, itemTexture(baseName));
        directionalMultipartBlock(block, PlantopiaCloverBlock.AMOUNT);
    }

    private void azollaBlock(Block block) {
        String baseName = nameOf(block);

        generatedItemModel(baseName, texture(baseName));
        directionalMultipartBlock(block, PlantopiaAzollaBlock.AMOUNT);
    }

    private void bigCloverBlock(Block block) {
        String baseName = nameOf(block);

        generatedItemModel(baseName, texture(baseName));
        simpleBlock(block, existingPlantopiaModel(baseName));

        pottedBlockOf(block).ifPresent(pottedBlock -> {
            simpleBlock(pottedBlock, existingPlantopiaModel(nameOf(pottedBlock)));
        });
    }

    private void pottedFernBlock(Block block) {
        if (!(block instanceof FlowerPotBlock flowerPotBlock)) return;

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

        pottedBlockOf(block).ifPresent(pottedBlock -> {
            var pottedModel = pottedCloverBlossomTemplateModel(nameOf(pottedBlock), blossomTexture);
            simpleBlock(pottedBlock, pottedModel);
        });
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
        rotatedVariableBlock(block, PlantopiaCobblestoneShardBlock.AMOUNT, oneShardModel, twoShardsModel, threeShardsModel, fourShardsModel);
    }

    private void tinyCactusBlock(Block block) {
        String baseName = nameOf(block);

        var texture = texture(baseName);
        var normalModel = crossModel(baseName, texture);
        var attachedModel = attachedTinyCactusTemplateModel("attached_" + baseName, texture);

        generatedItemModel(baseName, texture);

        getVariantBuilder(block).forAllStates(state -> {
            var facing = state.getValue(PlantopiaBlockStateProperties.CACTUS_FACING);

            if (facing == Direction.UP) {
                return ConfiguredModel.builder().modelFile(normalModel).build();
            }

            return ConfiguredModel.builder()
                .modelFile(attachedModel)
                .rotationX(90)
                .rotationY(((int) facing.toYRot() + ANGLE_OFFSET) % 360)
                .build();
        });

        pottedBlockOf(block).ifPresent(pottedBlock -> {
            simpleBlock(pottedBlock, flowerPotCrossModel(nameOf(pottedBlock), texture));
        });
    }

    private void cobblestoneShardPetBlock(Block block) {
        var originalBlock = ((PlantopiaCobblestoneShardPetBlock) block).getOriginBlock();
        String baseName = nameOf(block);
        String originalBaseName = nameOf(originalBlock);

        var shardsTexture = texture(originalBaseName + "s");

        var model = cobblestoneShardPetTemplateModel(baseName, shardsTexture);

        getVariantBuilder(block).forAllStatesExcept(state -> {
                var facing = state.getValue(PlantopiaCobblestoneShardPetBlock.FACING);

                return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY(((int) facing.toYRot() + ANGLE_OFFSET) % 360)
                    .build();
            },
            PlantopiaCobblestoneShardPetBlock.WATERLOGGED
        );
    }

    private void birchBaseBlock(Block block) {
        String baseName = nameOf(block);
        var isWood = metaOf(block).map(blockMeta -> blockMeta.getType().equals(MetaType.WOOD)).orElse(false);

        var topTexture = minecraftTexture("birch_log_top");
        var sideTexture = texture("birch_base_log");
        var bottomTexture = texture(baseName + "_bottom");

        if (isWood) {
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

    private void mallowBlock(Block block) {
        String baseName = nameOf(block);

        var topTexture = texture("mallow_top");
        var bottomTexture = texture("mallow_bottom");
        var flowersTopTexture = texture(baseName + "_flowers_top");
        var flowersBottomTexture = texture(baseName + "_flowers_bottom");

        var topModel = mallowTopTemplateModel(baseName + "_top", topTexture, flowersTopTexture);
        var bottomModel = mallowBottomTemplateModel(baseName + "_bottom", bottomTexture, flowersBottomTexture);

        generatedItemModel(baseName, flowersTopTexture);
        doubleHighBlock(block, topModel, bottomModel);
    }

    private void lupineBlock(Block block) {
        String baseName = nameOf(block);

        var topTexture = texture(baseName + "_top");
        var bottomTexture = texture(baseName + "_bottom");

        var topModel = lupineTemplateModel(baseName + "_top", topTexture);
        var bottomModel = lupineTemplateModel(baseName + "_bottom", bottomTexture);

        generatedItemModel(baseName, topTexture);
        doubleHighBlock(block, topModel, bottomModel);
    }

    private void luckyDaisyBlock(Block block) {
        String baseName = nameOf(block);

        var fullPetalsTexture = texture(baseName + "_petals_" + PlantopiaLuckyDaisyBlock.MAX_PETALS);
        var stemModel = existingPlantopiaModel("lucky_daisy_stem");

        getVariantBuilder(block).forAllStates(state -> {
            var amount = state.getValue(PlantopiaLuckyDaisyBlock.AMOUNT);

            ModelFile model = stemModel;

            if (amount > 0) {
                var petalsTexture = texture(baseName + "_petals_" + amount);

                model = luckyDaisyTemplateModel(baseName + "_" + amount, petalsTexture)
                    .texture("particle", fullPetalsTexture);
            }

            return ConfiguredModel.builder()
                .modelFile(model).nextModel()
                .modelFile(model).rotationY(90).nextModel()
                .modelFile(model).rotationY(180).nextModel()
                .modelFile(model).rotationY(270).build();
        });

        pottedBlockOf(block).ifPresent(pottedBlock -> {
            var pottedBaseName = nameOf(pottedBlock);
            var pottedPetalsTexture = texture(pottedBaseName + "_petals");
            var pottedModel = pottedLuckyDaisyTemplateModel(pottedBaseName, pottedPetalsTexture);

            simpleBlock(pottedBlock, pottedModel);
        });
    }

    private void pollinatedDandelionBlock(Block block) {
        var model = blockModel(Blocks.DANDELION);

        simpleBlock(block, model);
    }

    private void seaweedBlock(Block block) {
        String baseName = nameOf(block);

        var model = existingPlantopiaModel(baseName);

        blockItemModel(baseName, model);
        horizontalBlock(block, model);
    }

    private void snowdropBlock(Block block) {
        String baseName = nameOf(block);

        var plantTexture = texture(baseName);
        var model = existingPlantopiaModel(baseName);

        generatedItemModel(baseName, plantTexture);
        simpleBlock(block, model);

        pottedBlockOf(block).ifPresent(pottedBlock -> {
            var pottedModel = flowerPotCrossModel(nameOf(pottedBlock), plantTexture);
            simpleBlock(pottedBlock, pottedModel);
        });
    }

    private void coveredSnowdropBlock(Block block) {
        getVariantBuilder(block).forAllStates(state -> {
            int layers = state.getValue(PlantopiaCoveredSnowdropBlock.LAYERS);

            if (layers == PlantopiaCoveredSnowdropBlock.MAX_HEIGHT) {
                return ConfiguredModel.builder().modelFile(blockModel(Blocks.SNOW_BLOCK)).build();
            }

            var snowModel = existingMinecraftModel(nameOf(Blocks.SNOW) + "_height" + (layers * 2));

            return ConfiguredModel.builder().modelFile(snowModel).build();
        });
    }

    private void frozenReedsBlock(Block block) {
        var iceModel = blockModel(Blocks.ICE);

        simpleBlock(block, iceModel);
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

        var model = existingPlantopiaModel(baseName);

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

    private void branchingShrubBlock(Block block) {
        String baseName = nameOf(block);

        var baseTexture = texture(baseName + "_base");

        generatedItemModel(baseName, baseTexture);

        getVariantBuilder(block).forAllStatesExcept(state -> {
            boolean isBase = state.getValue(PlantopiaBranchingShrubBlock.BASE);
            var facing = state.getValue(PlantopiaBranchingShrubBlock.FACING);
            String type = isBase ? "base" : "body";
            var direction = facing == Direction.DOWN ? Direction.DOWN : Direction.UP;
            String suffix = "_" + direction + "_" + type;

            var texture = texture(baseName + "_" + type);
            var model = crossWithAOModel(baseName + suffix, direction, texture);

            if (facing == Direction.DOWN) {
                return ConfiguredModel.builder()
                    .modelFile(model)
                    .build();
            }

            return ConfiguredModel.builder()
                .modelFile(model)
                .rotationX(facing.getAxis().isHorizontal() ? 90 : 0)
                .rotationY(facing.getAxis().isVertical() ? 0 : (((int) facing.toYRot()) + ANGLE_OFFSET) % 360)
                .build();
        }, BlockStateProperties.WATERLOGGED);

        pottedBlockOf(block).ifPresent(pottedBlock -> {
            var pottedName = nameOf(pottedBlock);
            var pottedTexture = texture(pottedName);
            var pottedModel = flowerPotCrossModel(pottedName, pottedTexture);
            simpleBlock(pottedBlock, pottedModel);
        });
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

        var model = existingPlantopiaModel(baseName);

        rotatedBlock(block, model);
    }

    private void quicksandCauldronBlock(Block block) {
        var contentBlock = ((PlantopiaQuicksandCauldronBlock) block).getContentBlock();
        String baseName = nameOf(block);

        var contentTexture = texture(nameOf(contentBlock));

        variableBlock(block, LayeredCauldronBlock.LEVEL, level -> {
            BlockModelBuilder model;

            if (level == LayeredCauldronBlock.MAX_FILL_LEVEL) {
                model = models().withExistingParent(baseName + "_full", "template_cauldron_full");
            } else {
                model = models().withExistingParent(baseName + "_level" + level, "template_cauldron_level" + level);
            }

            return model.texture("content", contentTexture);
        });
    }

    private void seaMossCarpetBlock(Block block) {
        String baseName = nameOf(block);

        var texture = blockTexture(PlantopiaBlocks.SEA_MOSS_BLOCK.get());
        var model = carpetTemplateModel(baseName, texture);

        blockItemModel(baseName, model);
        simpleBlock(block, model);
    }

    private void smallPlatterleafBlock(Block block) {
        String baseName = nameOf(block);

        var model = existingPlantopiaModel(baseName);

        rotatedBlock(block, model);
    }

    private void bigPlatterleafBlock(Block block) {
        String baseName = nameOf(block);

        var model = existingPlantopiaModel(baseName);

        getVariantBuilder(block).forAllStates(state -> {
            PlantopiaQuarter quarter = state.getValue(PlantopiaBigPlatterleafBlock.QUARTER);

            int rotation = switch (quarter) {
                case SOUTH_WEST -> 0;
                case WEST_NORTH -> 90;
                case NORTH_EAST -> 180;
                case EAST_SOUTH -> 270;
            };

            return ConfiguredModel.builder().modelFile(model).rotationY(rotation).build();
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

            var modelFile = switch (half) {
                case UPPER -> topModel;
                case CENTRAL -> middleModel;
                case LOWER -> bottomModel;
            };

            int rotation = switch (quarter) {
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
        getVariantBuilder(block).forAllStatesExcept(state -> {
            DoubleBlockHalf half = state.getValue(DoublePlantBlock.HALF);

            var modelFile = switch (half) {
                case UPPER -> topModel;
                case LOWER -> bottomModel;
            };

            return ConfiguredModel.builder().modelFile(modelFile).build();
        }, BlockStateProperties.WATERLOGGED);
    }

    private void tripleHighBlock(Block block, ModelFile topModel, ModelFile middleModel, ModelFile bottomModel) {
        getVariantBuilder(block).forAllStatesExcept(state -> {
            PlantopiaTripleBlockHalf half = state.getValue(PlantopiaTriplePlantBlock.HALF);

            var modelFile = switch (half) {
                case UPPER -> topModel;
                case CENTRAL -> middleModel;
                case LOWER -> bottomModel;
            };

            return ConfiguredModel.builder().modelFile(modelFile).build();
        }, BlockStateProperties.WATERLOGGED);
    }

    private void directionalMultipartBlock(Block block, @NotNull IntegerProperty property) {
        String baseName = nameOf(block);
        directionalMultipartBlock(block, property, value -> existingPlantopiaModel(baseName + "_" + value));
    }

    private void directionalMultipartBlock(Block block, @NotNull IntegerProperty property, Function<Integer, ModelFile> modelFactory) {
        Integer maxValue = Collections.max(property.getPossibleValues());
        var builder = getMultipartBuilder(block);

        property.getPossibleValues().forEach(value -> {
            ArrayList<Integer> values = Lists.newArrayList();

            for (int i = value; i <= maxValue; i++) values.add(i);

            var model = modelFactory.apply(value);

            HORIZONTAL_DIRECTIONS.forEach(direction ->
                builder.part()
                    .modelFile(model).rotationY((((int) direction.toYRot()) + ANGLE_OFFSET) % 360).addModel()
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

    private <T extends Comparable<T>> void rotatedVariableBlock(Block block, @NotNull Property<T> property, ModelFile... modelFiles) {
        int modelFileIndex = 0;
        var builder = getVariantBuilder(block);

        for (T value : property.getPossibleValues()) {
            if (modelFileIndex == modelFiles.length) break;

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

        for (T value : property.getPossibleValues()) {
            var modelFile = resolver.getModelFile(value);

            builder
                .partialState().with(property, value).modelForState()
                .modelFile(modelFile).addModel();
        }
    }

    /* BLOCK MODELS ******************************************/

    @Contract("_ -> new")
    private @NotNull ModelFile.ExistingModelFile existingPlantopiaModel(String name) {
        return existingModel(plantopia(name));
    }

    @Contract("_ -> new")
    private @NotNull ModelFile.ExistingModelFile existingMinecraftModel(String name) {
        return models().getExistingFile(minecraft(name));
    }

    @Contract("_ -> new")
    private @NotNull ModelFile.ExistingModelFile existingModel(ResourceLocation location) {
        return models().getExistingFile(location);
    }

    private @NotNull ResourceLocation blockModelLocation(@NotNull Block block) {
        return blockModelLocation(locationOf(block));
    }

    private @NotNull ResourceLocation blockModelLocation(@NotNull ResourceLocation location) {
        return locationFrom(location.getNamespace(), ModelProvider.BLOCK_FOLDER, location.getPath());
    }

    private ModelFile.ExistingModelFile blockModel(@NotNull Block block) {
        return models().getExistingFile(blockModelLocation(block));
    }

    private BlockModelBuilder cubeAllModel(String name, ResourceLocation texture) {
        return models().cubeAll(name, texture);
    }

    private BlockModelBuilder crossModel(String name, ResourceLocation crossTexture, boolean tinted) {
        if (tinted) return tintedCrossModel(name, crossTexture);
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

    private BlockModelBuilder singleFaceTemplateModel(String name, ResourceLocation texture) {
        return models().withExistingParent(name, "template_single_face")
            .texture("texture", texture);
    }

    private BlockModelBuilder elevatedSingleFaceTemplateModel(String name, ResourceLocation texture) {
        return models().withExistingParent(name, parent("template_elevated_single_face"))
            .texture("texture", texture);
    }

    private BlockModelBuilder flowerPotCrossModel(String name, ResourceLocation plantTexture, boolean tinted) {
        if (tinted) return tintedFlowerPotCrossModel(name, plantTexture);
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

    private BlockModelBuilder tintedCrossWithTintedOverlayModel(String name, ResourceLocation crossTexture, ResourceLocation overlayTexture) {
        return models().withExistingParent(name, parent("tinted_cross_with_tinted_overlay"))
            .texture("cross", crossTexture)
            .texture("overlay", overlayTexture);
    }

    private BlockModelBuilder giantFernTemplateModel(String name, ResourceLocation crossTexture) {
        return models().withExistingParent(name, parent("template_giant_fern"))
            .texture("cross", crossTexture);
    }

    private BlockModelBuilder attachedTinyCactusTemplateModel(String name, ResourceLocation crossTexture) {
        return models().withExistingParent(name, parent("template_attached_tiny_cactus"))
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

    private BlockModelBuilder crossWithAOModel(String name, Direction direction, ResourceLocation crossTexture) {
        return models().withExistingParent(name, parent("cross_with_ao_" + direction))
            .texture("cross", crossTexture);
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

    private BlockModelBuilder mallowTopTemplateModel(String name, ResourceLocation crossTexture, ResourceLocation flowersTexture) {
        return models().withExistingParent(name, parent("template_mallow_top"))
            .texture("cross", crossTexture)
            .texture("flowers", flowersTexture);
    }

    private BlockModelBuilder mallowBottomTemplateModel(String name, ResourceLocation crossTexture, ResourceLocation flowersTexture) {
        return models().withExistingParent(name, parent("template_mallow_bottom"))
            .texture("cross", crossTexture)
            .texture("flowers", flowersTexture);
    }

    private BlockModelBuilder lupineTemplateModel(String name, ResourceLocation crossTexture) {
        return models().withExistingParent(name, parent("template_lupine"))
            .texture("cross", crossTexture);
    }

    private BlockModelBuilder sedgeTemplateModel(String name, ResourceLocation crossTexture) {
        return models().withExistingParent(name, parent("template_sedge"))
            .texture("cross", crossTexture);
    }

    private BlockModelBuilder sedgeWithOverlayTemplateModel(String name, ResourceLocation crossTexture, ResourceLocation overlayTexture) {
        return models().withExistingParent(name, parent("template_sedge_with_overlay"))
            .texture("cross", crossTexture)
            .texture("overlay", overlayTexture);
    }

    private BlockModelBuilder carpetTemplateModel(String name, ResourceLocation woolTexture) {
        return models().withExistingParent(name, "carpet")
            .texture("wool", woolTexture);
    }

    private BlockModelBuilder luckyDaisyTemplateModel(String name, ResourceLocation petalsTexture) {
        return models().withExistingParent(name, parent("template_lucky_daisy"))
            .texture("petals", petalsTexture);
    }

    private BlockModelBuilder pottedLuckyDaisyTemplateModel(String name, ResourceLocation petalsTexture) {
        return models().withExistingParent(name, parent("template_potted_lucky_daisy"))
            .texture("petals", petalsTexture);
    }

    private BlockModelBuilder cubeBottomTopModel(String name, ResourceLocation topTexture, ResourceLocation sideTexture, ResourceLocation bottomTexture) {
        return models().withExistingParent(name, "cube_bottom_top")
            .texture("top", topTexture)
            .texture("side", sideTexture)
            .texture("bottom", bottomTexture);
    }

    /* ITEM MODELS ******************************************/

    @Contract("_ -> new")
    private @NotNull ModelFile.ExistingModelFile existingItemModel(String name) {
        return itemModels().getExistingFile(plantopia(name));
    }

    public ItemModelBuilder generatedItemModel(String name, ResourceLocation... layers) {
        var itemModel = itemModels().withExistingParent(name, "generated");

        int layerIndex = 0;
        if (layers != null)
            for (ResourceLocation layeredTexture : layers) itemModel.texture("layer" + layerIndex++, layeredTexture);

        return itemModel;
    }

    public ItemModelBuilder blockItemModel(String name, @NotNull ModelFile modelFile) {
        return blockItemModel(name, modelFile.getLocation());
    }

    public ItemModelBuilder blockItemModel(String name, ResourceLocation modelLocation) {
        return itemModels().withExistingParent(name, modelLocation);
    }

    @SuppressWarnings("UnusedReturnValue")
    private ItemModelBuilder waterlilyFlowerTemplateItemModel(String name, ResourceLocation flowerTexture, ResourceLocation overlayTexture) {
        return itemModels().withExistingParent(name, itemParent("template_waterlily"))
            .texture("layer0", flowerTexture)
            .texture("layer1", overlayTexture);
    }

    /* HELPER METHODS ******************************************/

    private boolean isTextureExists(@NotNull ResourceLocation texture) {
        return existingFileHelper.exists(texture, TEXTURE);
    }

    private boolean isModelExists(@NotNull ResourceLocation model) {
        return existingFileHelper.exists(model, MODEL);
    }

    @Contract("_ -> new")
    private static @NotNull ResourceLocation texture(String name) {
        return plantopia(ModelProvider.BLOCK_FOLDER, name);
    }

    @Contract("_ -> new")
    private static @NotNull ResourceLocation minecraftTexture(String name) {
        return minecraft(ModelProvider.BLOCK_FOLDER, name);
    }

    @Contract("_ -> new")
    private static @NotNull ResourceLocation itemTexture(String name) {
        return plantopia(ModelProvider.ITEM_FOLDER, name);
    }

    @Contract("_ -> new")
    private static @NotNull ResourceLocation parent(String name) {
        return plantopia(ModelProvider.BLOCK_FOLDER, name);
    }

    @Contract("_ -> new")
    private static @NotNull ResourceLocation itemParent(String name) {
        return plantopia(ModelProvider.ITEM_FOLDER, name);
    }
}
