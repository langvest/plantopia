package by.langvest.plantopia.mixin.client.gui;

import by.langvest.plantopia.block.special.PlantopiaQuicksandBlock;
import by.langvest.plantopia.client.gui.PlantopiaGui;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class PlantopiaGuiMixin {
    @Shadow
    protected abstract Player getCameraPlayer();

    @Inject(
        method = "renderHeart(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Gui$HeartType;IIZZZ)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void renderHeart(GuiGraphics guiGraphics, Gui.HeartType heartType, int x, int y, boolean hardcore, boolean halfHeart, boolean blinking, CallbackInfo ci) {
        if (heartType == Gui.HeartType.NORMAL) {
            var player = getCameraPlayer();

            if (PlantopiaQuicksandBlock.isEntityDrownsInQuicksand(player)) {
                guiGraphics.blitSprite(PlantopiaGui.QUICKSAND_HEART.getSprite(hardcore, halfHeart, blinking), x, y, 9, 9);
                ci.cancel();
            }
        }
    }
}
