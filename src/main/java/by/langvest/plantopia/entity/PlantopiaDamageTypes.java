package by.langvest.plantopia.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopiaLocationFrom;

public class PlantopiaDamageTypes {
	public static final ResourceKey<DamageType> THORNY_SHRUB = createKey("thorny_shrub");

	public static void bootstrap(@NotNull BootstapContext<DamageType> context) {
		register(context, THORNY_SHRUB, new DamageType("thornyShrub", 0.1F, DamageEffects.POKING));
	}

	protected static @NotNull ResourceKey<DamageType> createKey(String name) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, plantopiaLocationFrom(name));
	}

	protected static void register(@NotNull BootstapContext<DamageType> context, ResourceKey<DamageType> key, DamageType damageType) {
		context.register(key, damageType);
	}
}
