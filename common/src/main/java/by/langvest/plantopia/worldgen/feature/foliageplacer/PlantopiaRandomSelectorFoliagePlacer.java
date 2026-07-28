package by.langvest.plantopia.worldgen.feature.foliageplacer;

import by.langvest.plantopia.worldgen.feature.PlantopiaFoliagePlacerTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaRandomSelectorFoliagePlacer extends PlantopiaDynamicFoliagePlacer {
    public static final Codec<PlantopiaRandomSelectorFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        SimpleWeightedRandomList.wrappedCodec(FoliagePlacer.CODEC).fieldOf("distribution").forGetter(it -> it.distribution)
    ).apply(instance, PlantopiaRandomSelectorFoliagePlacer::new));

    protected final SimpleWeightedRandomList<FoliagePlacer> distribution;

    public PlantopiaRandomSelectorFoliagePlacer(SimpleWeightedRandomList<FoliagePlacer> distribution) {
        this.distribution = distribution;
    }

    @Override
    protected void createDynamicFoliage(LevelSimulatedReader level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int treeHeight) {
        var foliagePlacer = distribution.getRandomValue(random).orElse(null);
        if (foliagePlacer == null) return;
        int foliageHeight = foliagePlacer.foliageHeight(random, treeHeight, config);
        int foliageRadius = foliagePlacer.foliageRadius(random, treeHeight - foliageHeight);
        foliagePlacer.createFoliage(level, blockSetter, random, config, maxFreeTreeHeight, attachment, foliageHeight, foliageRadius);
    }

    @Override
    protected @NotNull FoliagePlacerType<?> type() {
        return PlantopiaFoliagePlacerTypes.RANDOM_SELECTOR_FOLIAGE_PLACER.get();
    }
}
