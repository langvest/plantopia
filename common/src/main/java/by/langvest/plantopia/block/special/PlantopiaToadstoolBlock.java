package by.langvest.plantopia.block.special;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaToadstoolBlock extends MushroomBlock {
    protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 8.0D, 11.0D);

    public PlantopiaToadstoolBlock(Properties properties, ResourceKey<ConfiguredFeature<?, ?>> feature) {
        super(properties, feature);
    }

    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(25) != 0) {
            return;
        }

        if (!isValidBrightnessToSpread(level.getRawBrightness(pos, 0))) {
            return;
        }

        var mushroomLimit = 5;
        for (var nearbyPos : BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4))) {
            if (level.getBlockState(nearbyPos).is(this)) {
                mushroomLimit--;
                if (mushroomLimit <= 0) {
                    return;
                }
            }
        }

        var candidatePos = new BlockPos.MutableBlockPos();
        for (int i = 0; i < 4; i++) {
            candidatePos.setWithOffset(pos, random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);

            if (level.isEmptyBlock(candidatePos) && state.canSurvive(level, candidatePos) && isValidBrightnessToSpread(level.getRawBrightness(candidatePos, 0))) {
                level.setBlock(candidatePos, state, Block.UPDATE_CLIENTS);
                return;
            }
        }
    }

    protected boolean isValidBrightnessToSpread(int brightness) {
        return brightness < 13;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.isFaceSturdy(level, pos, Direction.UP);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        var posBelow = pos.below();
        var stateBelow = level.getBlockState(posBelow);

        if (stateBelow.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
            return true;
        }

        return mayPlaceOn(stateBelow, level, posBelow);
    }
}
