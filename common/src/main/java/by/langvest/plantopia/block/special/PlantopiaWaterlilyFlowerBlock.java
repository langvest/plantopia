package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaOffsettableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaWaterlilyFlowerBlock extends FlowerBlock implements PlantopiaOffsettableBlock {
    protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 6.0D, 12.0D);

    public PlantopiaWaterlilyFlowerBlock(Supplier<MobEffect> effectSupplier, int effectDuration, Properties properties) {
        super(effectSupplier.get(), effectDuration, properties);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        var vec3 = state.getOffset(level, pos);
        return SHAPE.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.FARMLAND)
            || state.is(Blocks.BAMBOO)
            || state.is(Blocks.CACTUS)
            || state.is(Blocks.POTTED_BAMBOO)
            || state.is(Blocks.POTTED_CACTUS)
            || state.is(PlantopiaBlocks.POTTED_BRANCHING_SHRUB.get())
            || state.isFaceSturdy(level, pos, Direction.UP, SupportType.CENTER);
    }

    @Override
    public Optional<OffsetFunction> getOffsetFunction(Optional<OffsetFunction> defaultOffsetFunction) {
        OffsetFunction offsetFunction = (state, level, pos) -> {
            var posBelow = pos.below();
            var stateBelow = level.getBlockState(posBelow);

            if (stateBelow.is(Blocks.BAMBOO)) {
                return stateBelow.getOffset(level, posBelow);
            }

            if (stateBelow.is(Blocks.DECORATED_POT)) {
                return Vec3.ZERO.add(0, 0.245D, 0);
            }

            return Vec3.ZERO;
        };

        return Optional.of(offsetFunction);
    }
}
