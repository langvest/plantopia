package by.langvest.plantopia.client.render;

import by.langvest.plantopia.client.render.entity.PlantopiaBoatEntityRenderer;
import by.langvest.plantopia.entity.PlantopiaEntityTypes;
import by.langvest.toolkit.event.client.RegisterRenderersEvent;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import org.jetbrains.annotations.NotNull;

public class PlantopiaEntityRenderers {
    public static void setup(RegisterRenderersEvent.@NotNull EntityEvent event) {
        event.register(PlantopiaEntityTypes.COBBLESTONE_SHARD.get(), ThrownItemRenderer::new);
        event.register(PlantopiaEntityTypes.BOAT.get(), context -> new PlantopiaBoatEntityRenderer(context, false));
        event.register(PlantopiaEntityTypes.CHEST_BOAT.get(), context -> new PlantopiaBoatEntityRenderer(context, true));
    }
}
