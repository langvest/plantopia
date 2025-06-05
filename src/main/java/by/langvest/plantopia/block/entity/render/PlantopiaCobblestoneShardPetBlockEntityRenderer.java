package by.langvest.plantopia.block.entity.render;

import by.langvest.plantopia.block.entity.special.PlantopiaCobblestoneShardPetBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class PlantopiaCobblestoneShardPetBlockEntityRenderer implements BlockEntityRenderer<PlantopiaCobblestoneShardPetBlockEntity> {
	public PlantopiaCobblestoneShardPetBlockEntityRenderer(@SuppressWarnings("unused") BlockEntityRendererProvider.Context context) {}

	@Override
	public void render(@NotNull PlantopiaCobblestoneShardPetBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
		var hitResult = Minecraft.getInstance().hitResult;

		if(!(hitResult instanceof BlockHitResult blockHitResult)) return;
		if(!blockHitResult.getBlockPos().equals(blockEntity.getBlockPos())) return;

		var customName = blockEntity.getCustomName();

		if(customName == null) return;

		var entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		var font = Minecraft.getInstance().font;
		float bgOpacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);

		poseStack.pushPose();
		poseStack.translate(0.5F, 0.6F, 0.5F);
		poseStack.mulPose(entityRenderDispatcher.cameraOrientation());
		poseStack.scale(-0.025F, -0.025F, 0.025F);

		Matrix4f matrix4f = poseStack.last().pose();

		int bgColor = (int)(bgOpacity * 255.0F) << 24;
		float x = (float)(-font.width(customName) / 2);
		float y = (float)(-font.lineHeight / 2);

		font.drawInBatch(customName, x, y, 553648127, false, matrix4f, buffer, Font.DisplayMode.SEE_THROUGH, bgColor, packedLight);
		font.drawInBatch(customName, x, y, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight);

		poseStack.popPose();
	}
}
