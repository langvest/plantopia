package by.langvest.plantopia.mixin.entity.special;

import by.langvest.plantopia.block.PlantopiaPollinableBlock;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.tag.PlantopiaItemTags;
import by.langvest.plantopia.extension.PlantopiaContextBinderExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Bee.class)
public abstract class PlantopiaBeeMixin {
    @Shadow
    Bee.BeePollinateGoal beePollinateGoal;

    @Inject(
        method = "isFood(Lnet/minecraft/world/item/ItemStack;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void isFood(@NotNull ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack.is(PlantopiaItemTags.IGNORED_BY_BEES)) {
            cir.setReturnValue(false);
            return;
        }

        if (itemStack.is(PlantopiaItemTags.PREFERRED_BY_BEES)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
        method = "isFlowerValid(Lnet/minecraft/core/BlockPos;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void isFlowerValid(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Bee that = (Bee) (Object) this;

        var level = that.level();
        if (!level.isLoaded(pos)) return;

        var state = level.getBlockState(pos);

        if (state.is(PlantopiaBlockTags.IGNORED_BY_BEES)) {
            cir.setReturnValue(false);
            return;
        }

        if (state.is(Blocks.SUNFLOWER) && state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
            cir.setReturnValue(false);
            return;
        }

        if (state.getBlock() instanceof PlantopiaPollinableBlock pollinableBlock) {
            cir.setReturnValue(pollinableBlock.isValidPollinationTarget(level, state, pos, that));
            return;
        }

        if (state.is(PlantopiaBlockTags.PREFERRED_BY_BEES)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
        method = "registerGoals()V",
        at = @At("TAIL")
    )
    private void registerGoals(CallbackInfo ci) {
        Bee that = (Bee) (Object) this;

        if (beePollinateGoal instanceof PlantopiaContextBinderExtension binder) {
            binder.plantopia$bindContext(that);
        }
    }
}
