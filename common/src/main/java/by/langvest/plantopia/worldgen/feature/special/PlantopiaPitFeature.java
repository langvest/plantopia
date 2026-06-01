package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.util.helper.PlantopiaMathHelper;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaPitConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaPitFeature extends Feature<PlantopiaPitConfiguration> {
    public PlantopiaPitFeature(Codec<PlantopiaPitConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PlantopiaPitConfiguration> context) {
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
        var mutablePos = new BlockPos.MutableBlockPos();
        boolean successfullyPlaced = false;

        for (int dx = -xzSpread; dx <= xzSpread; dx++) {
            for (int dz = -xzSpread; dz <= xzSpread; dz++) {
                int x = centerPos.getX() + dx;
                int z = centerPos.getZ() + dz;

                if (!PlantopiaMathHelper.isWithinShape(x, z, dx, dz, xzSpread, curvature, frequency)) {
                    continue;
                }

                int y = level.getHeight(heightmap, x, z);

                if (Math.abs(y - baseY) > ySpread) {
                    continue;
                }

                mutablePos.set(x, y, z);

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

        return successfullyPlaced;
    }
}
