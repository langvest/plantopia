package by.langvest.plantopia.block.entity.render;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.entity.special.PlantopiaCoveredSnowdropBlockEntity;
import by.langvest.plantopia.client.render.PlantopiaRenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class PlantopiaCoveredSnowdropBlockEntityRenderer implements BlockEntityRenderer<PlantopiaCoveredSnowdropBlockEntity> {
	public PlantopiaCoveredSnowdropBlockEntityRenderer(@SuppressWarnings("unused") BlockEntityRendererProvider.Context context) {}

	@Override
	public void render(@NotNull PlantopiaCoveredSnowdropBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
		var level = blockEntity.getLevel();

		if(level == null) return;
		if(blockEntity.skipFlowerRendering()) return;

		var blockRenderer = Minecraft.getInstance().getBlockRenderer();
		var pos = blockEntity.getBlockPos();
		var snowdropState = PlantopiaBlocks.SNOWDROP.get().defaultBlockState();
		var snowdropModel = blockRenderer.getBlockModel(snowdropState);
		var renderType = PlantopiaRenderType.cutoutNoCrumbling();
		var vec3 = snowdropState.getOffset(level, pos);
		boolean checkSides = true;

		poseStack.pushPose();

		poseStack.translate(vec3.x, vec3.y, vec3.z);

		blockRenderer.getModelRenderer().tesselateWithoutAO(
			level,
			snowdropModel,
			snowdropState,
			pos,
			poseStack,
			buffer.getBuffer(renderType),
			checkSides,
			level.getRandom(),
			snowdropState.getSeed(pos),
			packedOverlay,
			ModelData.EMPTY,
			renderType
		);

		poseStack.popPose();
	}
}
