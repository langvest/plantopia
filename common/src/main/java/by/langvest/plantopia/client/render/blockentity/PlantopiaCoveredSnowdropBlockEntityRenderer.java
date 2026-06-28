package by.langvest.plantopia.client.render.blockentity;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.blockentity.special.PlantopiaCoveredSnowdropBlockEntity;
import by.langvest.plantopia.client.render.PlantopiaRenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class PlantopiaCoveredSnowdropBlockEntityRenderer implements BlockEntityRenderer<PlantopiaCoveredSnowdropBlockEntity> {
    public PlantopiaCoveredSnowdropBlockEntityRenderer(@SuppressWarnings("unused") BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(PlantopiaCoveredSnowdropBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        var level = blockEntity.getLevel();

        if (level == null) return;
        if (blockEntity.skipFlowerRendering()) return;

        var renderHelper = Plantopia.getPlatform().getRenderHelper();
        var blockRenderer = Minecraft.getInstance().getBlockRenderer();
        var pos = blockEntity.getBlockPos();
        var state = blockEntity.getFlowerBlock().defaultBlockState();
        var model = blockRenderer.getBlockModel(state);
        var renderType = PlantopiaRenderType.cutoutNoCrumbling();
        var vec3 = state.getOffset(level, pos);
        boolean checkSides = true;

        poseStack.pushPose();
        poseStack.translate(vec3.x, vec3.y, vec3.z);

        renderHelper.renderBlockModel(
            state,
            model,
            level,
            pos,
            poseStack,
            buffer.getBuffer(renderType),
            checkSides,
            level.getRandom(),
            state.getSeed(pos),
            packedOverlay,
            Optional.of(renderType),
            Optional.of(false)
        );

        poseStack.popPose();
    }
}
