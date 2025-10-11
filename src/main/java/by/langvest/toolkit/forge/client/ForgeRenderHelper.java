package by.langvest.toolkit.forge.client;

import by.langvest.toolkit.platform.Platform;
import by.langvest.toolkit.platform.client.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ForgeRenderHelper extends RenderHelper {
    public ForgeRenderHelper(Platform platform) {
        super(platform);
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void renderBlockModel(BlockState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, @NotNull Optional<RenderType> renderType) {
        var blockRenderer = Minecraft.getInstance().getBlockRenderer();

        blockRenderer.renderSingleBlock(state, poseStack, bufferSource, packedLight, packedOverlay, ModelData.EMPTY, renderType.orElse(null));
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void renderBlockModel(BlockState state, BakedModel model, BlockAndTintGetter level, BlockPos pos, PoseStack poseStack, VertexConsumer vertexConsumer, boolean checkSides, RandomSource random, long seed, int packedOverlay, Optional<RenderType> renderType, @NotNull Optional<Boolean> ambientOcclusion) {
        var blockModelRenderer = Minecraft.getInstance().getBlockRenderer().getModelRenderer();

        if(ambientOcclusion.isEmpty()) {
            blockModelRenderer.tesselateBlock(level, model, state, pos, poseStack, vertexConsumer, checkSides, random, seed, packedOverlay, ModelData.EMPTY, renderType.orElse(null));
        } else if(ambientOcclusion.get()) {
            blockModelRenderer.tesselateWithAO(level, model, state, pos, poseStack, vertexConsumer, checkSides, random, seed, packedOverlay, ModelData.EMPTY, renderType.orElse(null));
        } else {
            blockModelRenderer.tesselateWithoutAO(level, model, state, pos, poseStack, vertexConsumer, checkSides, random, seed, packedOverlay, ModelData.EMPTY, renderType.orElse(null));
        }
    }
}
