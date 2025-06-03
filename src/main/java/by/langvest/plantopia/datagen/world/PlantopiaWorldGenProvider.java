package by.langvest.plantopia.datagen.world;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.entity.PlantopiaDamageTypes;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatures;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacements;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class PlantopiaWorldGenProvider extends DatapackBuiltinEntriesProvider {
	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
		.add(Registries.CONFIGURED_FEATURE, PlantopiaFeatures::bootstrap)
		.add(Registries.PLACED_FEATURE, PlantopiaPlacements::bootstrap)
		.add(Registries.DAMAGE_TYPE, PlantopiaDamageTypes::bootstrap);

	public PlantopiaWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, BUILDER, Set.of(Plantopia.MOD_ID));
	}
}
