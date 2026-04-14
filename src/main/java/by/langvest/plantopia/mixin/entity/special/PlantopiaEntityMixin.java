package by.langvest.plantopia.mixin.entity.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaQuicksandBlock;
import by.langvest.plantopia.extension.PlantopiaEntityQuicksandExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.extensions.IForgeEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class PlantopiaEntityMixin implements PlantopiaEntityQuicksandExtension, IForgeEntity {
	@Unique
	private boolean plantopia$isInQuicksand;

	@Unique
	private boolean plantopia$wasInQuicksand;

	@Shadow
	private Vec3 position;

	@Shadow
	private Level level;

	@Shadow
	public abstract AABB getBoundingBox();

	@Override
	public boolean plantopia$isInQuicksand() {
		return plantopia$isInQuicksand;
	}

	@Override
	public boolean plantopia$wasInQuicksand() {
		return plantopia$wasInQuicksand;
	}

	@Override
	public void plantopia$setIsInQuicksand(boolean isInQuicksand) {
		plantopia$isInQuicksand = isInQuicksand;
	}

	@Inject(
		method = "getBlockPosBelowThatAffectsMyMovement",
		at = @At("HEAD"),
		cancellable = true
	)
	private void getBlockPosBelowThatAffectsMyMovement(CallbackInfoReturnable<BlockPos> cir) {
		var posAbove = BlockPos.containing(position.x, getBoundingBox().minY + 0.15, position.z);
		var stateAbove = level.getBlockState(posAbove);

		if (stateAbove.is(PlantopiaBlocks.ICE_CRUST.get())) {
			cir.setReturnValue(posAbove);
		}
	}

	@Inject(
		method = "baseTick()V",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/entity/Entity;isInPowderSnow:Z",
			ordinal = 0,
			opcode = Opcodes.GETFIELD
		)
	)
	private void baseTick$updateInQuicksand(CallbackInfo ci) {
		plantopia$wasInQuicksand = plantopia$isInQuicksand;
		plantopia$isInQuicksand = false;
	}

	@Contract(pure = true)
	@Redirect(
		method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/entity/Entity;isInPowderSnow:Z",
			opcode = Opcodes.GETFIELD
		)
	)
	private boolean isInPowderSnow(@NotNull Entity entity) {
		return entity.isInPowderSnow || plantopia$isInQuicksand;
	}

	@Inject(
		method = "isStateClimbable(Lnet/minecraft/world/level/block/state/BlockState;)Z",
		at = @At("RETURN"),
		cancellable = true
	)
	private void isStateClimbable(BlockState state, @NotNull CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(cir.getReturnValue() || state.getBlock() instanceof PlantopiaQuicksandBlock);
	}
}
