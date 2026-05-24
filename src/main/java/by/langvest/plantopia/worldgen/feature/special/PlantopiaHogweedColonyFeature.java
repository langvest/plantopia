package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaInfestedDirtBlock;
import by.langvest.plantopia.block.special.PlantopiaInfestedGrassBlock;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.worldgen.placement.catalog.PlantopiaVegetationPlacements;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PlantopiaHogweedColonyFeature extends Feature<NoneFeatureConfiguration> {
    private static final int MAX_HEIGHT_DIFFERENCE = 3;

    public PlantopiaHogweedColonyFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        var level = context.level();
        var origin = context.origin();
        var random = context.random();
        var chunkGenerator = context.chunkGenerator();

        int minAge = PlantopiaInfestedDirtBlock.MIN_AGE;
        int startAge = random.nextIntBetweenInclusive(minAge, minAge + 8);

        Queue<BlockPos> queue = new ArrayDeque<>();
        Map<BlockPos, Integer> colonyPoints = new HashMap<>();
        Set<BlockPos> visitedPoints = new HashSet<>();
        var mutablePos = new BlockPos.MutableBlockPos();

        int centerSurfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, origin.getX(), origin.getZ());
        var centerPos = new BlockPos(origin.getX(), centerSurfaceY, origin.getZ());

        if (!isInfestable(level.getBlockState(centerPos.below()))) {
            return false;
        }

        queue.add(centerPos);
        visitedPoints.add(centerPos.atY(0));
        colonyPoints.put(centerPos, startAge);

        while (!queue.isEmpty()) {
            var currentPos = queue.poll();
            int currentAge = colonyPoints.get(currentPos);

            for (var direction : Direction.Plane.HORIZONTAL) {
                var neighborPos = mutablePos.set(currentPos).setY(0).move(direction);

                if (visitedPoints.contains(neighborPos)) {
                    continue;
                }

                if (!level.ensureCanWrite(neighborPos)) {
                    continue;
                }

                visitedPoints.add(neighborPos.immutable());

                int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, neighborPos.getX(), neighborPos.getZ());
                neighborPos.setY(surfaceY);

                if (Math.abs(currentPos.getY() - neighborPos.getY()) > MAX_HEIGHT_DIFFERENCE) {
                    continue;
                }

                if (!isInfestable(level.getBlockState(neighborPos.below()))) {
                    continue;
                }

                int newAge = PlantopiaInfestedDirtBlock.increaseAge(random, currentAge);
                if (newAge != currentAge) {
                    var immutableNeighborPos = neighborPos.immutable();
                    queue.add(immutableNeighborPos);
                    colonyPoints.put(immutableNeighborPos, newAge);
                }
            }
        }

        if (colonyPoints.isEmpty()) {
            return false;
        }

        for (Map.Entry<BlockPos, Integer> entry : colonyPoints.entrySet()) {
            var currentPos = mutablePos.set(entry.getKey().below());
            int currentAge = entry.getValue();

            while (currentAge <= PlantopiaInfestedDirtBlock.MAX_AGE) {
                var originalState = level.getBlockState(currentPos);
                var infestedState = getInfestedState(originalState, currentAge);

                if (infestedState == null) {
                    break;
                }

                setBlock(level, currentPos, infestedState);

                currentAge++;
                currentPos.move(Direction.DOWN);
            }
        }

        var hogweedFeature = level.registryAccess()
            .registryOrThrow(Registries.PLACED_FEATURE)
            .getHolder(PlantopiaVegetationPlacements.HOGWEED_INFESTED_GRASS_BLOCK);

        if (hogweedFeature.isPresent()) {
            for (BlockPos surfacePos : colonyPoints.keySet()) {
                var fluidState = level.getFluidState(surfacePos);
                if (!fluidState.isEmpty()) continue;
                if (random.nextFloat() > 0.6F) continue;
                hogweedFeature.get().value().place(level, chunkGenerator, random, surfacePos);
            }
        }

        return true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isInfestable(@NotNull BlockState state) {
        return state.is(PlantopiaBlockTags.INFESTED_DIRT_CAN_SPREAD_TO) || state.is(Blocks.GRASS_BLOCK);
    }

    @Nullable
    private BlockState getInfestedState(@NotNull BlockState state, int age) {
        if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.FARMLAND) || state.is(Blocks.DIRT_PATH)) {
            return PlantopiaBlocks.INFESTED_GRASS_BLOCK.get().defaultBlockState()
                .setValue(PlantopiaInfestedGrassBlock.AGE, age);
        }

        if (state.is(PlantopiaBlockTags.INFESTED_DIRT_CAN_SPREAD_TO)) {
            return PlantopiaBlocks.INFESTED_DIRT.get().defaultBlockState()
                .setValue(PlantopiaInfestedDirtBlock.AGE, age);
        }

        return null;
    }
}
