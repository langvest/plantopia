package by.langvest.plantopia.mixin.network;

import by.langvest.plantopia.extension.PlantopiaRandomizedHitResultExtension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FriendlyByteBuf.class)
public abstract class PlantopiaFriendlyByteBufMixin {
	@Inject(
		method = "readBlockHitResult()Lnet/minecraft/world/phys/BlockHitResult;",
		at = @At("RETURN")
	)
	private void readBlockHitResult(@NotNull CallbackInfoReturnable<BlockHitResult> cir) {
		FriendlyByteBuf friendlyByteBuf = (FriendlyByteBuf)(Object)this;
		var blockHitResult = cir.getReturnValue();

		if(blockHitResult instanceof PlantopiaRandomizedHitResultExtension randomizedHitResult) {
			randomizedHitResult.plantopia$setBaseSeed(friendlyByteBuf.readByte());
		}
	}

	@Inject(
		method = "writeBlockHitResult(Lnet/minecraft/world/phys/BlockHitResult;)V",
		at = @At("RETURN")
	)
	private void writeBlockHitResult(BlockHitResult blockHitResult, CallbackInfo ci) {
		FriendlyByteBuf friendlyByteBuf = (FriendlyByteBuf)(Object)this;

		if(blockHitResult instanceof PlantopiaRandomizedHitResultExtension randomizedHitResult) {
			friendlyByteBuf.writeByte(randomizedHitResult.plantopia$getBaseSeed());
		}
	}
}
