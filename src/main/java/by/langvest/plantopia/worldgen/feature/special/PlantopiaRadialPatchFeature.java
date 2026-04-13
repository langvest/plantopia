package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.util.helper.PlantopiaMathHelper;
import by.langvest.plantopia.worldgen.feature.blockplacer.PlantopiaBlockPlacer;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaRadialPatchConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlantopiaRadialPatchFeature extends Feature<PlantopiaRadialPatchConfiguration> {
    public PlantopiaRadialPatchFeature(Codec<PlantopiaRadialPatchConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<PlantopiaRadialPatchConfiguration> context) {
        var config = context.config();
        var random = context.random();
        var centerPos = context.origin();
        var level = context.level();
        int tries = config.tries().sample(random);
        int xzSpread = config.xzSpread().sample(random);
        int ySpread = config.ySpread().sample(random);

        int successfulPlacements = 0;

        for (int attempt = 0; attempt < tries; attempt++) {
            var targetPos = this.findTargetPos(context, xzSpread, ySpread);
            if (targetPos == null) {
                continue;
            }

            var selectedBlockPlacer = this.selectWeightedBlockPlacer(config, random);
            if (selectedBlockPlacer == null) {
                continue;
            }

            var blockPlaceContext = new PlantopiaBlockPlacer.Context(level, targetPos, centerPos, random, xzSpread, ySpread);
            if (selectedBlockPlacer.place(blockPlaceContext)) {
                successfulPlacements++;
            }
        }

        return successfulPlacements > 0;
    }

    @Nullable
    private BlockPos findTargetPos(FeaturePlaceContext<PlantopiaRadialPatchConfiguration> context, int xzSpread, int ySpread) {
        var xzPos = this.findXZPos(context, xzSpread);
        return this.findYPos(context, xzPos, ySpread);
    }

    @Nullable
    private BlockPos findYPos(@NotNull FeaturePlaceContext<PlantopiaRadialPatchConfiguration> context, @NotNull BlockPos xzPos, int ySpread) {
        var config = context.config();
        var random = context.random();
        var level = context.level();
        var centerPos = context.origin();
        var heightmap = config.heightmap();
        var predicate = config.predicate();

        int y = heightmap
            .map(type -> level.getHeight(type, xzPos.getX(), xzPos.getZ()))
            .orElseGet(() -> centerPos.getY() + random.nextInt(-ySpread, ySpread + 1));

        var targetPos = xzPos.atY(y);

        if (Math.abs(targetPos.getY() - centerPos.getY()) > ySpread) {
            return null;
        }

        if (predicate.isPresent() && !predicate.get().test(level, targetPos)) {
            return null;
        }

        return targetPos;
    }

    private @NotNull BlockPos findXZPos(@NotNull FeaturePlaceContext<PlantopiaRadialPatchConfiguration> context, int xzSpread) {
        var config = context.config();
        var random = context.random();
        var centerPos = context.origin();
        var sigma = config.sigma();
        var erosion = config.erosion();

        var xzOffset = PlantopiaMathHelper.getHorizontalRadialOffset(random, xzSpread, sigma, erosion);

        return centerPos.offset(xzOffset);
    }

    @Nullable
    private PlantopiaBlockPlacer selectWeightedBlockPlacer(@NotNull PlantopiaRadialPatchConfiguration config, RandomSource random) {
        List<PlantopiaBlockPlacer> blocks = config.blocks();

        if (blocks.isEmpty()) {
            return null;
        }

        int totalWeight = 0;

        for (PlantopiaBlockPlacer block : blocks) {
            totalWeight += block.weight();
        }

        if (totalWeight <= 0) {
            return null;
        }

        int roll = random.nextInt(totalWeight);

        for (PlantopiaBlockPlacer block : blocks) {
            roll -= block.weight();
            if (roll < 0) {
                return block;
            }
        }

        return null;
    }
}
