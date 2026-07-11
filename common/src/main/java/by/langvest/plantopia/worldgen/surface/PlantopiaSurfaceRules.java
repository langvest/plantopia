package by.langvest.plantopia.worldgen.surface;

import by.langvest.plantopia.event.PlantopiaTerraBlenderEvent;
import by.langvest.plantopia.worldgen.biome.catalog.PlantopiaBiomes;
import by.langvest.plantopia.worldgen.noise.PlantopiaNoises;
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

    @Contract(value = "_ -> new", pure = true)
    private static SurfaceRules.@NotNull ConditionSource gravelNoiseAbove(double value) {
        return SurfaceRules.noiseCondition(PlantopiaNoises.GRAVEL, value / 8.25F, Double.MAX_VALUE);
    }

    private static SurfaceRules.@NotNull RuleSource placeDeep(SurfaceRules.RuleSource topMaterial, SurfaceRules.RuleSource bottomMaterial) {
        return SurfaceRules.sequence(
            SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, topMaterial),
            SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, topMaterial),
            SurfaceRules.ifTrue(SurfaceRules.DEEP_UNDER_FLOOR, bottomMaterial)
        );
    }

    private static SurfaceRules.@NotNull RuleSource placeDeep(SurfaceRules.RuleSource material) {
        return placeDeep(material, material);
    }

    public static SurfaceRules.@NotNull RuleSource overworld() {
        var water = makeStateRule(Blocks.WATER);
        var podzol = makeStateRule(Blocks.PODZOL);
        var rottedDirt = makeStateRule(Blocks.ROOTED_DIRT);
        var gravel = makeStateRule(Blocks.GRAVEL);
        var stone = makeStateRule(Blocks.STONE);
        var sand = makeStateRule(Blocks.SAND);
        var sandstone = makeStateRule(Blocks.SANDSTONE);

        SurfaceRules.RuleSource sandAndSandstone = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, sandstone), sand);
        SurfaceRules.RuleSource gravelAndStone = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, stone), gravel);

        return SurfaceRules.sequence(
            SurfaceRules.ifTrue(
                SurfaceRules.isBiome(PlantopiaBiomes.MAPLE_WOODS),
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(PlantopiaNoises.GRAVEL, 0.34D, 0.46D),
                        placeDeep(gravelAndStone, stone)
                    ),
                    SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(PlantopiaNoises.GRAVEL, 0.26D, 0.34D),
                        placeDeep(stone)
                    )
                )
            ),
            SurfaceRules.ifTrue(
                SurfaceRules.isBiome(PlantopiaBiomes.GRAVELLY_RIVER),
                placeDeep(gravelAndStone, stone)
            ),
            SurfaceRules.ifTrue(
                SurfaceRules.isBiome(PlantopiaBiomes.SANDY_RIVER),
                placeDeep(sandAndSandstone, sandstone)
            ),
            SurfaceRules.ifTrue(
                SurfaceRules.ON_FLOOR,
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(
                        // Marsh water noise
                        SurfaceRules.isBiome(PlantopiaBiomes.MARSH, PlantopiaBiomes.DEAD_MARSH),
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
                    ),
                    SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(PlantopiaBiomes.BOREAL_WOODS),
                        SurfaceRules.ifTrue(
                            SurfaceRules.noiseCondition(PlantopiaNoises.PODZOL, 0.2D),
                            podzol
                        )
                    ),
                    SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(PlantopiaBiomes.ASPEN_GROVE),
                        SurfaceRules.ifTrue(
                            SurfaceRules.noiseCondition(PlantopiaNoises.ROTTED_DIRT, 0.2D),
                            rottedDirt
                        )
                    )
                )
            )
        );
    }

