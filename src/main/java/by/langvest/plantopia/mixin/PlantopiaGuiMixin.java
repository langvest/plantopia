package by.langvest.plantopia.mixin;

import by.langvest.plantopia.block.special.PlantopiaQuicksandBlock;
import by.langvest.plantopia.gui.PlantopiaGui;
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
		method = "renderHeart(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Gui$HeartType;IIIZZ)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void renderHeart(GuiGraphics guiGraphics, Gui.HeartType heartType, int x, int y, int yOffset, boolean renderHighlight, boolean halfHeart, CallbackInfo ci) {
		if(heartType == Gui.HeartType.NORMAL) {
			var player = getCameraPlayer();

			if(PlantopiaQuicksandBlock.isEntityDrownsInQuicksand(player)) {
				if(renderHighlight) {
					ci.cancel();
					return;
				}

				float uOffset = 0;
				float vOffset = 0;

				if(halfHeart) uOffset += 9;
				if(yOffset >= 9) vOffset += 9;

				guiGraphics.blit(PlantopiaGui.QUICKSAND_HEARTS, x, y, 0, uOffset, vOffset, 9, 9, 18, 18);
				ci.cancel();
			}
		}
	}
}
