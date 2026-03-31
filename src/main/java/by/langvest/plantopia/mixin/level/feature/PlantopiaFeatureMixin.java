package by.langvest.plantopia.mixin.level.feature;

import net.minecraft.world.level.levelgen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Feature.class)
public abstract class PlantopiaFeatureMixin {
    @ModifyConstant(
        method = "markAboveForPostProcessing(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/core/BlockPos;)V",
        constant = @Constant(intValue = 2)
    )
    public int markAboveForPostProcessing(int constant) {
        return 3;
    }
}
