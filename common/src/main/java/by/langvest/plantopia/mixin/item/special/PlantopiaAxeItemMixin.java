package by.langvest.plantopia.mixin.item.special;

import by.langvest.plantopia.block.PlantopiaStrippableBlock;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(AxeItem.class)
public abstract class PlantopiaAxeItemMixin {
    @Shadow
    public static Map<Block, Block> STRIPPABLES;

    @Inject(
        method = "useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void useOn(@NotNull UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        var level = context.getLevel();
        var pos = context.getClickedPos();

        var unstrippedState = level.getBlockState(pos);
        if (!(unstrippedState.getBlock() instanceof PlantopiaStrippableBlock strippableBlock)) return;

        var strippedBlock = STRIPPABLES.get(unstrippedState.getBlock());
        if (strippedBlock == null) return;

        var strippedState = strippableBlock.getStrippedState(context, unstrippedState, strippedBlock);
        if (strippedState == null) return;

        if (strippedState == unstrippedState) {
            cir.setReturnValue(InteractionResult.PASS);
            return;
        }

        strippableBlock.onStripped(context, strippedState);

        var player = context.getPlayer();
        var itemStack = context.getItemInHand();
        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, itemStack);
        }
        level.setBlock(pos, strippedState, 11);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, strippedState));
        if (player != null) {
            itemStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(context.getHand()));
        }
        cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
    }
}
