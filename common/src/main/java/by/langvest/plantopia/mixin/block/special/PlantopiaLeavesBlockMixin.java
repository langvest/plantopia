package by.langvest.plantopia.mixin.block.special;

import by.langvest.plantopia.tag.PlantopiaBlockTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LeavesBlock.class)
public abstract class PlantopiaLeavesBlockMixin {
    @Redirect(
        method = "getOptionalDistanceAt(Lnet/minecraft/world/level/block/state/BlockState;)Ljava/util/OptionalInt;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"
        )
    )
    private static boolean getOptionalDistanceAt$is(@NotNull BlockState state, @NotNull TagKey<Block> tagKey) {
        if (tagKey.location().equals(BlockTags.LOGS.location())) {
            return state.is(PlantopiaBlockTags.LEAVES_CAN_SURVIVE_ON);
        }

        return state.is(tagKey);
    }
}
