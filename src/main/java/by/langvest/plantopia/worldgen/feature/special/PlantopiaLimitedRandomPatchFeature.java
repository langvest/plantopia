package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.worldgen.feature.config.PlantopiaLimitedRandomPatchConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.NotNull;

public class PlantopiaLimitedRandomPatchFeature extends Feature<PlantopiaLimitedRandomPatchConfiguration> {
    public PlantopiaLimitedRandomPatchFeature(Codec<PlantopiaLimitedRandomPatchConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<PlantopiaLimitedRandomPatchConfiguration> context) {
        var config = context.config();
        var random = context.random();
        var centerPos = context.origin();
        var level = context.level();
        var feature = config.feature();
        int xzBound = config.xzSpread().sample(random) + 1;
        int yBound = config.ySpread().sample(random) + 1;
        int tries = config.tries().sample(random);
        int limit = config.limit().sample(random);

        int successfulPlacements = 0;
        var mutablePos = new BlockPos.MutableBlockPos();

        for (int i = 0; i < tries; i++) {
            if (successfulPlacements >= limit) {
                break;
            }

            int xOffset = random.nextInt(xzBound) - random.nextInt(xzBound);
            int yOffset = random.nextInt(yBound) - random.nextInt(yBound);
            int zOffset = random.nextInt(xzBound) - random.nextInt(xzBound);

            mutablePos.setWithOffset(centerPos, xOffset, yOffset, zOffset);

            if (feature.value().place(level, context.chunkGenerator(), random, mutablePos)) {
                successfulPlacements++;
            }
        }

        return successfulPlacements > 0;
    }
}
