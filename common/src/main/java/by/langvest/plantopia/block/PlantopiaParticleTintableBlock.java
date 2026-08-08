package by.langvest.plantopia.block;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface PlantopiaParticleTintableBlock {
    @Nullable Integer getDestroyParticlesColorOverride(BlockState state, ClientLevel level, BlockPos pos);
}
