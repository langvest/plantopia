package by.langvest.plantopia.block.special;

import by.langvest.plantopia.worldgen.feature.special.PlantopiaNaturalBlockFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaTallBushBlock extends BushBlock implements BonemealableBlock {
    protected static final VoxelShape SHAPE = Block.box(1.0F, 0.0F, 1.0F, 15.0F, 16.0F, 15.0F);
    protected Supplier<Block> shortVariant;

    public PlantopiaTallBushBlock(BlockBehaviour.Properties properties, Supplier<Block> shortVariant) {
        super(properties);
        this.shortVariant = shortVariant;
    }

    public Block getShortVariant() {
        return shortVariant.get();
    }

    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        var newState = getShortVariant().defaultBlockState();
        return Direction.Plane.HORIZONTAL.stream().anyMatch(direction -> {
            var candidatePos = pos.relative(direction);
            return level.isEmptyBlock(candidatePos) && newState.canSurvive(level, candidatePos);
        });
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        var directions = Direction.Plane.HORIZONTAL.shuffledCopy(random);
        var newState = getShortVariant().defaultBlockState();

        for (var direction : directions) {
            var candidatePos = pos.relative(direction);

            if (level.isEmptyBlock(candidatePos) && newState.canSurvive(level, candidatePos)) {
                if (PlantopiaNaturalBlockFeature.place(level, newState, candidatePos, random, Block.UPDATE_ALL)) {
                    return;
                }
            }
        }
    }
}
