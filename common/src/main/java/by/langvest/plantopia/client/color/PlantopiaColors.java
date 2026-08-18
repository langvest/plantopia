package by.langvest.plantopia.client.color;

import by.langvest.plantopia.blockentity.special.PlantopiaSeaShellBlockEntity;
import by.langvest.plantopia.block.special.PlantopiaSeaShellBlock;
import by.langvest.plantopia.extension.PlantopiaBiomeGetterExtension;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.property.PlantopiaTintType;
import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import by.langvest.plantopia.util.helper.PlantopiaBlockHelper;
import by.langvest.toolkit.meta.SimpleMetaObject;
import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Cursor3D;
import net.minecraft.core.Holder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static by.langvest.plantopia.util.helper.PlantopiaColorHelper.hexToInt;

/**
 * @see net.minecraft.client.color.block.BlockColors
 */
@ParametersAreNonnullByDefault
public class PlantopiaColors {
    private static final Set<Block> BLOCK_GRASS_COLOR_ALL = Sets.newHashSet();
    private static final Set<Block> BLOCK_FOLIAGE_COLOR_ALL = Sets.newHashSet();
    private static final Set<Block> BLOCK_LILY_PAD_COLOR_ALL = Sets.newHashSet();
    private static final Set<Block> BLOCK_WATERLILY_COLOR_ALL = Sets.newHashSet();
    private static final Set<Block> BLOCK_DRY_COLOR_ALL = Sets.newHashSet();
    private static final Set<Item> ITEM_GRASS_COLOR_0 = Sets.newHashSet();
    private static final Set<Item> ITEM_INHERIT_BLOCK_COLOR_0 = Sets.newHashSet();

    public static final int LILY_PAD_BLOCK_COLOR = hexToInt("#208030");
    public static final int LILY_PAD_ITEM_COLOR = hexToInt("#71C35C");
    public static final int WATERLILY_COLOR = hexToInt("#76B625");
    public static final int DRY_FOLIAGE_COLOR = hexToInt("#a18962");
    public static final int PALE_DRY_FOLIAGE_COLOR = hexToInt("#A0A69C");
    public static final int PALE_FOLIAGE_COLOR = hexToInt("#8c927a");

    private static final PlantopiaColorResolver BIRCH_FOLIAGE_RESOLVER =  (biome, pos) -> biome.is(PlantopiaBiomeTags.IS_PALE) ? PALE_FOLIAGE_COLOR : FoliageColor.getBirchColor();
    private static final PlantopiaColorResolver DRY_FOLIAGE_RESOLVER =  (biome, pos) -> biome.is(PlantopiaBiomeTags.IS_PALE) ? PALE_DRY_FOLIAGE_COLOR : DRY_FOLIAGE_COLOR;

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

