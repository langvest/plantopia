package by.langvest.plantopia.client.render;

import by.langvest.plantopia.blockentity.PlantopiaBlockEntityTypes;
import by.langvest.plantopia.client.render.blockentity.PlantopiaCobblestoneShardPetBlockEntityRenderer;
import by.langvest.plantopia.client.render.blockentity.PlantopiaCoveredSnowdropBlockEntityRenderer;
import by.langvest.plantopia.client.render.blockentity.PlantopiaFrozenReedBlockEntityRenderer;
import by.langvest.toolkit.event.client.RegisterRenderersEvent;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import org.jetbrains.annotations.NotNull;

public class PlantopiaBlockEntityRenderers {
    public static void setup(RegisterRenderersEvent.@NotNull BlockEntityEvent event) {
        event.register(PlantopiaBlockEntityTypes.COVERED_SNOWDROP.get(), PlantopiaCoveredSnowdropBlockEntityRenderer::new);
        event.register(PlantopiaBlockEntityTypes.FROZEN_REED.get(), PlantopiaFrozenReedBlockEntityRenderer::new);
        event.register(PlantopiaBlockEntityTypes.COBBLESTONE_SHARD_PET.get(), PlantopiaCobblestoneShardPetBlockEntityRenderer::new);
        event.register(PlantopiaBlockEntityTypes.SIGN.get(), SignRenderer::new);
        event.register(PlantopiaBlockEntityTypes.HANGING_SIGN.get(), HangingSignRenderer::new);
    }
}
