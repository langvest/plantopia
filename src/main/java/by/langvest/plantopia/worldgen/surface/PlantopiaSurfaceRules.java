package by.langvest.plantopia.worldgen.surface;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.worldgen.biome.catalog.PlantopiaOverworldBiomes;
import by.langvest.plantopia.worldgen.noise.PlantopiaNoises;
import by.langvest.toolkit.event.LifecycleEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import terrablender.api.SurfaceRuleManager;

/**
 * @see net.minecraft.data.worldgen.SurfaceRuleData
 */
public class PlantopiaSurfaceRules {
    private static final SurfaceRules.ConditionSource IS_ABOVE_62 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(62), 0);
    private static final SurfaceRules.ConditionSource IS_ABOVE_63 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(63), 0);

    public static SurfaceRules.@NotNull RuleSource overworld() {
        var water = makeStateRule(Blocks.WATER);

        return SurfaceRules.sequence(
            SurfaceRules.ifTrue(
                SurfaceRules.ON_FLOOR,
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(
                        // Marsh water noise
                        SurfaceRules.isBiome(
                            PlantopiaOverworldBiomes.MARSH,
                            PlantopiaOverworldBiomes.DEAD_MARSH
                        ),
                        SurfaceRules.ifTrue(
                            IS_ABOVE_62,
                            SurfaceRules.ifTrue(
                                SurfaceRules.not(IS_ABOVE_63),
                                SurfaceRules.ifTrue(
                                    SurfaceRules.noiseCondition(PlantopiaNoises.MARSH, 0.0D),
                                    water
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    public static void setup(LifecycleEvent.CommonSetupEvent event) {
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, Plantopia.MOD_ID, overworld());
    }

    @Contract("_ -> new")
    protected static SurfaceRules.@NotNull RuleSource makeStateRule(@NotNull Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