            PlantopiaBlockColors.add(Blocks.BIRCH_LEAVES, (state, level, pos, tintIndex) -> getBlockTint(level, pos, BIRCH_FOLIAGE_RESOLVER, FoliageColor::getBirchColor));
        });
    }

    public static void setupBlockColors() {
        runColorPhaseOnce(ColorPhase.BLOCK, () -> {
            PlantopiaBlockColors.add(BLOCK_DRY_COLOR_ALL, (state, level, pos, tintIndex) -> dryTint(state, level, pos, tintIndex, tintIndex));
            PlantopiaBlockColors.add(BLOCK_GRASS_COLOR_ALL, (state, level, pos, tintIndex) -> grassTint(state, level, pos, tintIndex, tintIndex));
            PlantopiaBlockColors.add(BLOCK_FOLIAGE_COLOR_ALL, (state, level, pos, tintIndex) -> foliageTint(state, level, pos, tintIndex, tintIndex));
            PlantopiaBlockColors.add(BLOCK_LILY_PAD_COLOR_ALL, (state, level, pos, tintIndex) -> lilyPadTint(state, level, pos, tintIndex, tintIndex));
            PlantopiaBlockColors.add(BLOCK_WATERLILY_COLOR_ALL, (state, level, pos, tintIndex) -> waterlilyTint(state, level, pos, tintIndex, tintIndex));
        });
    }

    public static void setupItemColors(BlockColors blockColors) {
        runColorPhaseOnce(ColorPhase.ITEM, () -> {
            PlantopiaItemColors.add(ITEM_GRASS_COLOR_0, (itemStuck, tintIndex) -> grassTint(tintIndex, 0));
            PlantopiaItemColors.add(ITEM_INHERIT_BLOCK_COLOR_0, (itemStack, tintIndex) -> inheritBlockTint(blockColors, (BlockItem) itemStack.getItem(), tintIndex, 0));
        });
    }

    /* GENERATED TINTS ******************************************/

    private static void generateAll() {
        PlantopiaMetaBuckets.BLOCK.forEach(blockMeta -> {
            if (!blockMeta.shouldApplyTint()) return;

            var block = blockMeta.get();
            var tintType = blockMeta.getTintType();

            if (blockMeta.shouldApplyTintToItem()) {
                ITEM_INHERIT_BLOCK_COLOR_0.add(block.asItem());
            }

            if (tintType == PlantopiaTintType.GRASS) {
                BLOCK_GRASS_COLOR_ALL.add(block);
            }

            if (tintType == PlantopiaTintType.LILY_PAD) {
                BLOCK_LILY_PAD_COLOR_ALL.add(block);
            }

            if (tintType == PlantopiaTintType.WATERLILY) {
                BLOCK_WATERLILY_COLOR_ALL.add(block);
            }

            if (tintType == PlantopiaTintType.FOLIAGE) {
                BLOCK_FOLIAGE_COLOR_ALL.add(block);
            }

            if (tintType == PlantopiaTintType.DRY) {
                BLOCK_DRY_COLOR_ALL.add(block);
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

    @SuppressWarnings("SameParameterValue")
    private static void pottedFernBlock(Block block) {
        BLOCK_GRASS_COLOR_ALL.add(block);
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

    private static int dryColor(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return DRY_FOLIAGE_COLOR;
    }

    /* TINTS ******************************************/

    private static int grassTint(int tintIndex, int targetTintIndex) {
        return grassTint(null, null, null, tintIndex, targetTintIndex);
    }

    private static int inheritBlockTint(BlockColors blockColors, BlockItem item, int tintIndex, int targetTintIndex) {
        if (tintIndex != targetTintIndex) return noColor();
        var state = item.getBlock().defaultBlockState();
        return blockColors.getColor(state, null, null, tintIndex);
    }

    private static int grassTint(@Nullable BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex, int targetTintIndex) {
        return tintIndex == targetTintIndex ? grassColor(level, getBaseBlockPos(state, pos)) : noColor();
    }

    private static int foliageTint(@Nullable BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex, int targetTintIndex) {
        return tintIndex == targetTintIndex ? foliageColor(level, getBaseBlockPos(state, pos)) : noColor();
    }

    private static int dryTint(@Nullable BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex, int targetTintIndex) {
        return tintIndex == targetTintIndex ? getBlockTint(level, getBaseBlockPos(state, pos), DRY_FOLIAGE_RESOLVER, () -> DRY_FOLIAGE_COLOR) : noColor();
    }

    private static int lilyPadTint(@Nullable BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex, int targetTintIndex) {
        return tintIndex == targetTintIndex ? lilyPadColor(level, getBaseBlockPos(state, pos)) : noColor();
    }

    private static int waterlilyTint(@Nullable BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex, int targetTintIndex) {
        return tintIndex == targetTintIndex ? waterlilyColor(level, getBaseBlockPos(state, pos)) : noColor();
    }

    /* HELPER METHODS ******************************************/

    private static int getBlockTint(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, PlantopiaColorResolver colorResolver, Supplier<Integer> fallbackColor) {
        if (level == null || pos == null) return fallbackColor.get();

        var biomeGetter = getBiomeGetter(level);
        if (biomeGetter == null) return fallbackColor.get();

        return calculateBlockTint(biomeGetter, pos, colorResolver);
    }

    @Nullable
    private static Function<BlockPos, Holder<Biome>> getBiomeGetter(BlockAndTintGetter level) {
        if (level instanceof LevelReader levelReader) return levelReader::getBiome;
        if (level instanceof PlantopiaBiomeGetterExtension extension) return extension::plantopia$getBiome;
        return null;
    }

    private static int calculateBlockTint(Function<BlockPos, Holder<Biome>> biomeGetter, BlockPos pos, PlantopiaColorResolver colorResolver) {
        int biomeBlendRadius = Minecraft.getInstance().options.biomeBlendRadius().get();
        if (biomeBlendRadius == 0) {
            return colorResolver.getColor(biomeGetter.apply(pos), pos);
        }

        int j = (biomeBlendRadius * 2 + 1) * (biomeBlendRadius * 2 + 1);
        int k = 0;
        int l = 0;
        int m = 0;
        Cursor3D cursor3D = new Cursor3D(pos.getX() - biomeBlendRadius, pos.getY(), pos.getZ() - biomeBlendRadius, pos.getX() + biomeBlendRadius, pos.getY(), pos.getZ() + biomeBlendRadius);

        int n;
        for(BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(); cursor3D.advance(); m += n & 255) {
            mutableBlockPos.set(cursor3D.nextX(), cursor3D.nextY(), cursor3D.nextZ());
            n = colorResolver.getColor(biomeGetter.apply(mutableBlockPos), mutableBlockPos);
            k += (n & 16711680) >> 16;
            l += (n & '\uff00') >> 8;
        }

        return (k / j & 255) << 16 | (l / j & 255) << 8 | m / j & 255;
    }

    @Nullable
    private static BlockPos getBaseBlockPos(@Nullable BlockState state, @Nullable BlockPos pos) {
        if (state == null || pos == null) return null;
        return PlantopiaBlockHelper.getBaseBlockPos(state, pos);
    }

    public enum ColorPhase {
        COMMON, BLOCK, ITEM
    }
}