package by.langvest.plantopia.client.color;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.blockentity.special.PlantopiaSeaShellBlockEntity;
import by.langvest.plantopia.block.special.PlantopiaSeaShellBlock;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.property.PlantopiaTintType;
import by.langvest.plantopia.util.helper.PlantopiaBlockHelper;
import by.langvest.toolkit.meta.SimpleMetaObject;
import com.google.common.collect.Sets;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import static by.langvest.plantopia.util.helper.PlantopiaColorHelper.hexToInt;

@ParametersAreNonnullByDefault
public class PlantopiaColors {
    private static final Set<Block> BLOCK_GRASS_COLOR_0 = Sets.newHashSet();
    private static final Set<Block> BLOCK_GRASS_COLOR_1 = Sets.newHashSet();
    private static final Set<Block> BLOCK_FOLIAGE_COLOR_0 = Sets.newHashSet();
    private static final Set<Block> BLOCK_FOLIAGE_COLOR_1 = Sets.newHashSet();
    private static final Set<Block> BLOCK_LILY_PAD_COLOR_0 = Sets.newHashSet();
    private static final Set<Block> BLOCK_LILY_PAD_COLOR_1 = Sets.newHashSet();
    private static final Set<Block> BLOCK_WATERLILY_COLOR_0 = Sets.newHashSet();
    private static final Set<Block> BLOCK_WATERLILY_COLOR_1 = Sets.newHashSet();
    private static final Set<Item> ITEM_GRASS_COLOR_0 = Sets.newHashSet();
    private static final Set<Item> ITEM_GRASS_COLOR_1 = Sets.newHashSet();
    private static final Set<Item> ITEM_BLOCK_COLOR_INHERIT = Sets.newHashSet();

    private static final int LILY_PAD_BLOCK_COLOR = hexToInt("#208030");
    private static final int LILY_PAD_ITEM_COLOR = hexToInt("#71C35C");
    private static final int WATERLILY_COLOR = hexToInt("#76B625");

    private static final EnumSet<ColorPhase> completedPhases = EnumSet.noneOf(ColorPhase.class);

    private static void runColorPhaseOnce(ColorPhase phase, Runnable task) {
        if (!completedPhases.add(phase)) return;
        task.run();
    }

    public static void setupCommonColors() {
        runColorPhaseOnce(ColorPhase.COMMON, () -> {
            generateAll();

            seaShellBlocks();
            pottedFernBlock(Blocks.POTTED_FERN);
            herbBlock(PlantopiaBlocks.FIREWEED.get());
            herbBlock(PlantopiaBlocks.CHICORY.get());
            herbBlock(PlantopiaBlocks.CARROTWEED.get());
            herbBlock(PlantopiaBlocks.TANSY.get());
        });
    }

    public static void setupBlockColors() {
        runColorPhaseOnce(ColorPhase.BLOCK, () -> {
            PlantopiaBlockColors.add(BLOCK_GRASS_COLOR_0, (state, level, pos, tintIndex) -> grassTint(state, level, pos, tintIndex, 0));
            PlantopiaBlockColors.add(BLOCK_GRASS_COLOR_1, (state, level, pos, tintIndex) -> grassTint(state, level, pos, tintIndex, 1));
            PlantopiaBlockColors.add(BLOCK_FOLIAGE_COLOR_0, (state, level, pos, tintIndex) -> foliageTint(state, level, pos, tintIndex, 0));
            PlantopiaBlockColors.add(BLOCK_FOLIAGE_COLOR_1, (state, level, pos, tintIndex) -> foliageTint(state, level, pos, tintIndex, 1));
            PlantopiaBlockColors.add(BLOCK_LILY_PAD_COLOR_0, (state, level, pos, tintIndex) -> lilyPadTint(state, level, pos, tintIndex, 0));
            PlantopiaBlockColors.add(BLOCK_LILY_PAD_COLOR_1, (state, level, pos, tintIndex) -> lilyPadTint(state, level, pos, tintIndex, 1));
            PlantopiaBlockColors.add(BLOCK_WATERLILY_COLOR_0, (state, level, pos, tintIndex) -> waterlilyTint(state, level, pos, tintIndex, 0));
            PlantopiaBlockColors.add(BLOCK_WATERLILY_COLOR_1, (state, level, pos, tintIndex) -> waterlilyTint(state, level, pos, tintIndex, 1));
        });
    }

    public static void setupItemColors(BlockColors blockColors) {
        runColorPhaseOnce(ColorPhase.ITEM, () -> {
            PlantopiaItemColors.add(ITEM_GRASS_COLOR_0, (itemStuck, tintIndex) -> grassTint(tintIndex, 0));
            PlantopiaItemColors.add(ITEM_GRASS_COLOR_1, (itemStuck, tintIndex) -> grassTint(tintIndex, 1));
            PlantopiaItemColors.add(ITEM_BLOCK_COLOR_INHERIT, (itemStack, tintIndex) -> {
                var item = (BlockItem) itemStack.getItem();
                var state = item.getBlock().defaultBlockState();
                return blockColors.getColor(state, null, null, tintIndex);
            });
        });
    }

    /* GENERATED TINTS ******************************************/

