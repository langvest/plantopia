package by.langvest.plantopia.block.special;

import by.langvest.plantopia.adv.trigger.PlantopiaAdvancementTriggers;
import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import by.langvest.plantopia.block.PlantopiaPollinableBlock;
import by.langvest.plantopia.block.PlantopiaPottableBlock;
import by.langvest.plantopia.util.helper.PlantopiaItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class PlantopiaLuckyDaisyBlock extends FlowerBlock implements BonemealableBlock, PlantopiaPollinableBlock, PlantopiaPottableBlock {
    protected static final VoxelShape FULL_SHAPE = Block.box(3.5D, 0.0D, 3.5D, 12.5D, 7.5D, 12.5D);
    protected static final VoxelShape EMPTY_SHAPE = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 7.5D, 10.0D);
    public static final IntegerProperty AMOUNT = PlantopiaBlockStateProperties.PETAL_AMOUNT;
    public static final int MIN_PETALS = 0;
    public static final int MAX_PETALS = 8;

    public PlantopiaLuckyDaisyBlock(Supplier<MobEffect> effectSupplier, int effectDuration, Properties properties) {
        super(effectSupplier, effectDuration, properties);
        registerDefaultState(stateDefinition.any().setValue(AMOUNT, MAX_PETALS));
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        var newItemStack = asItem().getDefaultInstance();
        PlantopiaItemHelper.setBlockStateData(newItemStack, state, AMOUNT);
        return newItemStack;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Vec3 vec3 = state.getOffset(level, pos);
        var shape = state.getValue(AMOUNT) == MIN_PETALS ? EMPTY_SHAPE : FULL_SHAPE;
        return shape.move(vec3.x, 0, vec3.z);
    }

    @Override
    public float getMaxVerticalOffset() {
        return 0.01F;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(AMOUNT);
    }

    @Override
    public boolean isValidPollinationTarget(Level level, @NotNull BlockState state, BlockPos pos, LivingEntity entity) {
        return true;
    }

    @Override
    public void onPollinationStop(Level level, BlockState state, BlockPos pos, LivingEntity entity, boolean successfully) {
        if (successfully) {
            int amount = state.getValue(AMOUNT);

            if (amount < MAX_PETALS) {
                level.setBlockAndUpdate(pos, state.setValue(AMOUNT, Math.min(MAX_PETALS, amount + 1)));
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return state.getValue(AMOUNT) != MAX_PETALS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult blockHitResult) {
        int amount = state.getValue(AMOUNT);
        var itemInHand = player.getItemInHand(hand);
        boolean isCorrectItem = !itemInHand.is(Items.BONE_MEAL) && itemInHand.isEmpty();

        if (player.getAbilities().mayBuild && isCorrectItem && amount > MIN_PETALS) {
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);

            if (!level.isClientSide()) {
                if (player.hasEffect(MobEffects.LUCK) || level.random.nextBoolean()) {
                    var newState = state.setValue(AMOUNT, Math.max(MIN_PETALS, amount - 1));
                    level.setBlock(pos, newState, 2);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));
                }

                if (player instanceof ServerPlayer serverPlayer) {
                    PlantopiaAdvancementTriggers.BLOCK_INTERACT.trigger(serverPlayer, pos);
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.use(state, level, pos, player, hand, blockHitResult);
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean isClient) {
        return state.getValue(AMOUNT) != MAX_PETALS;
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        level.setBlock(pos, defaultBlockState(), 2);
    }

    @Override
    public BlockState updatePottedState(BlockState newPottedState, BlockState oldPottedState, Level level, BlockPos pos, @NotNull Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        var itemInHand = player.getItemInHand(hand);
        var blockStateTag = PlantopiaItemHelper.getBlockStateData(itemInHand);

        if (blockStateTag != null) {
            var valueName = blockStateTag.getString(AMOUNT.getName());
            boolean canBePotted = valueName.isEmpty() || valueName.equals(String.valueOf(MAX_PETALS));

            if (!canBePotted) {
                return Blocks.AIR.defaultBlockState();
            }
        }

        return newPottedState;
    }
}
