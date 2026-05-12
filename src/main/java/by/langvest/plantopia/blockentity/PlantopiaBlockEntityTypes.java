package by.langvest.plantopia.blockentity;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.blockentity.special.*;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
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
import java.util.stream.Collectors;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaBlockEntityTypes {
	public static final RegistryObject<BlockEntityType<PlantopiaCobblestoneShardPetBlockEntity>> COBBLESTONE_SHARD_PET = registerBlockEntityType("cobblestone_shard_pet", PlantopiaCobblestoneShardPetBlockEntity::new, () -> Set.of(
		PlantopiaBlocks.COBBLESTONE_SHARD_PET.get(),
		PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD_PET.get()
	));

	public static final RegistryObject<BlockEntityType<PlantopiaSeaShellBlockEntity>> SEA_SHELL = registerBlockEntityType("sea_shell", PlantopiaSeaShellBlockEntity::new, () -> Set.of(
		PlantopiaBlocks.ROUND_SEA_SHELL.get(),
		PlantopiaBlocks.TWISTY_SEA_SHELL.get(),
		PlantopiaBlocks.TUBE_SEA_SHELL.get()
	));

	public static final RegistryObject<BlockEntityType<PlantopiaCoveredSnowdropBlockEntity>> COVERED_SNOWDROP = registerBlockEntityType("covered_snowdrop", PlantopiaCoveredSnowdropBlockEntity::new, () -> Set.of(
		PlantopiaBlocks.COVERED_SNOWDROP.get()
	));

	public static final RegistryObject<BlockEntityType<PlantopiaFrozenReedBlockEntity>> FROZEN_REED = registerBlockEntityType("frozen_reed", PlantopiaFrozenReedBlockEntity::new, () -> Set.of(
		PlantopiaBlocks.FROZEN_REED.get()
	));

	public static final RegistryObject<BlockEntityType<PlantopiaSignBlockEntity>> SIGN = registerBlockEntityType("sign", PlantopiaSignBlockEntity::new, () ->
		PlantopiaMetaBuckets.BLOCK
			.findAll(candidate -> candidate.getType().instanceOf(MetaType.SIGN))
			.stream()
			.map(Supplier::get)
			.collect(Collectors.toSet())
	);

	public static final RegistryObject<BlockEntityType<PlantopiaHangingSignBlockEntity>> HANGING_SIGN = registerBlockEntityType("hanging_sign", PlantopiaHangingSignBlockEntity::new, () ->
		PlantopiaMetaBuckets.BLOCK
			.findAll(candidate -> candidate.getType().instanceOf(MetaType.HANGING_SIGN))
			.stream()
			.map(Supplier::get)
			.collect(Collectors.toSet())
	);

	public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBlockEntityType(String name, BlockEntityType.BlockEntitySupplier<T> supplier, @NotNull Supplier<Set<Block>> blocks) {
		return registerBlockEntityType(plantopia(name), supplier, blocks);
	}

	public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBlockEntityType(ResourceLocation identifier, BlockEntityType.BlockEntitySupplier<T> supplier, @NotNull Supplier<Set<Block>> blocks) {
		return PlantopiaRegistries.BLOCK_ENTITY_TYPE.register(identifier, () -> {
			Block[] validBlocks = blocks.get().toArray(Block[]::new);

			return BlockEntityType.Builder.of(supplier, validBlocks).build(null);
		});
	}

	public static void setup(@NotNull RegisterEvent event) {
		event.registerAll(Registries.BLOCK_ENTITY_TYPE, PlantopiaRegistries.BLOCK_ENTITY_TYPE);
	}
}
