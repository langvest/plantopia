package by.langvest.plantopia.mixin.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static net.minecraft.world.level.block.Block.dropResources;

@Mixin(SpongeBlock.class)
public abstract class PlantopiaSpongeBlockMixin {
	@Redirect(
		method = "removeWaterBreadthFirstSearch(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/BlockPos;breadthFirstTraversal(Lnet/minecraft/core/BlockPos;IILjava/util/function/BiConsumer;Ljava/util/function/Predicate;)I"
		)
	)
	private int removeWaterBreadthFirstSearch$breadthFirstTraversal(BlockPos startPos, int depth, int visitLimit, BiConsumer<BlockPos, Consumer<BlockPos>> action, Predicate<BlockPos> predicate, @NotNull Level level) {
		BlockState spongeState = level.getBlockState(startPos);

		return BlockPos.breadthFirstTraversal(startPos, depth, visitLimit, action, candidatePos -> {
			var successfully = predicate.test(candidatePos);

			if(!successfully) {
				var candidateState = level.getBlockState(candidatePos);
				var candidateFluidState = level.getFluidState(candidatePos);

				if(spongeState.canBeHydrated(level, startPos, candidateFluidState, candidatePos)) {
					if(candidateState.is(PlantopiaBlocks.SEAWEED.get())) {
						var blockEntity = candidateState.hasBlockEntity() ? level.getBlockEntity(candidatePos) : null;
						dropResources(candidateState, level, candidatePos, blockEntity);
						level.setBlock(candidatePos, Blocks.AIR.defaultBlockState(), 3);

						return true;
					}
				}
			}

			return successfully;
		});
	}
}
