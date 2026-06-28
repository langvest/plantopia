package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaPollinableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaFoxgloveBlock extends TallFlowerBlock implements PlantopiaPollinableBlock {
    public PlantopiaFoxgloveBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidPollinationTarget(Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER;
    }
}
