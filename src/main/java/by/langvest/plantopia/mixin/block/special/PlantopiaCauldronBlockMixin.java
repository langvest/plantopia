package by.langvest.plantopia.mixin.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaQuicksandCauldronBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CauldronBlock.class)
public abstract class PlantopiaCauldronBlockMixin {
	@Inject(
		method = "handlePrecipitation(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/biome/Biome$Precipitation;)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void handlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation, CallbackInfo ci) {
		if(PlantopiaQuicksandCauldronBlock.shouldHandlePrecipitation(level, pos, precipitation)) {
			level.setBlockAndUpdate(pos, PlantopiaBlocks.QUICKSAND_CAULDRON.get().defaultBlockState());
			level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
			ci.cancel();
		}
	}
}
