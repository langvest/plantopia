package by.langvest.plantopia.block.special;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaCloverBlossomBlock extends FlowerBlock {
    protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 13.0D, 12.0D);

    public PlantopiaCloverBlossomBlock(Supplier<MobEffect> effectSupplier, int effectDuration, Properties properties) {
        super(effectSupplier.get(), effectDuration, properties);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        var vec3 = state.getOffset(level, pos);
        return SHAPE.move(vec3.x, 0, vec3.z);
    }

    @Override
    public float getMaxVerticalOffset() {
        return 0.01F;
    }
}
