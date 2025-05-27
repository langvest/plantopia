package by.langvest.plantopia.mixin;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaPollinableBlock;
import by.langvest.plantopia.block.special.PlantopiaPollinatedDandelionBlock;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.util.PlantopiaContextBinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(Bee.BeePollinateGoal.class)
public abstract class PlantopiaBee$BeePollinateGoalMixin implements PlantopiaContextBinder {
	@Nullable
	@Unique
	private Bee plantopia$bee = null;

	@Nullable
	@Unique
	private BlockPos plantopia$pollinationTargetPos = null;

	@Nullable
	@Unique
	private BlockPos plantopia$nextPollinationTargetPos = null;

	@Override
	public void plantopia$bindContext(Object context) {
		plantopia$bee = (Bee)context;
	}

	@Inject(
		method = "stop()V",
		at = @At("HEAD")
	)
	private void setPollinationTargetPos(CallbackInfo ci) {
		assert plantopia$bee != null;
		plantopia$pollinationTargetPos = plantopia$bee.savedFlowerPos;
	}

	@Inject(
		method = "stop()V",
		at = @At("TAIL")
	)
	private void stop(CallbackInfo ci) {
		if(plantopia$pollinationTargetPos == null) return;
		assert plantopia$bee != null;
		boolean successfully = plantopia$bee.hasNectar();
		Level level = plantopia$bee.level();
		BlockState state = level.getBlockState(plantopia$pollinationTargetPos);
		BlockPos pos = plantopia$pollinationTargetPos;
		if(successfully && state.is(Blocks.DANDELION)) {
			PlantopiaPollinatedDandelionBlock.placeAt(level, pos, PlantopiaBlocks.POLLINATED_DANDELION.get().defaultBlockState(), 27);
		}
		if(!(state.getBlock() instanceof PlantopiaPollinableBlock pollinableBlock)) return;
		pollinableBlock.onPollinationStop(level, state, pos, plantopia$bee, successfully);
	}

	@Inject(
		method = "findNearestBlock(Ljava/util/function/Predicate;D)Ljava/util/Optional;",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z"
		),
		locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void setNextPollinationTargetPos(Predicate<BlockState> predicate, double distance, CallbackInfoReturnable<Optional<BlockPos>> cir, BlockPos beePos, BlockPos.MutableBlockPos nextPollinationTargetPos) {
		plantopia$nextPollinationTargetPos = nextPollinationTargetPos;
	}

	@Redirect(
		method = "findNearestBlock(Ljava/util/function/Predicate;D)Ljava/util/Optional;",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z"
		)
	)
	private boolean findNearestBlock(Predicate<BlockState> predicate, Object object) {
		if(plantopia$nextPollinationTargetPos == null) return false;
		assert plantopia$bee != null;
		Level level = plantopia$bee.level();
		BlockState state = level.getBlockState(plantopia$nextPollinationTargetPos);
		if(state.is(PlantopiaBlockTags.IGNORED_BY_BEES)) return false;
		if(state.is(Blocks.SUNFLOWER) && state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) return false;
		if(state.getBlock() instanceof PlantopiaPollinableBlock pollinableBlock) return pollinableBlock.isValidPollinationTarget(level, state, plantopia$nextPollinationTargetPos, plantopia$bee);
		if(state.is(PlantopiaBlockTags.PREFERRED_BY_BEES)) return true;
		return predicate.test(state);
	}
}