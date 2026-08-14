package by.langvest.plantopia.worldgen.feature.trunkplacer;

import by.langvest.plantopia.block.PlantopiaDirtUtils;
import by.langvest.plantopia.worldgen.feature.PlantopiaTrunkPlacerTypes;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
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
        IntProvider.codec(0, 32).fieldOf("bonus_height").forGetter(it -> it.bonusHeight),
        Codec.BOOL.fieldOf("convert_dirt").orElse(true).forGetter(it -> it.convertDirt)
    ).apply(instance, PlantopiaStraightTrunkPlacer::new));

    protected final IntProvider baseHeight;
    protected final IntProvider bonusHeight;
    protected final boolean convertDirt;

    public PlantopiaStraightTrunkPlacer(IntProvider baseHeight) {
        this(baseHeight, ConstantInt.of(0), true);
    }

    public PlantopiaStraightTrunkPlacer(IntProvider baseHeight, boolean convertDirt) {
        this(baseHeight, ConstantInt.of(0), convertDirt);
    }

    public PlantopiaStraightTrunkPlacer(IntProvider baseHeight, IntProvider bonusHeight) {
        this(baseHeight, bonusHeight, true);
    }

    public PlantopiaStraightTrunkPlacer(IntProvider baseHeight, IntProvider bonusHeight, boolean convertDirt) {
        super(0, 0, 0);
        this.baseHeight = baseHeight;
        this.bonusHeight = bonusHeight;
        this.convertDirt = convertDirt;
    }

    @Override
    protected @NotNull TrunkPlacerType<?> type() {
        return PlantopiaTrunkPlacerTypes.STRAIGHT_TRUNK_PLACER.get();
    }

    @Override
    public int getTreeHeight(RandomSource random) {
        return baseHeight.sample(random) + bonusHeight.sample(random);
    }

    protected boolean shouldPlaceDirtInsteadOf(BlockState state, TreeConfiguration config) {
        if (config.forceDirt) return true;
        return convertDirt && PlantopiaDirtUtils.canConvertToDirt(state);
    }

    protected void placeDirtIfNeeded(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, BlockPos pos, TreeConfiguration config) {
        level.isStateAtPosition(pos, state -> {
            if (shouldPlaceDirtInsteadOf(state, config)) {
                var dirtState = config.dirtProvider.getState(random, pos);
                blockSetter.accept(pos, PlantopiaDirtUtils.getCorrespondingState(state, dirtState));
            }
            return true;
        });
    }

    @Override
    public @NotNull List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, int freeTreeHeight, BlockPos pos, TreeConfiguration config) {
        var mutablePos = new BlockPos.MutableBlockPos();

        mutablePos.setWithOffset(pos, Direction.DOWN);
        placeDirtIfNeeded(level, blockSetter, random, mutablePos, config);

        for (int i = 0; i < freeTreeHeight; i++) {
            mutablePos.move(Direction.UP);
            placeLog(level, blockSetter, random, mutablePos, config);
        }

        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(pos.above(freeTreeHeight), 0, false));
    }
}
