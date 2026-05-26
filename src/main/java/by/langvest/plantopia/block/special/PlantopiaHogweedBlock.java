package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaHogweedUtils;
import by.langvest.plantopia.block.PlantopiaNaturalBlock;
import by.langvest.plantopia.block.PlantopiaTripleBlockHalf;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.block.PlantopiaHogweedUtils.*;

public class PlantopiaHogweedBlock extends PlantopiaWideTriplePlantBlock {
    public PlantopiaHogweedBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull PathComputationType type) {
        return true;
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return true;
    }

    @Override
    protected boolean canGrowInto(@NotNull LevelAccessor level, BlockPos pos) {
        var fluidState = level.getFluidState(pos);

        return fluidState.isEmpty() && super.canGrowInto(level, pos);
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles).
     */
    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        var basePos = getBaseBlockPos(state, pos);

        if (pos != basePos) return;
        if (random.nextInt(16) != 0) return;

        var offset = state.getOffset(level, pos);
        double x = basePos.getX() + 1.0D + offset.x;
        double y = basePos.getY() + 1.5D + offset.y;
        double z = basePos.getZ() + 0.0D + offset.z;

        level.addParticle(
            ParticleTypes.SPORE_BLOSSOM_AIR,
            x + Mth.nextDouble(random, -0.25D, 0.25D),
            y + Mth.nextDouble(random, -1.0D, 1.0D),
            z + Mth.nextDouble(random, -0.25D, 0.25D),
            0.0D,
            0.0D,
            0.0D
        );
    }

    @Override
    public boolean placeNaturally(PlantopiaNaturalBlock.@NotNull PlaceContext context) {
        var pos = context.origin();

        if (!context.canSpreadInto(pos.above(2))) return false;
        if (!context.canSpreadInto(pos.north().above(2))) return false;
        if (!context.canSpreadInto(pos.north().east().above(2))) return false;
        if (!context.canSpreadInto(pos.east().above(2))) return false;

        return super.placeNaturally(context);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (random.nextInt(3) == 0) {
            var posBelow = pos.below();
            var stateBelow = level.getBlockState(posBelow);

            if (stateBelow.is(Blocks.GRASS_BLOCK)) {
                level.setBlockAndUpdate(posBelow, getGrassBlock().defaultBlockState());
            } else if (stateBelow.is(PlantopiaBlockTags.INFESTED_DIRT_CAN_SPREAD_TO)) {
                level.setBlockAndUpdate(posBelow, PlantopiaHogweedUtils.getDirtBlock().defaultBlockState());
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return state.getValue(HALF) == PlantopiaTripleBlockHalf.LOWER;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (level.isClientSide()) return;
        if (level.getDifficulty() == Difficulty.PEACEFUL) return;
        if (!(entity instanceof LivingEntity livingEntity)) return;
        if (livingEntity.isInvulnerable()) return;
        if (livingEntity instanceof Player player && player.isCreative()) return;

        if (livingEntity instanceof Zombie || livingEntity instanceof ZombieHorse) {
            var random = livingEntity.getRandom();

            if (random.nextInt(3) == 0 && livingEntity.getHealth() < livingEntity.getMaxHealth()) {
                livingEntity.heal(0.02F);
            }
        } else {
            if (livingEntity instanceof Enemy) return;

            livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 500));
        }
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, @NotNull Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluidState) {
        if (!level.isClientSide() && !player.isCreative()) {
            var basePos = getBaseBlockPos(state, pos).below();

            resetInfestedBlock(level, basePos);
            resetInfestedBlock(level, basePos.north());
            resetInfestedBlock(level, basePos.north().east());
            resetInfestedBlock(level, basePos.east());
        }

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluidState);
    }
}