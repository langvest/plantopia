package by.langvest.toolkit.platform.client;

import by.langvest.toolkit.platform.Platform;
import by.langvest.toolkit.platform.PlatformHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public abstract class RenderHelper extends PlatformHelper {
    public RenderHelper(Platform platform) {
        super(platform);
    }

    public abstract void renderBlockModel(BlockState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, Optional<RenderType> renderType);

    public abstract void renderBlockModel(BlockState state, BakedModel model, BlockAndTintGetter level, BlockPos pos, PoseStack poseStack, VertexConsumer vertexConsumer, boolean checkSides, RandomSource random, long seed, int packedOverlay, Optional<RenderType> renderType, Optional<Boolean> ambientOcclusion);
}
