package by.langvest.plantopia.mixin;

import by.langvest.plantopia.block.special.PlantopiaQuicksandCauldronBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.chunk.LevelChunk;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerLevel.class)
public abstract class PlantopiaServerLevelMixin {
	@Inject(
		method = "tickChunk(Lnet/minecraft/world/level/chunk/LevelChunk;I)V",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/level/biome/Biome$Precipitation;NONE:Lnet/minecraft/world/level/biome/Biome$Precipitation;",
			opcode = Opcodes.GETSTATIC,
			ordinal = 0
		),
		locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void beforePrecipitationCheck(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci, ChunkPos chunkPos, boolean flag, int i, int j, ProfilerFiller profilerfiller, BlockPos pos, BlockPos posBelow, Biome biome, int i1, Biome.Precipitation precipitation) {
		ServerLevel level = (ServerLevel)(Object)this;

		if(precipitation == Biome.Precipitation.NONE) {
			var stateBelow = level.getBlockState(posBelow);
			var blockBelow = stateBelow.getBlock();

			if(blockBelow instanceof PlantopiaQuicksandCauldronBlock || blockBelow instanceof CauldronBlock) {
				blockBelow.handlePrecipitation(stateBelow, level, posBelow, precipitation);
			}
		}
	}
}
