package by.langvest.toolkit.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomItemRenderer {
    default void render(@NotNull RenderContext context) {
        var poseStack = context.poseStack();
        poseStack.pushPose();
        applyTransformations(context);
        renderByItem(context);
        poseStack.popPose();
    }

    default void applyTransformations(@NotNull RenderContext context) {
        var model = context.model();
        if (model == null) return;
        model.getTransforms()
            .getTransform(context.displayContext())
            .apply(context.isLeftHand(), context.poseStack());
    }

    void renderByItem(RenderContext context);

    record RenderContext(
        @NotNull ItemStack itemStack,
        @NotNull ItemDisplayContext displayContext,
        boolean isLeftHand,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource buffer,
        int combinedLight,
        int combinedOverlay,
        @Nullable BakedModel model
    ) {}
}
