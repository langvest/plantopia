package by.langvest.plantopia.client.render;

import by.langvest.toolkit.event.client.RegisterLayerDefinitionsEvent;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Supplier;

public class PlantopiaEntityLayerDefinitions {
    private static final Set<Pair<ModelLayerLocation, Supplier<LayerDefinition>>> DEFINITIONS = Sets.newHashSet();

    public static void registerLayerDefinition(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier) {
        DEFINITIONS.add(Pair.of(layerLocation, supplier));
    }

    public static void setup(RegisterLayerDefinitionsEvent.@NotNull EntityEvent event) {
        event.registerAll(DEFINITIONS);
    }
}
