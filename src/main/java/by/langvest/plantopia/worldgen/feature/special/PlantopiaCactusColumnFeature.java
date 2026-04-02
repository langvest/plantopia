package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaTinyCactusBlock;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import org.jetbrains.annotations.NotNull;

public class PlantopiaCactusColumnFeature extends Feature<BlockColumnConfiguration> {
    public PlantopiaCactusColumnFeature(Codec<BlockColumnConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<BlockColumnConfiguration> context) {
        var config = context.config();
        var layers = config.layers();

        return PlantopiaNaturalBlockColumnFeature.place(
            context.level(),
            context.origin(),
            context.random(),
            config.direction(),
            config.allowedPlacement(),
            config.prioritizeTip(),
            Block.UPDATE_CLIENTS,
            layers.size(),
            PlantopiaNaturalBlockColumnFeature.simpleHeightProvider(layers),
            PlantopiaNaturalBlockColumnFeature.simpleStateProvider(layers),
            (level, pos, state, flags, random, layerIndex, blockIndex, layerHeight, totalHeight) -> {
                boolean isLastLayer = layerIndex == layers.size() - 1;
                boolean isLastBlock = blockIndex == layerHeight - 1;
                boolean allowTopDecoration = isLastLayer && isLastBlock;

                placeCactusBlock(level, pos, state, flags, random, allowTopDecoration);
            }
        );
    }

    public static boolean placeCactusBlock(@NotNull LevelAccessor level, BlockPos pos, BlockState state, int flags, RandomSource random, boolean allowTopDecoration) {
        if (level.setBlock(pos, state, flags)) {
            var decorationDirections = Lists.newArrayList(Direction.Plane.HORIZONTAL);

            if (allowTopDecoration) {
                decorationDirections.add(Direction.UP);
            }

            for (var direction : decorationDirections) {
                var attachedPos = pos.relative(direction);
                var attachedState = level.getBlockState(attachedPos);

                if (attachedState.isAir() && random.nextInt(6) == 0) {
                    var decorationState = getRandomTinyCactus(random).defaultBlockState()
                        .setValue(PlantopiaTinyCactusBlock.FACING, direction);

                    level.setBlock(attachedPos, decorationState, Block.UPDATE_ALL);
                }
            }

            return true;
        }

        return false;
    }

    public static Block getRandomTinyCactus(@NotNull RandomSource random) {
        if (random.nextInt(3) == 0) {
            return PlantopiaBlocks.FLOWERING_TINY_CACTUS.get();
        }

        return PlantopiaBlocks.TINY_CACTUS.get();
    }
}
