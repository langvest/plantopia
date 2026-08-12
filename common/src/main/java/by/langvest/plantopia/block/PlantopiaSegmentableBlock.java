package by.langvest.plantopia.block;

import by.langvest.plantopia.util.helper.PlantopiaShapeHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

@ParametersAreNonnullByDefault
public interface PlantopiaSegmentableBlock {
    int MIN_SEGMENT = 1;
    int MAX_SEGMENT = 4;
    IntegerProperty AMOUNT = PlantopiaBlockStateProperties.SEGMENT_AMOUNT;

    default Function<BlockState, VoxelShape> getShapeCalculator(final EnumProperty<Direction> facing, final IntegerProperty amount) {
        VoxelShape baseShape = Block.box(0.0, 0.0, 0.0, 8.0, getShapeHeight(), 8.0);
        return state -> {
            var shape = Shapes.empty();
            var direction = state.getValue(facing);
            int count = state.getValue(amount);

            for (int i = 0; i < count; i++) {
                shape = Shapes.or(shape, PlantopiaShapeHelper.rotateShape(baseShape, direction));
                direction = direction.getCounterClockWise();
            }

            return shape.singleEncompassing();
        };
    }

    default IntegerProperty getSegmentAmountProperty() {
        return AMOUNT;
    }

    default double getShapeHeight() {
        return 1.0D;
    }

    default boolean canBeReplaced(final BlockState state, final BlockPlaceContext context, final IntegerProperty segment) {
        return !context.isSecondaryUseActive() && context.getItemInHand().is(state.getBlock().asItem()) && state.getValue(segment) < MAX_SEGMENT;
    }

    default BlockState getStateForPlacement(final BlockPlaceContext context, final Block block, final IntegerProperty segment, final EnumProperty<Direction> facing) {
        var state = context.getLevel().getBlockState(context.getClickedPos());
        return state.is(block)
            ? state.setValue(segment, Math.min(MAX_SEGMENT, state.getValue(segment) + 1))
            : block.defaultBlockState().setValue(facing, context.getHorizontalDirection().getOpposite());
    }
}
