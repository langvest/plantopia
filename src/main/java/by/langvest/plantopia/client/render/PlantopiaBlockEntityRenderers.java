package by.langvest.plantopia.client.render;

import by.langvest.plantopia.blockentity.PlantopiaBlockEntities;
import by.langvest.plantopia.client.render.blockentity.PlantopiaCobblestoneShardPetBlockEntityRenderer;
import by.langvest.plantopia.client.render.blockentity.PlantopiaCoveredSnowdropBlockEntityRenderer;
import by.langvest.plantopia.client.render.blockentity.PlantopiaIcyReedsBlockEntityRenderer;
import by.langvest.toolkit.event.client.RegisterRenderersEvent;
import org.jetbrains.annotations.NotNull;

public class PlantopiaBlockEntityRenderers {
    public static void setup(RegisterRenderersEvent.@NotNull BlockEntityEvent event) {
        event.register(PlantopiaBlockEntities.COVERED_SNOWDROP.get(), PlantopiaCoveredSnowdropBlockEntityRenderer::new);
        event.register(PlantopiaBlockEntities.ICY_REEDS.get(), PlantopiaIcyReedsBlockEntityRenderer::new);
        event.register(PlantopiaBlockEntities.COBBLESTONE_SHARD_PET.get(), PlantopiaCobblestoneShardPetBlockEntityRenderer::new);
    }
}
