package by.langvest.plantopia.forge.handler;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.special.PlantopiaQuicksandBlock;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static by.langvest.plantopia.util.helper.PlantopiaColorHelper.*;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Plantopia.MOD_ID)
public final class PlantopiaRenderFogHandler {
    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.@NotNull RenderFog event) {
        var camera = event.getCamera();
        var type = event.getType();

        if (isPowderSnowFog(type) && isCameraInsideQuicksand(camera)) {
            if (camera.getEntity().isSpectator()) {
                event.setNearPlaneDistance(-8.0F);
                event.scaleFarPlaneDistance(0.5F);
            } else {
                event.setNearPlaneDistance(0.0F);
                event.setFarPlaneDistance(1.25F);
            }

            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onComputeFogColor(ViewportEvent.@NotNull ComputeFogColor event) {
        var camera = event.getCamera();
        var r = event.getRed();
        var g = event.getGreen();
        var b = event.getBlue();

        if (isPowderSnowFog(r, g, b) && isCameraInsideQuicksand(camera)) {
            var dustColor = PlantopiaQuicksandBlock.DUST_COLOR;
            event.setRed(red(dustColor));
            event.setGreen(green(dustColor));
            event.setBlue(blue(dustColor));
        }
    }

    /* HELPER METHODS ********************************************************************************/

    private static boolean isCameraInsideQuicksand(@NotNull Camera camera) {
        var entity = camera.getEntity();
        var level = entity.level();
        var nearPlane = camera.getNearPlane();

        for (var vec3 : Arrays.asList(nearPlane.forward, nearPlane.getTopLeft(), nearPlane.getTopRight(), nearPlane.getBottomLeft(), nearPlane.getBottomRight())) {
            var vec31 = camera.getPosition().add(vec3);
            var pos = BlockPos.containing(vec31);
            var state = level.getBlockState(pos);

            if (state.getBlock() instanceof PlantopiaQuicksandBlock) return true;
        }

        return false;
    }

    private static boolean isPowderSnowFog(float red, float green, float blue) {
        return red == 0.623F && green == 0.734F && blue == 0.785F;
    }

    @Contract(pure = true)
    private static boolean isPowderSnowFog(@NotNull FogType type) {
        return type.equals(FogType.POWDER_SNOW);
    }
}
