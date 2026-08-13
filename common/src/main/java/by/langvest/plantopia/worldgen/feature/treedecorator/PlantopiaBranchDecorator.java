package by.langvest.plantopia.worldgen.feature.treedecorator;

import by.langvest.plantopia.block.special.PlantopiaBalkBlock;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.worldgen.feature.PlantopiaTreeDecoratorTypes;
import by.langvest.plantopia.worldgen.util.intproportion.PlantopiaIntProportion;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ColumnPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;

@ParametersAreNonnullByDefault
public class PlantopiaBranchDecorator extends TreeDecorator {
    public static final Codec<PlantopiaBranchDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.POSITIVE_FLOAT.fieldOf("probability").forGetter(it -> it.probability),
        Codec.intRange(1, 4).fieldOf("max_branches_per_block").orElse(1).forGetter(it -> it.maxBranchesPerBlock),
        Entry.CODEC.listOf().fieldOf("entries").forGetter(it -> it.entries)
    ).apply(instance, PlantopiaBranchDecorator::new));

    protected final float probability;
    protected final int maxBranchesPerBlock;
    protected final List<Entry> entries;

    public PlantopiaBranchDecorator(float probability, int maxBranchesPerBlock, List<Entry> entries) {
        this.probability = probability;
        this.maxBranchesPerBlock = maxBranchesPerBlock;
        this.entries = entries;
    }

    @Contract("_, _ -> new")
    public static @NotNull Builder builder(float probability, int maxBranchesPerBlock) {
        return new Builder(probability, maxBranchesPerBlock);
    }

    @Override
    protected @NotNull TreeDecoratorType<?> type() {
        return PlantopiaTreeDecoratorTypes.BRANCH.get();
    }

    @Override
    public void place(Context context) {
        if (entries.isEmpty()) return;

        var logs = context.logs();
        if (logs.isEmpty()) return;

        var trunkBox = getBoundingBoxForTrunk(context, logs);
        if (trunkBox == null) return;
        
        int trunkHeight = trunkBox.maxY() - trunkBox.minY();
        var random = context.random();
        var resolvedEntries = resolveEntries(random, trunkHeight, entries);

        for (var pos : logs) {
            if (!trunkBox.isInside(pos)) continue;

            int height = pos.getY() - trunkBox.minY() + 1;
            var provider = getProviderForHeight(height, resolvedEntries);
            if (provider == null) continue;

            placeBranchesForBlock(context, pos, provider, trunkBox);
        }
    }

    private @Nullable BoundingBox getBoundingBoxForTrunk(Context context, List<BlockPos> logs) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;

        for (var pos : logs) {
            if (pos.getY() < minY && context.level().isStateAtPosition(pos, state -> state.is(BlockTags.LOGS) || state.is(PlantopiaBlockTags.BALKS))) {
                minY = pos.getY();
            }
        }
        if (minY == Integer.MAX_VALUE) return null;

        Set<Long> baseColumns = Sets.newHashSet();
        for (var pos : logs) {
            if (pos.getY() == minY) {
                baseColumns.add(ColumnPos.asLong(pos.getX(), pos.getZ()));
            }
        }
        if (baseColumns.isEmpty()) return null;

        for (var pos : logs) {
            if (pos.getY() >= minY && baseColumns.contains(ColumnPos.asLong(pos.getX(), pos.getZ()))) {
                minX = Math.min(minX, pos.getX());
                minZ = Math.min(minZ, pos.getZ());
                maxX = Math.max(maxX, pos.getX());
                maxY = Math.max(maxY, pos.getY());
                maxZ = Math.max(maxZ, pos.getZ());
            }
        }
        if (maxY == Integer.MIN_VALUE) return null;

        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private void placeBranchesForBlock(Context context, BlockPos pos, BlockStateProvider provider, BoundingBox trunkBox) {
        var random = context.random();
        var directions = Direction.Plane.HORIZONTAL.shuffledCopy(random);
        var mutablePos = new BlockPos.MutableBlockPos();

        int branchesPlaced = 0;
        for (var direction : directions) {
            if (branchesPlaced >= maxBranchesPerBlock) break;
            if (random.nextFloat() >= probability) continue;

            mutablePos.setWithOffset(pos, direction);
            if (context.isAir(mutablePos) && !trunkBox.isInside(mutablePos)) {
                updateLog(context, pos, direction);
                placeBranch(context, mutablePos, direction, provider);
                branchesPlaced++;
            }
        }
    }

    private @Nullable BlockStateProvider getProviderForHeight(int height, List<ResolvedEntry> resolvedEntries) {
        for (var entry : resolvedEntries) {
            if (height >= entry.stratHeight) {
                return entry.provider;
            }
        }
        return null;
    }

    private void updateLog(Context context, BlockPos pos, Direction direction) {
        context.level().isStateAtPosition(pos, state -> {
            if (state.is(PlantopiaBlockTags.BALKS)) {
                context.setBlock(pos, state.setValue(PlantopiaBalkBlock.getSegmentProperty(direction), true));
            }
            return true;
        });
    }

    private void placeBranch(Context context, BlockPos pos, Direction direction, BlockStateProvider provider) {
        var level = context.level();
        var random = context.random();
        var state = provider.getState(random, pos);

        if (state.isAir()) return;

        if (state.hasProperty(BlockStateProperties.FACING)) {
            state = state.setValue(BlockStateProperties.FACING, direction);
        } else if (state.hasProperty(BlockStateProperties.AXIS)) {
            state = state.setValue(BlockStateProperties.AXIS, direction.getAxis());
        }

        if (state.getBlock() instanceof PlantopiaBalkBlock) {
            state = PlantopiaBalkBlock.getDirectedStraightState(state, direction);
        }

        context.setBlock(pos, copyWaterloggedFrom(level, pos, state));
    }
    
    protected List<ResolvedEntry> resolveEntries(RandomSource random, int treeHeight, List<Entry> entries) {
        return entries.stream().map(entry -> entry.sample(random, treeHeight)).toList();
    } 

    public record Entry(
        PlantopiaIntProportion stratHeight,
        BlockStateProvider provider
    ) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PlantopiaIntProportion.CODEC.fieldOf("start_height").forGetter(Entry::stratHeight),
            BlockStateProvider.CODEC.fieldOf("provider").forGetter(Entry::provider)
        ).apply(instance, Entry::new));
        
        @Contract("_, _ -> new")
        public @NotNull ResolvedEntry sample(RandomSource random, int treeHeight) {
            return new ResolvedEntry(stratHeight.sample(random, treeHeight), provider);
        }
    }

    public record ResolvedEntry(
        int stratHeight,
        BlockStateProvider provider
    ) {}

    public static class Builder {
        private final float probability;
        private final int maxBranchesPerBlock;
        private final List<Entry> entries = new ArrayList<>();

        public Builder(float probability, int maxBranchesPerBlock) {
            this.probability = probability;
            this.maxBranchesPerBlock = maxBranchesPerBlock;
        }

        public Builder add(PlantopiaIntProportion stratHeight, BlockStateProvider provider) {
            entries.add(new Entry(stratHeight, provider));
            return this;
        }

        public PlantopiaBranchDecorator build() {
            return new PlantopiaBranchDecorator(probability, maxBranchesPerBlock, entries);
        }
    }
}
