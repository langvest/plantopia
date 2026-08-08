package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaParticleTintableBlock;
import by.langvest.plantopia.block.PlantopiaPollinableBlock;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaHerbBlock extends DoublePlantBlock implements PlantopiaPollinableBlock, PlantopiaParticleTintableBlock {
    public PlantopiaHerbBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidPollinationTarget(Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER;
    }

    @Override
    public @Nullable Integer getDestroyParticlesColorOverride(BlockState state, ClientLevel level, BlockPos pos) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER ? -1 : null;
    }
}
