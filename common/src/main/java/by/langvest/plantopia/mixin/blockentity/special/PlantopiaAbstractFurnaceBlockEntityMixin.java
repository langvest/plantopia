package by.langvest.plantopia.mixin.blockentity.special;

import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class PlantopiaAbstractFurnaceBlockEntityMixin {
    @Shadow
    private static void add(Map<Item, Integer> map, ItemLike item, int burnTime) {}

    @Inject(
        method = "getFuel()Ljava/util/Map;",
        at = @At("RETURN")
    )
    private static void getFuel(@NotNull CallbackInfoReturnable<Map<Item, Integer>> cir) {
        var map = cir.getReturnValue();

        PlantopiaMetaBuckets.ITEM.forEach(itemMeta -> {
            var burnTime = itemMeta.getBurnTime();

            if (burnTime > 0) {
                add(map, itemMeta.get(), burnTime);
            }
        });
    }
}
