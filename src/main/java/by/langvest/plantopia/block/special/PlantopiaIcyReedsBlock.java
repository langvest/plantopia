package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.blockentity.special.PlantopiaIcyReedsBlockEntity;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.scheduleWaterTick;

public class PlantopiaIcyReedsBlock extends IceBlock implements EntityBlock {
    public PlantopiaIcyReedsBlock(Properties properties) {
        super(properties);
    }

    public Block getPlantBlock() {
        return PlantopiaBlocks.REEDS.get();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        var stateAbove = level.getBlockState(pos.above());

        if(!stateAbove.is(getPlantBlock())) return false;
        if(stateAbove.getValue(PlantopiaReedsBlock.HALF) != DoubleBlockHalf.UPPER) return false;

        return getPlantBlock().defaultBlockState().canSurvive(level, pos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos facingPos) {
        if(facing.getAxis().isVertical() && !state.canSurvive(level, pos)) {
            return Blocks.ICE.defaultBlockState();
        }

        return state;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return getPlantBlock().asItem().getDefaultInstance();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PlantopiaIcyReedsBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        playerWillDestroy(level, pos, state, player);

        var newState = getDriedState();

        boolean hasSilkTouch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, player.getMainHandItem()) > 0;

        if(!hasSilkTouch && !player.getAbilities().instabuild && !level.dimensionType().ultraWarm()) {
            newState = getMeltedState();
        }

        scheduleWaterTick(newState, level, pos);
        return level.setBlock(pos, newState, level.isClientSide ? 11 : 3);
    }

    @Override
    public void playerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity blockEntity, @NotNull ItemStack tool) {
        var iceState = Blocks.ICE.defaultBlockState();

        player.awardStat(Stats.BLOCK_MINED.get(Blocks.ICE));
        player.causeFoodExhaustion(0.005F);
        dropResources(iceState, level, pos, blockEntity, player, tool, false);
    }

    public BlockState getDriedState() {
        return getPlantBlock().defaultBlockState()
            .setValue(PlantopiaReedsBlock.HALF, DoubleBlockHalf.LOWER);
    }

    public BlockState getMeltedState() {
        return getDriedState()
            .setValue(PlantopiaReedsBlock.WATERLOGGED, true);
    }

    @Override
    protected void melt(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        var newState = level.dimensionType().ultraWarm() ? getDriedState() : getMeltedState();

        level.setBlockAndUpdate(pos, newState);
        level.neighborChanged(pos, newState.getBlock(), pos);
    }
}
