package by.langvest.plantopia.entity;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.entity.special.PlantopiaCobblestoneShardProjectileEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class PlantopiaEntities {
	private static final DeferredRegister<EntityType<?>> ENTITY_TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Plantopia.MOD_ID);

	public static final RegistryObject<EntityType<PlantopiaCobblestoneShardProjectileEntity>> COBBLESTONE_SHARD = registerEntity("cobblestone_shard", () -> EntityType.Builder.<PlantopiaCobblestoneShardProjectileEntity>of(PlantopiaCobblestoneShardProjectileEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));

	public static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier) {
		return ENTITY_TYPE_REGISTER.register(name, () -> supplier.get().build(name));
	}

	public static void setup(IEventBus eventBus) {
		ENTITY_TYPE_REGISTER.register(eventBus);
	}
}
