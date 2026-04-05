package by.langvest.plantopia.mixin.entity.special;

import by.langvest.plantopia.block.special.PlantopiaIcicleBlock;
import by.langvest.plantopia.block.special.PlantopiaQuicksandBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class PlantopiaAbstractArrowMixin {
    @Shadow
    protected boolean inGround;

    @Unique
    private BlockPos plantopia$lastRicochetPos = null;

    @Redirect(
        method = "tick()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
        )
    )
    private boolean tick$is(BlockState state, @NotNull Block block) {
        if (block instanceof PowderSnowBlock) {
            return state.is(block) || state.getBlock() instanceof PlantopiaQuicksandBlock;
        }

        return state.is(block);
    }

    @Inject(
        method = "onHitBlock(Lnet/minecraft/world/phys/BlockHitResult;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onHitIcicleBlock(@NotNull BlockHitResult hitResult, CallbackInfo ci) {
        var arrow = (AbstractArrow) (Object) this;
        var level = arrow.level();
        var hitPos = hitResult.getBlockPos();
        var hitState = level.getBlockState(hitResult.getBlockPos());
        var hitVector = hitResult.getLocation();

        boolean isSimpleArrow = arrow instanceof Arrow || arrow instanceof SpectralArrow;

        // Проверяем, является ли блок нашей сосулькой
        if (isSimpleArrow && hitState.getBlock() instanceof PlantopiaIcicleBlock) {
            if (hitPos.equals(plantopia$lastRicochetPos)) {
                ci.cancel();
                return;
            }

            plantopia$lastRicochetPos = hitPos;

            Vec3 normal = switch (hitResult.getDirection()) {
                case UP -> new Vec3(0, 1, 0);
                case DOWN -> new Vec3(0, -1, 0);
                case NORTH -> new Vec3(0, 0, -1);
                case SOUTH -> new Vec3(0, 0, 1);
                case WEST -> new Vec3(-1, 0, 0);
                case EAST -> new Vec3(1, 0, 0);
            };

            Vec3 velocity = arrow.getDeltaMovement();
            Vec3 reflection = velocity.subtract(normal.scale(2 * velocity.dot(normal)));
            arrow.setDeltaMovement(reflection.scale(0.125D));

            inGround = false;

            arrow.playSound(SoundEvents.AMETHYST_BLOCK_HIT, 0.4F, 0.6F / (level.random.nextFloat() * 0.2F + 0.9F));
            level.addParticle(ParticleTypes.CRIT, hitVector.x, hitVector.y, hitVector.z, 0, 0, 0);

            ci.cancel();
            return;
        }

        plantopia$lastRicochetPos = null;
    }
}
