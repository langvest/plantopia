package by.langvest.plantopia.handler.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.entity.ai.goal.PlantopiaClimbOnTopOfQuicksandGoal;
import by.langvest.plantopia.entity.ai.goal.PlantopiaZombieFindHogweedGoal;
import by.langvest.toolkit.event.game.EntitySpawnEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaEntityHandler {
    public static void onEntitySpawn(EntitySpawnEvent event) {
        var entity = event.getEntity();

        if (entity instanceof Mob mob) {
            if (mob instanceof Rabbit || mob instanceof Endermite || mob instanceof Silverfish) {
                mob.goalSelector.addGoal(1, new PlantopiaClimbOnTopOfQuicksandGoal(mob, mob.level()));
            }

            if (mob instanceof Zombie zombie) {
                zombie.goalSelector.addGoal(1, new PlantopiaZombieFindHogweedGoal(zombie, 1.0D));
            }

            if (mob instanceof EnderMan enderMan) {
                var infestedState = getInfestedBlockForEnderMan(enderMan);
                if (infestedState != null) {
                    enderMan.setCarriedBlock(infestedState);
                }
            }
        }
    }

    @Nullable
    @SuppressWarnings("resource")
    protected static BlockState getInfestedBlockForEnderMan(EnderMan enderMan) {
        var level = enderMan.level();
        if (level.dimension() != Level.OVERWORLD) return null;

        float chance = 0.012F;
        if (level.getDifficulty() == Difficulty.HARD) {
            chance *= 2;
        }

        var random = enderMan.getRandom();
        if (random.nextFloat() >= chance) return null;

        var posBelow = enderMan.blockPosition().below();
        var stateBelow = level.getBlockState(posBelow);
        if (!stateBelow.is(BlockTags.DIRT)) return null;

        return PlantopiaBlocks.INFESTED_GRASS_BLOCK.get().defaultBlockState();
    }
}
