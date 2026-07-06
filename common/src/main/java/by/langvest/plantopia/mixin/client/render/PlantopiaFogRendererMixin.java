package by.langvest.plantopia.mixin.client.render;

import by.langvest.plantopia.block.special.PlantopiaQuicksandBlock;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Arrays;

import static by.langvest.plantopia.util.helper.PlantopiaColorHelper.*;

@Mixin(FogRenderer.class)
public abstract class PlantopiaFogRendererMixin {
    @Shadow
    private static float fogRed;
    @Shadow
    private static float fogGreen;
    @Shadow
    private static float fogBlue;
    @Shadow
    private static long biomeChangedTime;

    @Inject(
        method = "setupColor(Lnet/minecraft/client/Camera;FLnet/minecraft/client/multiplayer/ClientLevel;IF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Camera;getEntity()Lnet/minecraft/world/entity/Entity;",
            ordinal = 0
        ),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private static void setupColor(Camera camera, float partialTicks, ClientLevel level, int renderDistanceChunks, float bossColorModifier, CallbackInfo ci, FogType fogType) {
        if (fogType == FogType.POWDER_SNOW && plantopia$isCameraInsideQuicksand(camera, level)) {
            var dustColor = PlantopiaQuicksandBlock.DUST_COLOR;
            fogRed = red(dustColor);
            fogGreen = green(dustColor);
            fogBlue = blue(dustColor);
            biomeChangedTime = -1L;
            RenderSystem.clearColor(fogRed, fogGreen, fogBlue, 0.0F);
            ci.cancel();
        }
    }

    @Unique
    private static boolean plantopia$isCameraInsideQuicksand(@NotNull Camera camera, ClientLevel level) {
        var nearPlane = camera.getNearPlane();

        for (var vec3 : Arrays.asList(nearPlane.forward, nearPlane.getTopLeft(), nearPlane.getTopRight(), nearPlane.getBottomLeft(), nearPlane.getBottomRight())) {
            var vec31 = camera.getPosition().add(vec3);
            var pos = BlockPos.containing(vec31);
            var state = level.getBlockState(pos);

            if (state.getBlock() instanceof PlantopiaQuicksandBlock) return true;
        }

        return false;
    }
}
