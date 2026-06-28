package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.blockentity.special.PlantopiaFrozenReedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.scheduleWaterTick;

@ParametersAreNonnullByDefault
public class PlantopiaFrozenReedBlock extends IceBlock implements EntityBlock {
    public PlantopiaFrozenReedBlock(Properties properties) {
        super(properties);
    }

    public Block getPlantBlock() {
        return PlantopiaBlocks.REED.get();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        var stateAbove = level.getBlockState(pos.above());

        if (!stateAbove.is(getPlantBlock())) return false;
        if (stateAbove.getValue(PlantopiaReedBlock.HALF) != DoubleBlockHalf.UPPER) return false;

        return getPlantBlock().defaultBlockState().canSurvive(level, pos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
        if (facing.getAxis().isVertical() && !state.canSurvive(level, pos)) {
            return Blocks.ICE.defaultBlockState();
        }

        return state;
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return getPlantBlock().asItem().getDefaultInstance();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PlantopiaFrozenReedBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player);

        var newState = getDriedState();

        boolean hasSilkTouch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, player.getMainHandItem()) > 0;

        if (!hasSilkTouch && !player.getAbilities().instabuild && !level.dimensionType().ultraWarm()) {
            newState = getMeltedState();
        }

        scheduleWaterTick(newState, level, pos);
        level.setBlock(pos, newState, level.isClientSide ? 11 : 3);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        var iceState = Blocks.ICE.defaultBlockState();

        player.awardStat(Stats.BLOCK_MINED.get(Blocks.ICE));
        player.causeFoodExhaustion(0.005F);

        dropResources(iceState, level, pos, blockEntity, player, tool);
    }

    public BlockState getDriedState() {
        return getPlantBlock().defaultBlockState()
            .setValue(PlantopiaReedBlock.HALF, DoubleBlockHalf.LOWER);
    }

    public BlockState getMeltedState() {
        return getDriedState()
            .setValue(PlantopiaReedBlock.WATERLOGGED, true);
    }

    @Override
    protected void melt(BlockState state, Level level, BlockPos pos) {
        var newState = level.dimensionType().ultraWarm() ? getDriedState() : getMeltedState();

        level.setBlockAndUpdate(pos, newState);
        level.neighborChanged(pos, newState.getBlock(), pos);
    }
}
