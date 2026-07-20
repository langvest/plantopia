package by.langvest.plantopia.worldgen.feature.foliageplacer;

import by.langvest.plantopia.worldgen.util.PlantopiaTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class PlantopiaFoliagePlacer extends FoliagePlacer {
    public PlantopiaFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    protected void placeRow(LevelSimulatedReader level, FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, FoliageAttachment attachment, int dy, int range, PlantopiaTemplate template) {
        boolean large = attachment.doubleTrunk();
        int i = large ? 1 : 0;
        var mutablePos = new BlockPos.MutableBlockPos();

        for (int dx = -range; dx <= range + i; dx++) {
            for (int dz = -range; dz <= range + i; dz++) {
                int templateDx = dx;
                int templateDz = dz;

                if (large) {
                    if (dx >= 1) templateDx = dx - 1;
                    if (dz >= 1) templateDz = dz - 1;
                }

                if (template.test(random, templateDx, templateDz, range)) {
                    mutablePos.setWithOffset(attachment.pos(), dx, dy, dz);
                    tryPlaceLeaf(level, foliageSetter, random, config, mutablePos);
                }
            }
        }
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int dx, int dy, int dz, int range, boolean large) {
        return false;
    }
}
