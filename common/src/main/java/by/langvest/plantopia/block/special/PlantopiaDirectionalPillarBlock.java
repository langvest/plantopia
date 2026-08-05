package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaStrippableBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaDirectionalPillarBlock extends DirectionalBlock implements PlantopiaStrippableBlock {
    public PlantopiaDirectionalPillarBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var facing = context.getClickedFace();
        return defaultBlockState().setValue(FACING, facing);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStrippedState(UseOnContext context, BlockState unstrippedState, Block strippedBlock) {
        var facing = unstrippedState.getValue(FACING);
        var newState = strippedBlock.defaultBlockState();

        if (newState.hasProperty(BlockStateProperties.FACING)) {
            return newState.setValue(BlockStateProperties.FACING, facing);
        }

        if (newState.hasProperty(BlockStateProperties.AXIS)) {
            return newState.setValue(BlockStateProperties.AXIS, facing.getAxis());
        }

        return null;
    }
}
