package by.langvest.plantopia.client.render;

import by.langvest.toolkit.event.client.RegisterLayerDefinitionsEvent;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class PlantopiaEntityLayerDefinitions {
    private static final List<Pair<ModelLayerLocation, Supplier<LayerDefinition>>> DEFINITIONS = Lists.newArrayList();

    public static void registerLayerDefinition(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier) {
        DEFINITIONS.add(Pair.of(layerLocation, supplier));
    }

    public static void setup(RegisterLayerDefinitionsEvent.@NotNull EntityEvent event) {
        event.registerAll(DEFINITIONS);
    }
}
