package by.langvest.plantopia.entity;

import by.langvest.plantopia.entity.special.PlantopiaCobblestoneShardProjectileEntity;
import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.toolkit.event.RegistryEvent;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopiaLocationFrom;

public class PlantopiaEntities {
	public static final RegistryObject<EntityType<PlantopiaCobblestoneShardProjectileEntity>> COBBLESTONE_SHARD = registerEntityType("cobblestone_shard", () -> EntityType.Builder.<PlantopiaCobblestoneShardProjectileEntity>of(PlantopiaCobblestoneShardProjectileEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));

	public static <T extends Entity> RegistryObject<EntityType<T>> registerEntityType(String name, Supplier<EntityType.Builder<T>> supplier) {
		return registerEntityType(plantopiaLocationFrom(name), supplier);
	}

	public static <T extends Entity> RegistryObject<EntityType<T>> registerEntityType(ResourceLocation identifier, Supplier<EntityType.Builder<T>> supplier) {
		return PlantopiaRegistries.ENTITY_TYPE.register(identifier, () -> supplier.get().build(identifier.getPath()));
	}

	public static void setup(@NotNull RegistryEvent event) {
		event.registerAll(Registries.ENTITY_TYPE, PlantopiaRegistries.ENTITY_TYPE);
	}
}
