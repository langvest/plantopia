package by.langvest.plantopia.worldgen.surface.special;

import by.langvest.plantopia.worldgen.biome.PlantopiaOverworldBiomes;
import by.langvest.plantopia.worldgen.noise.PlantopiaNoises;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

/**
 * @see net.minecraft.data.worldgen.SurfaceRuleData
 */
public class PlantopiaOverworldSurface extends PlantopiaSurface {
    private static final SurfaceRules.ConditionSource IS_ABOVE_62 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(62), 0);
    private static final SurfaceRules.ConditionSource IS_ABOVE_63 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(63), 0);

    @Override
    public SurfaceRules.RuleSource makeRules() {
        var water = makeStateRule(Blocks.WATER);

        return SurfaceRules.sequence(
            SurfaceRules.ifTrue(
                SurfaceRules.ON_FLOOR,
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(
                        // Weighted water noise
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
}
