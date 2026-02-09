package by.langvest.plantopia.mixin;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaFreezableBlock;
import by.langvest.plantopia.block.special.PlantopiaQuicksandCauldronBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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

	@Redirect(
		method = "tickChunk(Lnet/minecraft/world/level/chunk/LevelChunk;I)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/biome/Biome;shouldFreeze(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"
		)
	)
	private boolean tickChunk$shouldIce(@NotNull Biome biome, LevelReader levelReader, BlockPos pos) {
		ServerLevel level = (ServerLevel)(Object) this;

		var state = level.getBlockState(pos);
		var block = state.getBlock();

		if(block instanceof PlantopiaFreezableBlock freezableBlock) {
			if(!biome.shouldFreeze(levelReader, pos)) return false;

			var iceState = Blocks.ICE.defaultBlockState();

			freezableBlock.freezeAt(state, iceState, level, pos, 3);

			return false;
		}

		return biome.shouldFreeze(levelReader, pos);
	}

	@Redirect(
		method = "tickChunk(Lnet/minecraft/world/level/chunk/LevelChunk;I)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/biome/Biome;shouldSnow(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"
		)
	)
	private boolean tickChunk$shouldSnow(@NotNull Biome biome, LevelReader levelReader, BlockPos pos) {
		ServerLevel level = (ServerLevel)(Object) this;

		var state = level.getBlockState(pos);
		var block = state.getBlock();

		if(state.is(PlantopiaBlocks.COVERED_SNOWDROP.get())) {
			System.out.println(232);
		}

		if(block instanceof PlantopiaFreezableBlock || block instanceof SnowLayerBlock) {
			if(!biome.shouldSnow(levelReader, pos)) return false;

			int layers = block instanceof SnowLayerBlock ? state.getValue(SnowLayerBlock.LAYERS) : 0;
			int snowMaxHeight = level.getGameRules().getInt(GameRules.RULE_SNOW_ACCUMULATION_HEIGHT);

			if(layers >= Math.min(snowMaxHeight, SnowLayerBlock.MAX_HEIGHT)) return false;

			if(block instanceof PlantopiaFreezableBlock freezableBlock) {
				var snowState = Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, layers + 1);

				freezableBlock.freezeAt(state, snowState, level, pos, 3);
				Block.pushEntitiesUp(state, level.getBlockState(pos), level, pos);

				return false;
			}

			if(block instanceof SnowLayerBlock) {
				var newState = state.setValue(SnowLayerBlock.LAYERS, layers + 1);

				level.setBlockAndUpdate(pos, newState);
				Block.pushEntitiesUp(state, newState, level, pos);

				return false;
			}
		}

		return biome.shouldSnow(levelReader, pos);
	}
}
