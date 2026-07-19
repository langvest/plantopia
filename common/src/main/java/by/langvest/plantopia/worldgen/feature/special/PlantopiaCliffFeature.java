package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaCliffConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaCliffFeature extends Feature<PlantopiaCliffConfiguration> {
    public PlantopiaCliffFeature(Codec<PlantopiaCliffConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PlantopiaCliffConfiguration> context) {
        var level = context.level();
        var origin = context.origin();
        var random = context.random();
        var config = context.config();
        var provider = config.provider();
        var predicate = config.predicate();
        var heightmap = Heightmap.Types.OCEAN_FLOOR_WG;
        var smoothness = config.smoothness();

        var mutablePos = new BlockPos.MutableBlockPos();
        var mask = new byte[18][18];
        boolean shouldPlace = false;

        for (int dx = -1; dx < 17; dx++) {
            for (int dz = -1; dz < 17; dz++) {
                int x = origin.getX() + dx;
                int z = origin.getZ() + dz;
                int y = level.getHeight(heightmap, x, z);

                if (!config.allowedBiomes().contains(level.getBiome(mutablePos.set(x, y, z)))) {
                    continue;
                }

                if (!isPotentiallySteep(level, x, y, z, heightmap)) {
                    continue;
                }

                if (
                    hasAroundGroundNeighbor(level, x, y, z, 1, mutablePos) &&
                    hasAroundGroundNeighbor(level, x, y, z, 2, mutablePos)
                ) {
                    mask[dx + 1][dz + 1] = 1;
                    shouldPlace = true;
                }
            }
        }

        if (!shouldPlace) return false;

        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                int sum = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        sum += mask[dx + 1 + i][dz + 1 + j];
                    }
                }

                boolean blurAllows = sum > smoothness;
                if (!blurAllows) continue;

                boolean maskAllows = mask[dx + 1][dz + 1] == 1;
                int x = origin.getX() + dx;
                int z = origin.getZ() + dz;
                int y = level.getHeight(heightmap, x, z);

                if (!maskAllows && !hasCloseGroundNeighbor(level, x, y, z, 1, mutablePos)) {
                    continue;
                }

                int northY = level.getHeight(heightmap, x, z - 1);
                int southY = level.getHeight(heightmap, x, z + 1);
                int westY = level.getHeight(heightmap, x - 1, z);
                int eastY = level.getHeight(heightmap, x + 1, z);

                int minY = Math.min(y, Math.min(northY, Math.min(southY, Math.min(westY, eastY))));
                int depth = config.depth().sample(random, y - minY);

                for (int i = 0; i < depth; i++) {
                    mutablePos.set(x, y - 1 - i, z);
                    if (predicate.test(level, mutablePos)) {
                        level.setBlock(mutablePos, provider.getState(random, mutablePos), Block.UPDATE_CLIENTS);

                        if (i == 0) {
                            markAboveForPostProcessing(level, mutablePos);
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        return true;
    }

    protected boolean isPotentiallySteep(WorldGenLevel level, int x, int y, int z, Heightmap.Types heightmap) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) continue;
                if (level.getHeight(heightmap, x + dx, z + dz) >= y + 2) {
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean hasAroundGroundNeighbor(WorldGenLevel level, int x, int y, int z, int offset, BlockPos.MutableBlockPos mutablePos) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) continue;
                mutablePos.set(x + dx, y + offset, z + dz);
                if (level.getBlockState(mutablePos).is(PlantopiaBlockTags.GROUND_OVERWORLD)) {
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean hasCloseGroundNeighbor(WorldGenLevel level, int x, int y, int z, int offset, BlockPos.MutableBlockPos mutablePos) {
        for (var direction : Direction.Plane.HORIZONTAL) {
            mutablePos.set(x + direction.getStepX(), y + offset, z + direction.getStepZ());
            if (level.getBlockState(mutablePos).is(PlantopiaBlockTags.GROUND_OVERWORLD)) {
                return true;
            }
        }

        return false;
    }
}
