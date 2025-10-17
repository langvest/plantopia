package by.langvest.plantopia.blockentity;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.blockentity.special.PlantopiaCobblestoneShardPetBlockEntity;
import by.langvest.plantopia.blockentity.special.PlantopiaSeaShellBlockEntity;
import by.langvest.plantopia.blockentity.special.PlantopiaCoveredSnowdropBlockEntity;
import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaBlockEntities {
	public static final RegistryObject<BlockEntityType<PlantopiaCobblestoneShardPetBlockEntity>> COBBLESTONE_SHARD_PET = registerBlockEntityType("cobblestone_shard_pet", PlantopiaCobblestoneShardPetBlockEntity::new, Set.of(PlantopiaBlocks.COBBLESTONE_SHARD_PET, PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD_PET));
	public static final RegistryObject<BlockEntityType<PlantopiaSeaShellBlockEntity>> SEA_SHELL = registerBlockEntityType("sea_shell", PlantopiaSeaShellBlockEntity::new, Set.of(PlantopiaBlocks.ROUND_SEA_SHELL));
	public static final RegistryObject<BlockEntityType<PlantopiaCoveredSnowdropBlockEntity>> COVERED_SNOWDROP = registerBlockEntityType("covered_snowdrop", PlantopiaCoveredSnowdropBlockEntity::new, Set.of(PlantopiaBlocks.COVERED_SNOWDROP));

	public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBlockEntityType(String name, BlockEntityType.BlockEntitySupplier<T> supplier, @NotNull Set<Supplier<Block>> blocks) {
		return registerBlockEntityType(plantopia(name), supplier, blocks);
	}

	public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBlockEntityType(ResourceLocation identifier, BlockEntityType.BlockEntitySupplier<T> supplier, @NotNull Set<Supplier<Block>> blocks) {
		return PlantopiaRegistries.BLOCK_ENTITY_TYPE.register(identifier, () -> {
			Block[] validBlocks = blocks.stream().map(Supplier::get).toArray(Block[]::new);

			return BlockEntityType.Builder.of(supplier, validBlocks).build(null);
		});
	}

	public static void setup(@NotNull RegisterEvent event) {
		event.registerAll(Registries.BLOCK_ENTITY_TYPE, PlantopiaRegistries.BLOCK_ENTITY_TYPE);
	}
}
