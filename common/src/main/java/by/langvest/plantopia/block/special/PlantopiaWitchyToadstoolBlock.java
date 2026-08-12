package by.langvest.plantopia.block.special;

import by.langvest.plantopia.particle.PlantopiaParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.pathfinder.PathComputationType;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaWitchyToadstoolBlock extends PlantopiaToadstoolBlock {
    public PlantopiaWitchyToadstoolBlock(Properties properties, ResourceKey<ConfiguredFeature<?, ?>> feature) {
        super(properties, feature);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);

        if (random.nextFloat() > 0.4F) return;

        float x = pos.getX() + 8.0F / 16.0F;
        float y = pos.getY() + 6.0F / 16.0F;
        float z = pos.getZ() + 8.0F / 16.0F;

        for (int i = 0; i < 3; i++) {
            if (random.nextInt(0, 4) == 0) {
                level.addParticle(
                    PlantopiaParticleTypes.WITCHY_TOADSTOOL_SPORE.get(),
                    x + Mth.nextDouble(random, -0.15F, 0.15F),
                    y + Mth.nextDouble(random, -0.22F, 0.22F),
                    z + Mth.nextDouble(random, -0.15F, 0.15F),
                    0.0,
                    0.0,
                    0.0
                );
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);

        if (level.isClientSide()) return;
        if (level.getDifficulty() == Difficulty.PEACEFUL) return;

        if (entity instanceof LivingEntity livingEntity && !(entity instanceof Monster)) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 50, 1, true, true));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 180, 1, true, false, true));
        }
    }
}
