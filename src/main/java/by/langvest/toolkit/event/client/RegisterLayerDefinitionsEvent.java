package by.langvest.toolkit.event.client;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Supplier;

public abstract class RegisterLayerDefinitionsEvent extends ClientEvent {
	public static abstract class EntityEvent extends RegisterLayerDefinitionsEvent {
		public abstract void register(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier);

		public void registerAll(@NotNull Set<Pair<ModelLayerLocation, Supplier<LayerDefinition>>> definitions) {
			for(var definition : definitions) {
				register(definition.getFirst(), definition.getSecond());
			}
		}
	}
}
