package by.langvest.plantopia.worldgen.feature.trunkplacer;

import by.langvest.plantopia.worldgen.feature.PlantopiaTrunkPlacerTypes;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
public class PlantopiaStraightTrunkPlacer extends TrunkPlacer {
    public static final Codec<PlantopiaStraightTrunkPlacer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        IntProvider.codec(0, 32).fieldOf("base_height").forGetter(it -> it.baseHeight),
        IntProvider.codec(0, 32).fieldOf("bonus_height").forGetter(it -> it.bonusHeight)
    ).apply(instance, PlantopiaStraightTrunkPlacer::new));

    protected final IntProvider baseHeight;
    protected final IntProvider bonusHeight;

    public PlantopiaStraightTrunkPlacer(IntProvider baseHeight, IntProvider heightRand) {
        super(0, 0, 0);
        this.baseHeight = baseHeight;
        this.bonusHeight = heightRand;
    }

    @Override
    protected @NotNull TrunkPlacerType<?> type() {
        return PlantopiaTrunkPlacerTypes.STRAIGHT_TRUNK_PLACER.get();
    }

    @Override
    public int getTreeHeight(RandomSource random) {
        return baseHeight.sample(random) + bonusHeight.sample(random);
    }

    @Override
    public @NotNull List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, int freeTreeHeight, BlockPos pos, TreeConfiguration config) {
        setDirtAt(level, blockSetter, random, pos.below(), config);

        for (int i = 0; i < freeTreeHeight; i++) {
            placeLog(level, blockSetter, random, pos.above(i), config);
        }

        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(pos.above(freeTreeHeight), 0, false));
    }
}
