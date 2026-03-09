package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.worldgen.feature.config.PlantopiaVegetationPatchConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Feature that generates a patch of blocks on a surface and then populates it with vegetation.
 * This is a refactored version for better readability and maintainability.
 * Now supports underwater generation and placing through non-solid blocks like flowers or grass.
 */
public class PlantopiaVegetationPatchFeature extends Feature<PlantopiaVegetationPatchConfiguration> {

    public PlantopiaVegetationPatchFeature(Codec<PlantopiaVegetationPatchConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<PlantopiaVegetationPatchConfiguration> context) {
        var level = context.level();
        var config = context.config();
        var random = context.random();
        var origin = context.origin();

        var xRadius = config.xzRadius().sample(random) + 1;
        var zRadius = config.xzRadius().sample(random) + 1;

        var groundPositions = placeGroundPatch(level, config, random, origin, xRadius, zRadius);
        if (groundPositions.isEmpty()) {
            return false;
        }

        distributeVegetation(context, level, config, random, groundPositions);
        return true;
    }

    protected Set<BlockPos> placeGroundPatch(WorldGenLevel level, @NotNull PlantopiaVegetationPatchConfiguration config, RandomSource random, @NotNull BlockPos origin, int xRadius, int zRadius) {
        var placedPositions = new HashSet<BlockPos>();

        for (int dx = -xRadius; dx <= xRadius; ++dx) {
            for (int dz = -zRadius; dz <= zRadius; ++dz) {
                var isXEdge = dx == -xRadius || dx == xRadius;
                var isZEdge = dz == -zRadius || dz == zRadius;
                var isCorner = isXEdge && isZEdge;

                if (isCorner) continue;

                var isEdge = isXEdge || isZEdge;
                if (isEdge && random.nextFloat() >= config.extraEdgeColumnChance()) {
                    continue;
                }

                tryPlacingColumn(level, config, random, origin, dx, dz, placedPositions);
            }
        }
        return placedPositions;
    }

    private void tryPlacingColumn(WorldGenLevel level, @NotNull PlantopiaVegetationPatchConfiguration config, RandomSource random, BlockPos origin, int dx, int dz, Set<BlockPos> placedPositions) {
        var columnPos = new BlockPos.MutableBlockPos().setWithOffset(origin, dx, 0, dz);

        findSuitableSurface(level, columnPos, config.verticalRange(), config.surface().getDirection());

        // The predicate checks the environment (air, water, flower) at the found surface.
        boolean canPlaceInEnvironment = config.predicate().test(level, columnPos);
        if (!canPlaceInEnvironment) {
            return;
        }

        // Safety check: Even if the predicate allows it, we should never place inside a solid block.
        var state = level.getBlockState(columnPos);
        if (state.isCollisionShapeFullBlock(level, columnPos)) {
            return;
        }

        // The position for the ground block is one block towards the surface direction from the passable block.
        var groundPos = new BlockPos.MutableBlockPos().set(columnPos).move(config.surface().getDirection());

        // Check if the ground below is sturdy.
        if (!level.getBlockState(groundPos).isFaceSturdy(level, groundPos, config.surface().getDirection().getOpposite())) {
            return;
        }

        var depth = config.depth().sample(random) + (config.extraBottomBlockChance() > 0.0F && random.nextFloat() < config.extraBottomBlockChance() ? 1 : 0);
        var topOfColumnPos = groundPos.immutable();

        if (placeGroundColumn(level, config, random, groundPos, depth)) {
            placedPositions.add(topOfColumnPos);
        }
    }

    private void findSuitableSurface(WorldGenLevel level, BlockPos.MutableBlockPos mutablePos, int verticalRange, net.minecraft.core.@NotNull Direction surfaceDirection) {
        var oppositeDirection = surfaceDirection.getOpposite();

        // Scan down through passable blocks (air, water, flowers) until we hit solid ground.
        for (int i = 0; i < verticalRange && isPassable(level.getBlockState(mutablePos)); ++i) {
            mutablePos.move(surfaceDirection);
        }

        // Scan back up until we are in a passable block again. This finds the first passable block above the solid ground.
        for (int i = 0; i < verticalRange && !isPassable(level.getBlockState(mutablePos)); ++i) {
            mutablePos.move(oppositeDirection);
        }
    }

    protected void distributeVegetation(FeaturePlaceContext<PlantopiaVegetationPatchConfiguration> context, WorldGenLevel level, PlantopiaVegetationPatchConfiguration config, RandomSource random, @NotNull Set<BlockPos> groundPositions) {
        for (var groundPos : groundPositions) {
            if (config.vegetationChance() > 0.0F && random.nextFloat() < config.vegetationChance()) {
                placeSingleVegetation(level, config, context.chunkGenerator(), random, groundPos);
            }
        }
    }

    protected void placeSingleVegetation(@NotNull WorldGenLevel level, @NotNull PlantopiaVegetationPatchConfiguration config, ChunkGenerator chunkGenerator, RandomSource random, @NotNull BlockPos groundPos) {
        var vegetationPos = groundPos.relative(config.surface().getDirection().getOpposite());
        var stateAtVegetationPos = level.getBlockState(vegetationPos);

        // Place vegetation only if the spot is air or water.
        if (stateAtVegetationPos.isAir() || stateAtVegetationPos.is(Blocks.WATER)) {
            config.vegetationFeature().value().place(level, chunkGenerator, random, vegetationPos);
        }
    }

    protected boolean placeGroundColumn(WorldGenLevel level, PlantopiaVegetationPatchConfiguration config, RandomSource random, BlockPos.MutableBlockPos mutablePos, int maxDepth) {
        for (int i = 0; i < maxDepth; ++i) {
            var stateToPlace = config.groundState().getState(random, mutablePos);
            var existingState = level.getBlockState(mutablePos);

            if (stateToPlace.is(existingState.getBlock())) {
                return true;
            }

            // Stop if the block is not replaceable by tag AND it's a solid, non-passable block.
            // This allows us to replace grass, flowers, and water, but not stone or ore.
            if (!existingState.is(config.replaceable()) && !isPassable(existingState)) {
                return i > 0;
            }

            level.setBlock(mutablePos, stateToPlace, 2);
            mutablePos.move(config.surface().getDirection());
        }
        return true;
    }

    /**
     * Checks if a block is "passable", meaning the feature can generate through it.
     * This includes air, liquids, and other non-solid blocks.
     *
     * @return true if the block is considered passable.
     */
    private boolean isPassable(@NotNull BlockState state) {
        return !state.canOcclude();
    }
}
