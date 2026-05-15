package by.langvest.plantopia.worldgen.surface.special;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @see net.minecraft.data.worldgen.SurfaceRuleData
 */
public abstract class PlantopiaSurface {
    public abstract SurfaceRules.RuleSource makeRules();

    /* HELPER METHODS *************************************************************************************************/

    @Contract("_ -> new")
    protected static SurfaceRules.@NotNull RuleSource makeStateRule(@NotNull Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
