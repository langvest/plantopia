package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.worldgen.util.PlantopiaThresholdType;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaPoiAnchorConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaPoiAnchorFeature extends Feature<PlantopiaPoiAnchorConfiguration> {
    public PlantopiaPoiAnchorFeature(com.mojang.serialization.Codec<PlantopiaPoiAnchorConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PlantopiaPoiAnchorConfiguration> context) {
        var level = context.level();
        var originPos = context.origin();
        var random = context.random();
        var config = context.config();
        var generator = context.chunkGenerator();
        var worldGenContext = new WorldGenerationContext(generator, level);

        int topAnchorY = config.topAnchor().resolveY(worldGenContext);
        int bottomAnchorY = config.bottomAnchor().resolveY(worldGenContext);

        var heightmap = config.heightmap().orElse(Heightmap.Types.OCEAN_FLOOR_WG);
        int surfaceY = level.getHeight(heightmap, originPos.getX(), originPos.getZ());

        int searchTop;
        int searchBottom;

        switch (config.dipType()) {
            case SURFACE_ONLY -> {
                searchTop = surfaceY;
                searchBottom = surfaceY;
            }
            case SURFACE_OR_CAVE -> {
                searchTop = surfaceY;
                searchBottom = bottomAnchorY;
            }
            case CAVE_ONLY -> {
                searchTop = surfaceY - 1;
                searchBottom = bottomAnchorY;
            }
            default -> {
                return false;
            }
        }

        searchTop = Math.min(searchTop, topAnchorY);

        if (searchTop < searchBottom) {
            return false;
        }

        int originY = originPos.getY();
        int avoidDistance = config.originAvoidDistance().sample(random);
        var predicate = config.predicate().orElse(BlockPredicate.alwaysTrue());
        var mutablePos = new BlockPos.MutableBlockPos();
        var allowedThresholds = config.allowedThresholds();
        boolean isAllowedAbove = allowedThresholds.contains(PlantopiaThresholdType.ABOVE);
        boolean isAllowedBelow = allowedThresholds.contains(PlantopiaThresholdType.BELOW);

        for (int y = searchTop; y >= searchBottom; y--) {
            if (y > originY) {
                if (!isAllowedAbove) {
                    continue;
                }
            } else if (y < originY) {
                if (!isAllowedBelow) {
                    continue;
                }
            } else {
                continue;
            }

            if (Math.abs(y - originY) <= avoidDistance) {
                continue;
            }

            mutablePos.set(originPos.getX(), y, originPos.getZ());

            if (predicate.test(level, mutablePos)) {
                if (config.feature().value().place(level, generator, random, mutablePos)) {
                    return true;
                }
            }
        }

        return false;
    }
}
