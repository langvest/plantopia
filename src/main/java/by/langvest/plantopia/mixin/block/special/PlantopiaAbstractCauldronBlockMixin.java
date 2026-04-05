package by.langvest.plantopia.mixin.block.special;

import by.langvest.plantopia.block.special.PlantopiaIcicleBlock;
import by.langvest.plantopia.extension.PlantopiaAbstractCauldronBlockExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractCauldronBlock.class)
public abstract class PlantopiaAbstractCauldronBlockMixin implements PlantopiaAbstractCauldronBlockExtension {
	@Shadow
	protected abstract boolean canReceiveStalactiteDrip(Fluid fluid);

	@Override
	public boolean plantopia$canReceiveStalactiteDrip(Fluid fluid) {
		return canReceiveStalactiteDrip(fluid);
	}

	/**
	 * Перехватывает вызов PointedDripstoneBlock.findStalactiteTipAboveCauldron.
	 * Сначала пытается найти ванильный капельник. Если не находит, ищет нашу сосульку.
	 */
	@Redirect(
		method = "tick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/PointedDripstoneBlock;findStalactiteTipAboveCauldron(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/BlockPos;"
		)
	)
	private BlockPos tick$findStalactiteTipAboveCauldron(Level level, BlockPos pos) {
		var dripstonePos = PointedDripstoneBlock.findStalactiteTipAboveCauldron(level, pos);
		if (dripstonePos != null) {
			return dripstonePos;
		}

		return PlantopiaIcicleBlock.findStalactiteTipAboveCauldron(level, pos);
	}

	/**
	 * Перехватывает вызов PointedDripstoneBlock.getCauldronFillFluidType.
	 * Логика аналогична: сначала ванилла, потом мы.
	 */
	@Redirect(
		method = "tick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/PointedDripstoneBlock;getCauldronFillFluidType(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/Fluid;"
		)
	)
	private Fluid tick$getCauldronFillFluidType(ServerLevel level, BlockPos pos) {
		var dripstoneFluid = PointedDripstoneBlock.getCauldronFillFluidType(level, pos);
		if (dripstoneFluid != Fluids.EMPTY) {
			return dripstoneFluid;
		}

		return PlantopiaIcicleBlock.getCauldronFillFluidType(level, pos);
	}
}
