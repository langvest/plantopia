package by.langvest.plantopia.mixin.block.special;

import by.langvest.plantopia.block.PlantopiaPottableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FlowerPotBlock.class)
public abstract class PlantopiaFlowerPotBlockMixin {
    @Redirect(
        method = "use(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;",
            ordinal = 0
        )
    )
    private @NotNull BlockState use$defaultBlockState(Block pottedBlock, BlockState flowerPotState, Level level, BlockPos pos, @NotNull Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        var itemInHand = player.getItemInHand(hand);

        if (itemInHand.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof PlantopiaPottableBlock pottableBlock) {
            return pottableBlock.updatePottedState(pottedBlock.defaultBlockState(), flowerPotState, level, pos, player, hand, blockHitResult);
        }

        return pottedBlock.defaultBlockState();
    }
}
