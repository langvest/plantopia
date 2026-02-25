package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.worldgen.feature.config.PlantopiaPitConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.NotNull;

public class PlantopiaPitFeature extends Feature<PlantopiaPitConfiguration> {
    public PlantopiaPitFeature(Codec<PlantopiaPitConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<PlantopiaPitConfiguration> context) {
        var config = context.config();
        var level = context.level();
        var random = context.random();
        var centerPos = context.origin();
        int xzSpread = config.xzSpread().sample(random);
        int ySpread = config.ySpread().sample(random);
        var predicate = config.predicate();
        var heightmap = config.heightmap().orElse(Heightmap.Types.OCEAN_FLOOR_WG);
        int baseY = level.getHeight(heightmap, centerPos.getX(), centerPos.getZ());
        float curvature = config.curvature().sample(random);
        double frequency = 0.05 + curvature * 0.1;
        boolean successfullyPlaced = false;

        for (int dx = -xzSpread; dx <= xzSpread; dx++) {
            for (int dz = -xzSpread; dz <= xzSpread; dz++) {
                var xzPos = centerPos.offset(dx, 0, dz);

                if (isWithinShape(xzPos, dx, dz, xzSpread, curvature, frequency)) {
                    int y = level.getHeight(heightmap, xzPos.getX(), xzPos.getZ());

                    if (Math.abs(y - baseY) > ySpread) {
                        continue;
                    }

                    var mutablePos = new BlockPos.MutableBlockPos(xzPos.getX(), y, xzPos.getZ());
                    int depth = config.depth().sample(random);

                    for (int i = 0; i < depth; i++) {
                        mutablePos.move(0, -1, 0);

                        if (predicate.map(p -> p.test(level, mutablePos)).orElse(true)) {
                            level.setBlock(mutablePos, config.toPlace().getState(random, mutablePos), Block.UPDATE_CLIENTS);
                            successfullyPlaced = true;

                            if (i == 0) {
                                markAboveForPostProcessing(level, mutablePos);
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        }

        return successfullyPlaced;
    }

    protected boolean isWithinShape(BlockPos zxPos, int dx, int dz, int radius, float curvature, double frequency) {
        double distanceSq = dx * dx + dz * dz;
        if (distanceSq > radius * radius) {
            return false;
        }

        if (curvature == 0) {
            return true;
        }

        double distanceFalloff = Math.sqrt(distanceSq) / radius;

        @SuppressWarnings("removal")
        double noiseValue = Biome.BIOME_INFO_NOISE.getValue((double) zxPos.getX() * frequency, (double) zxPos.getZ() * frequency, false);
        double normalizedNoise = (noiseValue + 1.0) / 2.0;

        return normalizedNoise > distanceFalloff;
    }
}
