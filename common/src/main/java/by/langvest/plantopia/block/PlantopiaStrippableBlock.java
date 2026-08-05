package by.langvest.plantopia.block;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface PlantopiaStrippableBlock {
    @Nullable BlockState getStrippedState(UseOnContext context, BlockState unstrippedState, Block strippedBlock);

    default SoundEvent getStrippedSound(UseOnContext context, BlockState strippedState) {
        return SoundEvents.AXE_STRIP;
    }

    default void playStrippedSound(UseOnContext context, BlockState strippedState) {
        context.getLevel().playSound(
            context.getPlayer(),
            context.getClickedPos(),
            getStrippedSound(context, strippedState),
            SoundSource.BLOCKS,
            1.0f,
            1.0f
        );
    }

    default void onStripped(UseOnContext context, BlockState strippedState) {
        playStrippedSound(context, strippedState);
    }
}
