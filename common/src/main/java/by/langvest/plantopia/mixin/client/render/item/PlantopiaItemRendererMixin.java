package by.langvest.plantopia.mixin.client.render.item;

import by.langvest.toolkit.client.render.item.CustomItemRenderer;
import by.langvest.toolkit.client.render.item.CustomItemRenderers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class PlantopiaItemRendererMixin {
    @Inject(
        method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void render(@NotNull ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
        if (itemStack.isEmpty()) return;

        var customRenderer = CustomItemRenderers.getCustomRenderer(itemStack.getItem());
        if (customRenderer == null) return;

        customRenderer.render(new CustomItemRenderer.RenderContext(itemStack, displayContext, leftHand, poseStack, buffer, combinedLight, combinedOverlay, model));
        ci.cancel();
    }
}