//    public static SurfaceRules.RuleSource overworldLike(boolean aboveGround, boolean bedrockRoof, boolean bedrockFloor) {
//        // Condition sources
//        SurfaceRules.ConditionSource isAboveY97 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(97), 2);
//        SurfaceRules.ConditionSource isAboveY256 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(256), 0);
//        SurfaceRules.ConditionSource isBelowY63 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(63), -1);
//        SurfaceRules.ConditionSource isAboveY74 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(74), 1);
//        SurfaceRules.ConditionSource isAboveY60 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(60), 0);
//        SurfaceRules.ConditionSource isAboveY62 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(62), 0);
//        SurfaceRules.ConditionSource isAboveY63 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(63), 0);
//        SurfaceRules.ConditionSource isWaterAtOrAbove = SurfaceRules.waterBlockCheck(-1, 0);
//        SurfaceRules.ConditionSource isWater = SurfaceRules.waterBlockCheck(0, 0);
//        SurfaceRules.ConditionSource isWater6BlocksBelow = SurfaceRules.waterStartCheck(-6, -1);
//        SurfaceRules.ConditionSource isHole = SurfaceRules.hole();
//        SurfaceRules.ConditionSource isFrozenOcean = SurfaceRules.isBiome(Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN);
//        SurfaceRules.ConditionSource isSteep = SurfaceRules.steep();
//        SurfaceRules.ConditionSource isWarmOrBeach = SurfaceRules.isBiome(Biomes.WARM_OCEAN, Biomes.BEACH, Biomes.SNOWY_BEACH);
//        SurfaceRules.ConditionSource isDesert = SurfaceRules.isBiome(Biomes.DESERT);
//
//        // Surface noise conditions for badlands
//        SurfaceRules.ConditionSource surfaceNoiseLow = SurfaceRules.noiseCondition(Noises.SURFACE, -0.909, -0.5454);
//        SurfaceRules.ConditionSource surfaceNoiseMid = SurfaceRules.noiseCondition(Noises.SURFACE, -0.1818, 0.1818);
//        SurfaceRules.ConditionSource surfaceNoiseHigh = SurfaceRules.noiseCondition(Noises.SURFACE, 0.5454, 0.909);
//
//        // Rule sources
//        SurfaceRules.RuleSource grassOrDirt = SurfaceRules.sequence(SurfaceRules.ifTrue(isWater, GRASS_BLOCK), DIRT);
//        SurfaceRules.RuleSource sandAndSandstone = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, SANDSTONE), SAND);
//        SurfaceRules.RuleSource gravelAndStone = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, STONE), GRAVEL);
//
//        SurfaceRules.RuleSource mountainAndBeachMaterial = SurfaceRules.sequence(
//            SurfaceRules.ifTrue(
//                SurfaceRules.isBiome(Biomes.STONY_PEAKS),
//                SurfaceRules.sequence(
//                    SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.CALCITE, -0.0125, 0.0125), CALCITE),
//                    STONE
//                )
//            ),
//            SurfaceRules.ifTrue(
//                SurfaceRules.isBiome(Biomes.STONY_SHORE),
//                SurfaceRules.sequence(
//                    SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.GRAVEL, -0.05, 0.05), gravelAndStone),
//                    STONE
//                )
//            ),
//            SurfaceRules.ifTrue(
//                SurfaceRules.isBiome(Biomes.WINDSWEPT_HILLS),
//                SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE)
//            ),
//            SurfaceRules.ifTrue(isWarmOrBeach, sandAndSandstone),
//            SurfaceRules.ifTrue(isDesert, sandAndSandstone),
//            SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.DRIPSTONE_CAVES), STONE)
//        );
//
//        SurfaceRules.RuleSource powderSnowLayer = SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.45, 0.58), SurfaceRules.ifTrue(isWater, POWDER_SNOW));
//        SurfaceRules.RuleSource thickerPowderSnowLayer = SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.35, 0.6), SurfaceRules.ifTrue(isWater, POWDER_SNOW));
//
//        SurfaceRules.RuleSource erodedSurfaceMaterial = SurfaceRules.sequence(
//            SurfaceRules.ifTrue(
//                SurfaceRules.isBiome(Biomes.FROZEN_PEAKS),
//                SurfaceRules.sequence(
//                    SurfaceRules.ifTrue(isSteep, PACKED_ICE),
//                    SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.PACKED_ICE, -0.5F, 0.2), PACKED_ICE),
//                    SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.ICE, -0.0625F, 0.025), ICE),
//                    SurfaceRules.ifTrue(isWater, SNOW_BLOCK)
//                )
//            ),
//            SurfaceRules.ifTrue(
//                SurfaceRules.isBiome(Biomes.SNOWY_SLOPES),
//                SurfaceRules.sequence(
//                    SurfaceRules.ifTrue(isSteep, STONE),
//                    powderSnowLayer,
//                    SurfaceRules.ifTrue(isWater, SNOW_BLOCK)
//                )
//            ),
//            SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.JAGGED_PEAKS), STONE),
//            SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.GROVE), SurfaceRules.sequence(powderSnowLayer, DIRT)),
//            mountainAndBeachMaterial,
//            SurfaceRules.ifTrue(
//                SurfaceRules.isBiome(Biomes.WINDSWEPT_SAVANNA),
//                SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), STONE)
//            ),
//            SurfaceRules.ifTrue(
//                SurfaceRules.isBiome(Biomes.WINDSWEPT_GRAVELLY_HILLS),
//                SurfaceRules.sequence(
//                    SurfaceRules.ifTrue(surfaceNoiseAbove(2.0D), gravelAndStone),
//                    SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE),
//                    SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), DIRT),
//                    gravelAndStone
//                )
//            ),
//            SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.MANGROVE_SWAMP), MUD),
//            DIRT
//        );
//
//        SurfaceRules.RuleSource surfaceMaterial = SurfaceRules.sequence(
//            SurfaceRules.ifTrue(
//                SurfaceRules.isBiome(Biomes.FROZEN_PEAKS),
//                SurfaceRules.sequence(
//                    SurfaceRules.ifTrue(isSteep, PACKED_ICE),
//                    SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.PACKED_ICE, 0.0F, 0.2), PACKED_ICE),
//                    SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.ICE, 0.0F, 0.025), ICE),
//                    SurfaceRules.ifTrue(isWater, SNOW_BLOCK)
//                )
//            ),
//            SurfaceRules.ifTrue(
//                SurfaceRules.isBiome(Biomes.SNOWY_SLOPES),
//                SurfaceRules.sequence(
//                    SurfaceRules.ifTrue(isSteep, STONE),
//                    thickerPowderSnowLayer,
//                    SurfaceRules.ifTrue(isWater, SNOW_BLOCK)
//                )
//            ),
//            SurfaceRules.ifTrue(
//                SurfaceRules.isBiome(Biomes.JAGGED_PEAKS),
//                SurfaceRules.sequence(
//                    SurfaceRules.ifTrue(isSteep, STONE),
//                    SurfaceRules.ifTrue(isWater, SNOW_BLOCK)
//                )
//            ),
//            SurfaceRules.ifTrue(
//                SurfaceRules.isBiome(Biomes.GROVE),
//                SurfaceRules.sequence(
//                    thickerPowderSnowLayer,
//                    SurfaceRules.ifTrue(isWater, SNOW_BLOCK)
//                )
//            ),
//            mountainAndBeachMaterial,
//            SurfaceRules.ifTrue(
//                SurfaceRules.isBiome(Biomes.WINDSWEPT_SAVANNA),
//                SurfaceRules.sequence(
//                    SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), STONE),
//                    SurfaceRules.ifTrue(surfaceNoiseAbove(-0.5D), COARSE_DIRT)
//                )
//            ),
//            SurfaceRules.ifTrue(
//                SurfaceRules.isBiome(Biomes.WINDSWEPT_GRAVELLY_HILLS),
//                SurfaceRules.sequence(
//                    SurfaceRules.ifTrue(surfaceNoiseAbove(2.0D), gravelAndStone),
//                    SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE),
//                    SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), grassOrDirt),
//                    gravelAndStone
//                )
//            ),
//            SurfaceRules.ifTrue(
//                SurfaceRules.isBiome(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA),
//                SurfaceRules.sequence(
//                    SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), COARSE_DIRT),
//                    SurfaceRules.ifTrue(surfaceNoiseAbove(-0.95D), PODZOL)
//                )
//            ),
//            SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.ICE_SPIKES), SurfaceRules.ifTrue(isWater, SNOW_BLOCK)),
//            SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.MANGROVE_SWAMP), MUD),
//            SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.MUSHROOM_FIELDS), MYCELIUM),
//            grassOrDirt
//        );
//
//        SurfaceRules.RuleSource overworldSurfaceRules = SurfaceRules.sequence(
//            // Floor-specific rules
//            SurfaceRules.ifTrue(
//                SurfaceRules.ON_FLOOR,
//                SurfaceRules.sequence(
//                    SurfaceRules.ifTrue(
//                        SurfaceRules.isBiome(Biomes.WOODED_BADLANDS),
//                        SurfaceRules.ifTrue(
//                            isAboveY97,
//                            SurfaceRules.sequence(
//                                SurfaceRules.ifTrue(surfaceNoiseLow, COARSE_DIRT),
//                                SurfaceRules.ifTrue(surfaceNoiseMid, COARSE_DIRT),
//                                SurfaceRules.ifTrue(surfaceNoiseHigh, COARSE_DIRT),
//                                grassOrDirt
//                            )
//                        )
//                    ),
//                    SurfaceRules.ifTrue(
//                        SurfaceRules.isBiome(Biomes.SWAMP),
//                        SurfaceRules.ifTrue(
//                            isAboveY62,
//                            SurfaceRules.ifTrue(
//                                SurfaceRules.not(isAboveY63),
//                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SWAMP, 0.0D), WATER)
//                            )
//                        )
//                    ),
//                    SurfaceRules.ifTrue(
//                        SurfaceRules.isBiome(Biomes.MANGROVE_SWAMP),
//                        SurfaceRules.ifTrue(
//                            isAboveY60,
//                            SurfaceRules.ifTrue(
//                                SurfaceRules.not(isAboveY63),
//                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SWAMP, 0.0D), WATER)
//                            )
//                        )
//                    )
//                )
//            ),
//            // Badlands-specific rules
//            SurfaceRules.ifTrue(
//                SurfaceRules.isBiome(Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS),
//                SurfaceRules.sequence(
//                    SurfaceRules.ifTrue(
//                        SurfaceRules.ON_FLOOR,
//                        SurfaceRules.sequence(
//                            SurfaceRules.ifTrue(isAboveY256, ORANGE_TERRACOTTA),
//                            SurfaceRules.ifTrue(
//                                isAboveY74,
//                                SurfaceRules.sequence(
//                                    SurfaceRules.ifTrue(surfaceNoiseLow, TERRACOTTA),
//                                    SurfaceRules.ifTrue(surfaceNoiseMid, TERRACOTTA),
//                                    SurfaceRules.ifTrue(surfaceNoiseHigh, TERRACOTTA),
//                                    SurfaceRules.bandlands()
//                                )
//                            ),
//                            SurfaceRules.ifTrue(
//                                isWaterAtOrAbove,
//                                SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, RED_SANDSTONE), RED_SAND)
//                            ),
//                            SurfaceRules.ifTrue(SurfaceRules.not(isHole), ORANGE_TERRACOTTA),
//                            SurfaceRules.ifTrue(isWater6BlocksBelow, WHITE_TERRACOTTA),
//                            gravelAndStone
//                        )
//                    ),
//                    SurfaceRules.ifTrue(
//                        isBelowY63,
//                        SurfaceRules.sequence(
//                            SurfaceRules.ifTrue(
//                                isAboveY63,
//                                SurfaceRules.ifTrue(SurfaceRules.not(isAboveY74), ORANGE_TERRACOTTA)
//                            ),
//                            SurfaceRules.bandlands()
//                        )
//                    ),
//                    SurfaceRules.ifTrue(
//                        SurfaceRules.UNDER_FLOOR,
//                        SurfaceRules.ifTrue(isWater6BlocksBelow, WHITE_TERRACOTTA)
//                    )
//                )
//            ),
//            // Water-related rules
//            SurfaceRules.ifTrue(
//                SurfaceRules.ON_FLOOR,
//                SurfaceRules.ifTrue(
//                    isWaterAtOrAbove,
//                    SurfaceRules.sequence(
//                        SurfaceRules.ifTrue(
//                            isFrozenOcean,
//                            SurfaceRules.ifTrue(
//                                isHole,
//                                SurfaceRules.sequence(
//                                    SurfaceRules.ifTrue(isWater, AIR),
//                                    SurfaceRules.ifTrue(SurfaceRules.temperature(), ICE),
//                                    WATER
//                                )
//                            )
//                        ),
//                        surfaceMaterial
//                    )
//                )
//            ),
//            SurfaceRules.ifTrue(
//                isWater6BlocksBelow,
//                SurfaceRules.sequence(
//                    SurfaceRules.ifTrue(
//                        SurfaceRules.ON_FLOOR,
//                        SurfaceRules.ifTrue(
//                            isFrozenOcean,
//                            SurfaceRules.ifTrue(isHole, WATER)
//                        )
//                    ),
//                    SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, erodedSurfaceMaterial),
//                    SurfaceRules.ifTrue(isWarmOrBeach, SurfaceRules.ifTrue(SurfaceRules.DEEP_UNDER_FLOOR, SANDSTONE)),
//                    SurfaceRules.ifTrue(isDesert, SurfaceRules.ifTrue(SurfaceRules.VERY_DEEP_UNDER_FLOOR, SANDSTONE))
//                )
//            ),
//            // Default floor rules
//            SurfaceRules.ifTrue(
//                SurfaceRules.ON_FLOOR,
//                SurfaceRules.sequence(
//                    SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS), STONE),
//                    SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN), sandAndSandstone),
//                    gravelAndStone
//                )
//            )
//        );
//
//        ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
//        if (bedrockRoof) {
//            builder.add(SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK));
//        }
//
//        if (bedrockFloor) {
//            builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK));
//        }
//
//        SurfaceRules.RuleSource finalRules = SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), overworldSurfaceRules);
//        builder.add(aboveGround ? finalRules : overworldSurfaceRules);
//        builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("deepslate", VerticalAnchor.absolute(0), VerticalAnchor.absolute(8)), DEEPSLATE));
//
//        return SurfaceRules.sequence(builder.build().toArray(SurfaceRules.RuleSource[]::new));
//    }

    public static void setup(PlantopiaTerraBlenderEvent.@NotNull SurfaceRules event) {
        event.register(SurfaceRuleManager.RuleCategory.OVERWORLD, overworld());
    }

    @Contract("_ -> new")
    protected static SurfaceRules.@NotNull RuleSource makeStateRule(@NotNull Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