    private static void generateAll() {
        PlantopiaMetaBuckets.BLOCK.forEach(blockMeta -> {
            if (!blockMeta.shouldApplyTint()) return;

            var block = blockMeta.get();
            var tintType = blockMeta.getTintType();

            if (blockMeta.shouldApplyTintToItem()) ITEM_BLOCK_COLOR_INHERIT.add(block.asItem());

            if (tintType == PlantopiaTintType.GRASS) {
                if (blockMeta.shouldApplyTintToParticles()) BLOCK_GRASS_COLOR_0.add(block);
                else BLOCK_GRASS_COLOR_1.add(block);
                return;
            }

            if (tintType == PlantopiaTintType.LILY_PAD) {
                if (blockMeta.shouldApplyTintToParticles()) BLOCK_LILY_PAD_COLOR_0.add(block);
                else BLOCK_LILY_PAD_COLOR_1.add(block);
                return;
            }

            if (tintType == PlantopiaTintType.WATERLILY) {
                if (blockMeta.shouldApplyTintToParticles()) BLOCK_WATERLILY_COLOR_0.add(block);
                else BLOCK_WATERLILY_COLOR_1.add(block);
                return;
            }

            if (tintType == PlantopiaTintType.FOLIAGE) {
                if (blockMeta.shouldApplyTintToParticles()) BLOCK_FOLIAGE_COLOR_0.add(block);
                else BLOCK_FOLIAGE_COLOR_1.add(block);
            }
        });
    }

    /* CUSTOM TINTS ******************************************/

    private static void seaShellBlocks() {
        Set<Block> seaShellBlocks = PlantopiaMetaBuckets.BLOCK.stream()
            .map(SimpleMetaObject::get)
            .filter(block -> block instanceof PlantopiaSeaShellBlock)
            .collect(Collectors.toSet());

        Set<Item> seaShellItems = seaShellBlocks.stream()
            .map(Block::asItem)
            .collect(Collectors.toSet());

        PlantopiaBlockColors.add(seaShellBlocks, (state, level, pos, tintIndex) -> {
            if (tintIndex == 1 && level != null && pos != null && level.getBlockEntity(pos) instanceof PlantopiaSeaShellBlockEntity seaShellBlockEntity) {
                return seaShellBlockEntity.getColor();
            }

            return noColor();
        });

        PlantopiaItemColors.add(seaShellItems, (itemStack, tintIndex) -> {
            if (tintIndex == 1) {
                var tag = BlockItem.getBlockEntityData(itemStack);

                if (tag != null && tag.contains("Color")) {
                    return tag.getInt("Color");
                } else {
                    return PlantopiaSeaShellBlockEntity.DEFAULT_COLOR;
                }
            }

            return noColor();
        });
    }

    private static void herbBlock(Block block) {
        ITEM_GRASS_COLOR_0.add(block.asItem());

        PlantopiaBlockColors.add(block, (state, level, pos, tintIndex) -> {
            var half = state.getValue(DoublePlantBlock.HALF);
            var basePos = getBaseBlockPos(state, pos);

            if (half == DoubleBlockHalf.UPPER && tintIndex == 1) return grassColor(level, basePos);
            if (half == DoubleBlockHalf.LOWER && tintIndex == 0) return grassColor(level, basePos);
            return noColor();
        });
    }

    @SuppressWarnings("SameParameterValue")
    private static void pottedFernBlock(Block block) {
        BLOCK_GRASS_COLOR_1.add(block);
    }

    /* COLORS ******************************************/

    private static int noColor() {
        return -1;
    }

    private static int grassColor(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        if (level == null || pos == null) return GrassColor.get(0.5D, 1.0D);
        return BiomeColors.getAverageGrassColor(level, pos);
    }

    private static int foliageColor(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        if (level == null || pos == null) return FoliageColor.getDefaultColor();
        return BiomeColors.getAverageFoliageColor(level, pos);
    }

    private static int lilyPadColor(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        if (level == null || pos == null) return LILY_PAD_ITEM_COLOR;
        return LILY_PAD_BLOCK_COLOR;
    }

    private static int waterlilyColor(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return WATERLILY_COLOR;
    }

    /* TINTS ******************************************/

    private static int grassTint(int tintIndex, int targetTintIndex) {
        return grassTint(null, null, null, tintIndex, targetTintIndex);
    }

    private static int grassTint(@Nullable BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex, int targetTintIndex) {
        return tintIndex == targetTintIndex ? grassColor(level, getBaseBlockPos(state, pos)) : noColor();
    }

    private static int foliageTint(@Nullable BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex, int targetTintIndex) {
        return tintIndex == targetTintIndex ? foliageColor(level, getBaseBlockPos(state, pos)) : noColor();
    }

    private static int lilyPadTint(@Nullable BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex, int targetTintIndex) {
        return tintIndex == targetTintIndex ? lilyPadColor(level, getBaseBlockPos(state, pos)) : noColor();
    }

    private static int waterlilyTint(@Nullable BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex, int targetTintIndex) {
        return tintIndex == targetTintIndex ? waterlilyColor(level, getBaseBlockPos(state, pos)) : noColor();
    }

    /* HELPER METHODS ******************************************/

    @Nullable
    private static BlockPos getBaseBlockPos(@Nullable BlockState state, @Nullable BlockPos pos) {
        if (state == null || pos == null) return null;
        return PlantopiaBlockHelper.getBaseBlockPos(state, pos);
    }

    public enum ColorPhase {
        COMMON, BLOCK, ITEM
    }
}