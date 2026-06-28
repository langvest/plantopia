package by.langvest.plantopia.mixin.client;

import by.langvest.plantopia.util.helper.PlantopiaTickHelper;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class PlantopiaMinecraftMixin {
    @Inject(
        method = "tick()V",
        at = @At("HEAD")
    )
    private void tick(CallbackInfo ci) {
        PlantopiaTickHelper.tick();
    }
}
