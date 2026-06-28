package by.langvest.plantopia.mixin.item.special;

import by.langvest.plantopia.item.PlantopiaUpdateUseOnContext;
import by.langvest.plantopia.extension.PlantopiaRandomizedHitResultExtension;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BlockItem.class)
public abstract class PlantopiaBlockItemMixin {
    @ModifyVariable(
        method = "useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;",
        at = @At("HEAD"),
        ordinal = 0,
        argsOnly = true
    )
    private UseOnContext useOn(UseOnContext context) {
        BlockItem blockItem = (BlockItem) (Object) this;

        if (blockItem instanceof PlantopiaUpdateUseOnContext expandedBlockItem) {
            var random = context.getLevel().getRandom();

            if (context.getHitResult() instanceof PlantopiaRandomizedHitResultExtension randomizedHitResult) {
                random = randomizedHitResult.plantopia$getRandom();
            }

            return expandedBlockItem.updateUseOnContext(context, random);
        }

        return context;
    }
}
