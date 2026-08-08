package by.langvest.plantopia.mixin.client.particle.special;

import by.langvest.plantopia.block.PlantopiaParticleTintableBlock;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.metaOf;

@Mixin(TerrainParticle.class)
public abstract class PlantopiaTerrainParticleMixin extends TextureSheetParticle {
    protected PlantopiaTerrainParticleMixin(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Inject(
        method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V",
        at = @At("RETURN")
    )
    private void init(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, @NotNull BlockState state, BlockPos pos, CallbackInfo ci) {
        @Nullable Integer colorOverride;
        if (state.getBlock() instanceof PlantopiaParticleTintableBlock tintableBlock) {
            colorOverride = tintableBlock.getDestroyParticlesColorOverride(state, level, pos);
        } else {
            colorOverride = metaOf(state.getBlock()).map(blockMeta -> blockMeta.shouldApplyTintToParticles(state, level, pos) ? null : -1).orElse(null);
        }

        if (colorOverride != null) {
            if (colorOverride == -1) {
                this.rCol = 0.6F;
                this.gCol = 0.6F;
                this.bCol = 0.6F;
            } else {
                this.rCol *= (float) (colorOverride >> 16 & 0xFF) / 255.0F;
                this.gCol *= (float) (colorOverride >> 8 & 0xFF) / 255.0F;
                this.bCol *= (float) (colorOverride & 0xFF) / 255.0F;
            }
        }
    }
}
