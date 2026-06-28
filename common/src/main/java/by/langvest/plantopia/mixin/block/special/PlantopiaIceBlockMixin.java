package by.langvest.plantopia.mixin.block.special;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(IceBlock.class)
public abstract class PlantopiaIceBlockMixin extends HalfTransparentBlock {
    public PlantopiaIceBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean skipRendering(@NotNull BlockState state, @NotNull BlockState adjacentState, @NotNull Direction side) {
        var adjacentBlock = adjacentState.getBlock();

        return adjacentBlock instanceof IceBlock || super.skipRendering(state, adjacentState, side);
    }
}
