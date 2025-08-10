package by.langvest.plantopia.mixin;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaCoveredSnowdropBlock;
import by.langvest.plantopia.block.special.PlantopiaQuicksandCauldronBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
			target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
		)
	)
	private boolean tickChunk$is(BlockState state, @NotNull Block block) {
		if(block instanceof SnowLayerBlock) {
			return state.is(block) || state.is(PlantopiaBlocks.COVERED_SNOWDROP.get());
		}

		return state.is(block);
	}

	@Redirect(
		method = "tickChunk(Lnet/minecraft/world/level/chunk/LevelChunk;I)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/biome/Biome;shouldSnow(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"
		)
	)
	private boolean tickChunk$shouldSnow(@NotNull Biome biome, LevelReader levelReader, BlockPos pos) {
		ServerLevel level = (ServerLevel)(Object)this;

		boolean shouldSnow = biome.shouldSnow(levelReader, pos);

		if(!shouldSnow && plantopia$shouldCoverSnowdrop(biome, level, pos)) {
			var state = level.getBlockState(pos);

			if(state.is(PlantopiaBlocks.COVERED_SNOWDROP.get())) {
				int snowMaxHeight = level.getGameRules().getInt(GameRules.RULE_SNOW_ACCUMULATION_HEIGHT);
				int layers = state.getValue(PlantopiaCoveredSnowdropBlock.LAYERS);

				if(layers < Math.min(snowMaxHeight, PlantopiaCoveredSnowdropBlock.MAX_HEIGHT)) {
					var newState = state.setValue(PlantopiaCoveredSnowdropBlock.LAYERS, layers + 1);
					Block.pushEntitiesUp(state, newState, level, pos);
					level.setBlockAndUpdate(pos, newState);
				}
			} else {
				level.setBlockAndUpdate(pos, PlantopiaBlocks.COVERED_SNOWDROP.get().defaultBlockState());
			}

			return false;
		}

		return shouldSnow;
	}

	@Unique
	private boolean plantopia$shouldCoverSnowdrop(@NotNull Biome biome, @NotNull ServerLevel level, BlockPos pos) {
		if(biome.warmEnoughToRain(pos)) return false;
		if(pos.getY() < level.getMinBuildHeight()) return false;
		if(pos.getY() >= level.getMaxBuildHeight()) return false;
		if(level.getBrightness(LightLayer.BLOCK, pos) >= 10) return false;

		var state = level.getBlockState(pos);

		return state.is(PlantopiaBlocks.SNOWDROP.get()) || state.is(PlantopiaBlocks.COVERED_SNOWDROP.get());
	}
}
