package by.langvest.plantopia.mixin;

import by.langvest.plantopia.block.PlantopiaOffsetSeedAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockBehaviour.Properties.class)
public abstract class PlantopiaBlockBehaviour$PropertiesMixin {
	@Redirect(
		method = "lambda$offsetType$11(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/Vec3;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/Mth;getSeed(III)J"
		)
	)
	private static long offsetType$11$getSeed(int x, int y, int z, @NotNull BlockState state, BlockGetter level, BlockPos pos) {
		return plantopia$getSeed(x, y, z, state, level, pos);
	}

	@Redirect(
		method = "lambda$offsetType$12(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/Vec3;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/Mth;getSeed(III)J"
		)
	)
	private static long offsetType$12$getSeed(int x, int y, int z, @NotNull BlockState state, BlockGetter level, BlockPos pos) {
		return plantopia$getSeed(x, y, z, state, level, pos);
	}

	@Unique
	@SuppressWarnings("deprecation")
	private static long plantopia$getSeed(int x, int y, int z, @NotNull BlockState state, BlockGetter level, BlockPos pos) {
		Block block = state.getBlock();

		if(block instanceof PlantopiaOffsetSeedAccessor accessor) {
			return accessor.getOffsetSeed(state, pos);
		}

		return Mth.getSeed(x, y, z);
	}
}