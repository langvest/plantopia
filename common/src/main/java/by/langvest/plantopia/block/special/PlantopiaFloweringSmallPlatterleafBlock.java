package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaFloweringWaterlilyBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaFloweringSmallPlatterleafBlock extends PlantopiaSmallPlatterleafBlock implements PlantopiaFloweringWaterlilyBlock {
    protected final Supplier<Block> flowerBlock;

    public PlantopiaFloweringSmallPlatterleafBlock(Supplier<Block> flowerBlock, Properties properties) {
        super(properties);
        this.flowerBlock = flowerBlock;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return getFlowerBlock().asItem().getDefaultInstance();
    }

    @Override
    public Block getFlowerBlock() {
        return flowerBlock.get();
    }

    @Override
    public Block getOriginBlock() {
        return PlantopiaBlocks.SMALL_PLATTERLEAF.get();
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        return useShears(state, level, pos, player, hand, blockHitResult);
    }
}
