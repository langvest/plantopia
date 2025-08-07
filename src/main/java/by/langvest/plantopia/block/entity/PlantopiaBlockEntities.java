package by.langvest.plantopia.block.entity;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.entity.special.PlantopiaCobblestoneShardPetBlockEntity;
import by.langvest.plantopia.block.entity.special.PlantopiaSeaShellBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Supplier;

public class PlantopiaBlockEntities {
	private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Plantopia.MOD_ID);

	public static final RegistryObject<BlockEntityType<PlantopiaCobblestoneShardPetBlockEntity>> COBBLESTONE_SHARD_PET = registerType("cobblestone_shard_pet", PlantopiaCobblestoneShardPetBlockEntity::new, Set.of(PlantopiaBlocks.COBBLESTONE_SHARD_PET, PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD_PET));
	public static final RegistryObject<BlockEntityType<PlantopiaSeaShellBlockEntity>> SEA_SHELL = registerType("sea_shell", PlantopiaSeaShellBlockEntity::new, Set.of(PlantopiaBlocks.ROUND_SEA_SHELL));

	public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerType(String name, BlockEntityType.BlockEntitySupplier<T> supplier, @NotNull Set<Supplier<Block>> blocks) {
		return BLOCK_ENTITY_REGISTER.register(name, () -> {
			Block[] validBlocks = blocks.stream().map(Supplier::get).toArray(Block[]::new);

			return BlockEntityType.Builder.of(supplier, validBlocks).build(null);
		});
	}

	public static void setup(IEventBus bus) {
		BLOCK_ENTITY_REGISTER.register(bus);
	}
}
