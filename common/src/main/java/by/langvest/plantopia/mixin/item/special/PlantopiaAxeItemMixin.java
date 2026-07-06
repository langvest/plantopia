package by.langvest.plantopia.mixin.item.special;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Optional;

@Mixin(AxeItem.class)
public abstract class PlantopiaAxeItemMixin {
    @Shadow
    public static Map<Block, Block> STRIPPABLES;

    @Inject(
        method = "getStripped(Lnet/minecraft/world/level/block/state/BlockState;)Ljava/util/Optional;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getStripped(@NotNull BlockState unstrippedState, CallbackInfoReturnable<Optional<BlockState>> cir) {
        Block strippedBlock = STRIPPABLES.get(unstrippedState.getBlock());

        if (strippedBlock != null && unstrippedState.hasProperty(BlockStateProperties.FACING)) {
            var newState = plantopia$getSpecialStrippedState(unstrippedState, strippedBlock);

            if (newState != null) {
                cir.setReturnValue(Optional.of(newState));
            }
        }
    }

    @Unique
    @Nullable
    private static BlockState plantopia$getSpecialStrippedState(@NotNull BlockState unstrippedState, Block strippedBlock) {
        if (unstrippedState.hasProperty(BlockStateProperties.FACING)) {
            var axis = unstrippedState.getValue(BlockStateProperties.FACING).getAxis();
            return strippedBlock.defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis);
        }

        return null;
    }
}
