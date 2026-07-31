package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.worldgen.feature.config.PlantopiaFirTreeConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;

/**
 * Фича для генерации большой ели (пихты) с кастомной логикой.
 * В отличие от ванильных деревьев, эта фича использует собственный алгоритм
 * для создания более реалистичной и детализированной формы дерева.
 */
@ParametersAreNonnullByDefault
public class PlantopiaFirTreeFeature extends PlantopiaAbstractTreeFeature<PlantopiaFirTreeConfiguration> {
    public PlantopiaFirTreeFeature(Codec<PlantopiaFirTreeConfiguration> codec) {
        super(codec);
    }

    /**
     * Определяет пайплайн (последовательность) операций для генерации дерева.
     */
    @Override
    protected List<TreeModifier> getTreePipeline(FeaturePlaceContext<PlantopiaFirTreeConfiguration> context) {
        var config = context.config();

        return List.of(
            makeFirStructure(config),      // 1. Создание основной структуры (ствол и листва)
            applyDecorators(config.decorators()), // 2. Применение декораторов (например, грибы, лианы)
            updateLeaves()                 // 3. Обновление состояния блоков листвы (для правильного опадания)
        );
    }

    /**
     * Создает основной {@link TreeModifier}, который отвечает за генерацию ствола и листвы ели.
     */
    @Contract(pure = true)
    protected static @NotNull TreeModifier makeFirStructure(PlantopiaFirTreeConfiguration config) {
        return (context, pool, setter) -> {
            var level = context.level();
            var random = context.random();
            var origin = context.origin();

            // Определяем высоту дерева и ствола на основе конфигурации
            int treeHeight = config.treeHeight().sample(random);
            int trunkHeight = config.trunkHeight().sample(random, treeHeight);
            int leavesHeight = treeHeight - trunkHeight;
            // Листве нужно минимальное пространство для роста
            if (leavesHeight < 3) {
                return false;
            }

            // Определяем максимальную ширину ствола у основания
            int maxTrunkWidth = config.trunkWidth().sample(random);
            // Проверяем, достаточно ли места для генерации всего дерева
            if (!checkSpace(level, origin, trunkHeight, treeHeight, maxTrunkWidth)) {
                return false;
            }

            // Генерируем ствол и листву
            generateTrunk(random, origin, treeHeight, maxTrunkWidth, setter, config);
            generateFoliage(level, random, origin, trunkHeight, treeHeight, maxTrunkWidth, setter, config);
            return true;
        };
    }

    /**
     * Вычисляет ширину ствола на определенной высоте.
     * @param currentHeight Высота от основания дерева (0 - самый низ).
     * @param treeHeight    Общая высота дерева.
     * @param maxTrunkWidth Максимальная ширина ствола у основания.
     * @return Ширина ствола на заданной высоте.
     */
    private static int getTrunkWidthAt(int currentHeight, int treeHeight, int maxTrunkWidth) {
        // Используем оригинальную формулу, которая плавно уменьшает ширину с высотой.
        int width = (maxTrunkWidth * (treeHeight - currentHeight) / treeHeight) + 1;
        // Ограничиваем результат максимальной шириной, чтобы избежать слишком толстого основания из-за округления.
        return Math.min(maxTrunkWidth, width);
    }

    /**
     * Проверяет, достаточно ли свободного места для размещения дерева.
     * Проверка идет по всем слоям (от основания до верхушки), вычисляя необходимый
     * радиус на каждой высоте.
     */
    protected static boolean checkSpace(LevelAccessor level, BlockPos origin, int trunkHeight, int treeHeight, int maxTrunkWidth) {
        // Проверка, не выходит ли дерево за пределы высоты мира
        if (origin.getY() + treeHeight >= level.getMaxBuildHeight()) {
            return false;
        }

        var mutablePos = new BlockPos.MutableBlockPos();

        // Итерация по высоте дерева, от корней до верхушки
        for (int dy = 0; dy <= treeHeight; dy++) {
            int trunkWidth = getTrunkWidthAt(dy, treeHeight, maxTrunkWidth);
            int trunkStart = Mth.ceil(0.25D - trunkWidth / 2.0D);
            int trunkEnd   = Mth.floor(0.25D + trunkWidth / 2.0D);

            // Радиус проверки для листвы больше, чем для ствола
            int start = (dy <= trunkHeight ? trunkStart : trunkStart - 1);
            int end   = (dy <= trunkHeight ? trunkEnd   : trunkEnd + 1);

            // Проверка области вокруг ствола на текущем слое
            for (int dx = start; dx <= end; dx++) {
                for (int dz = start; dz <= end; dz++) {
                    mutablePos.setWithOffset(origin, dx, dy, dz);

                    // Используем ванильный метод для проверки, можно ли в этой позиции разместить часть дерева
                    if (!TreeFeature.validTreePos(level, mutablePos)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Генерирует ствол дерева, который сужается кверху.
     */
    protected static void generateTrunk(
        RandomSource random,
        BlockPos origin,
        int treeHeight,
        int maxTrunkWidth,
        TreeBlockSetter setter,
        PlantopiaFirTreeConfiguration config
    ) {
        var mutablePos = new BlockPos.MutableBlockPos();

        // Итерация по высоте дерева для создания ствола.
        // Заканчиваем на 1 блок раньше (treeHeight - 1), чтобы освободить место для верхушки из листвы.
        for (int dy = 0; dy < treeHeight - 1; dy++) {
            int trunkWidth = getTrunkWidthAt(dy, treeHeight, maxTrunkWidth);
            int trunkStart = Mth.ceil(0.25D - trunkWidth / 2.0D);
            int trunkEnd   = Mth.floor(0.25D + trunkWidth / 2.0D);

            for (int dx = trunkStart; dx <= trunkEnd; dx++) {
                for (int dz = trunkStart; dz <= trunkEnd; dz++) {
                    // На самом нижнем слое (dy == 0) заменяем землю под стволом на блок корней (землю)
                    if (dy == 0) {
                        mutablePos.setWithOffset(origin, dx, -1, dz);
                        setter.root().accept(mutablePos, Blocks.DIRT.defaultBlockState());
                    }

                    // Устанавливаем блок ствола
                    mutablePos.setWithOffset(origin, dx, dy, dz);
                    setter.trunk().accept(mutablePos, config.trunkProvider().getState(random, mutablePos));
                }
            }
        }
    }

    /**
     * Генерирует листву дерева, начиная с верхушки и спускаясь вниз.
     */
    protected static void generateFoliage(
        LevelAccessor level,
        RandomSource random,
        BlockPos origin,
        int trunkHeight,
        int treeHeight,
        int maxTrunkWidth,
        TreeBlockSetter setter,
        PlantopiaFirTreeConfiguration config
    ) {
        int leavesHeight = treeHeight - trunkHeight;

        // Начинаем с верхушки дерева (самый высокий возможный блок)
        BlockPos foliageTopPos = origin.atY(origin.getY() + treeHeight);

        // Итерируемся по слоям листвы сверху вниз (от 0 до leavesHeight-1)
        for (int layerIndexFromTop = 0; layerIndexFromTop < leavesHeight; layerIndexFromTop++) {
            // Позиция текущего слоя листвы
            BlockPos layerPos = foliageTopPos.below(layerIndexFromTop);
            int currentHeight = treeHeight - layerIndexFromTop;

            int trunkWidth = getTrunkWidthAt(currentHeight, treeHeight, maxTrunkWidth);
            int trunkStart = Mth.ceil(0.25D - trunkWidth / 2.0D);
            int trunkEnd   = Mth.floor(0.25D + trunkWidth / 2.0D);

            // Радиус листвы зависит от высоты слоя. Формула взята из референса.
            // Он увеличивается к середине кроны и уменьшается к верхушке и основанию.
            int radius = Math.min(
                Math.min((layerIndexFromTop + 2) / 3, 3 + (leavesHeight - layerIndexFromTop)),
                6
            );

            generateLayer(level, random, layerPos, layerIndexFromTop, trunkStart, trunkEnd, radius, setter, config);
        }
    }

    /**
     * Генерирует один слой листвы или веток на заданной высоте.
     */
    protected static void generateLayer(
        LevelAccessor level,
        RandomSource random,
        BlockPos layerPos,
        int layerIndexFromTop,
        int trunkStart,
        int trunkEnd,
        int radius,
        TreeBlockSetter setter,
        PlantopiaFirTreeConfiguration config
    ) {
        // Если радиус 0, ставим один блок листвы (самая верхушка дерева)
        if (radius == 0) {
            tryPlaceLeaf(level, setter.foliage(), layerPos, config.foliageProvider().getState(random, layerPos));
            return;
        }

        // Для небольшого радиуса генерируем простые слои листвы
        if (radius < 4) {
            // Четные слои имеют полный радиус, нечетные - половину, для создания "рваной" формы
            int effectiveRadius = (layerIndexFromTop % 2 == 0) ? radius : radius / 2;
            generateLeafLayer(level, random, layerPos, effectiveRadius, trunkStart, trunkEnd, setter.foliage(), config);
            return;
        }

        // Для большого радиуса генерируем полноценные ветки, расходящиеся в 4 стороны
        // Ветки генерируются только на четных слоях для более естественного вида
        if (layerIndexFromTop % 2 == 0) {
            generateBranch(level, random, layerPos.offset(trunkStart, 0, trunkStart),
                Direction.NORTH, radius, setter.trunk(), setter.foliage(), config);

            generateBranch(level, random, layerPos.offset(trunkEnd, 0, trunkStart),
                Direction.EAST, radius, setter.trunk(), setter.foliage(), config);

            generateBranch(level, random, layerPos.offset(trunkEnd, 0, trunkEnd),
                Direction.SOUTH, radius, setter.trunk(), setter.foliage(), config);

            generateBranch(level, random, layerPos.offset(trunkStart, 0, trunkEnd),
                Direction.WEST, radius, setter.trunk(), setter.foliage(), config);
        }
    }

    /**
     * Генерирует плоский слой листвы в форме ромба (или "алмаза").
     */
    protected static void generateLeafLayer(
        LevelAccessor level,
        RandomSource random,
        BlockPos center,
        int radius,
        int trunkStart,
        int trunkEnd,
        FoliagePlacer.FoliageSetter leaves,
        PlantopiaFirTreeConfiguration config
    ) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        int start = trunkStart - radius;
        int end   = trunkEnd   + radius;

        for (int dx = start; dx <= end; dx++) {
            for (int dz = start; dz <= end; dz++) {

                // Пропускаем углы, чтобы создать более округлую/ромбовидную форму
                if ((radius > 0) && (dx == start || dx == end) && (dz == start || dz == end)) {
                    continue;
                }

                // Вычисляем расстояние от "центра" ствола
                int distFromTrunk =
                    (dx < 0 ? trunkStart - dx : dx - trunkEnd)
                        + (dz < 0 ? trunkStart - dz : dz - trunkEnd);

                // Ставим листву, если она не слишком далеко от ствола (для выживания)
                // На границе (distFromTrunk == 4) ставим с шансом 50%
                if (distFromTrunk < 4 || (distFromTrunk == 4 && random.nextInt(2) == 0)) {
                    pos.set(center.getX() + dx, center.getY(), center.getZ() + dz);
                    tryPlaceLeaf(level, leaves, pos, config.foliageProvider().getState(random, pos));
                }
            }
        }
    }

    /**
     * Генерирует одну большую ветку, состоящую из древесины и листвы.
     */
    protected static void generateBranch(
        LevelAccessor level,
        RandomSource random,
        BlockPos start,
        Direction direction,
        int length,
        java.util.function.BiConsumer<BlockPos, BlockState> logs,
        FoliagePlacer.FoliageSetter leaves,
        PlantopiaFirTreeConfiguration config
    ) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        // Направление, перпендикулярное росту ветки
        Direction sideways = direction.getCounterClockWise();

        // Идем от ствола наружу, создавая сегменты ветки
        for (int i = 1; i <= length; i++) {
            BlockPos branchPos = start.relative(direction, i);
            // Радиус листвы вокруг сегмента ветки. У начала и конца ветки он меньше.
            int r = (i == 1 || i == length) ? 1 : 2;

            // Генерируем листву вокруг текущего сегмента ветки
            for (int j = -r; j <= r; j++) {
                // На конце ветки листва генерируется с шансом 50%
                if (i < length || random.nextInt(2) == 0) {
                    pos.set(branchPos).move(sideways, j);
                    tryPlaceLeaf(level, leaves, pos, config.foliageProvider().getState(random, pos));
                }
            }

            // Если это не последние два сегмента ветки, создаем более пышную структуру
            if (length - i > 2) {
                // Листва над веткой
                tryPlaceLeaf(level, leaves, branchPos.above(), config.foliageProvider().getState(random, branchPos.above()));
                tryPlaceLeaf(level, leaves, branchPos.above().relative(sideways, 1), config.foliageProvider().getState(random, branchPos.above().relative(sideways, 1)));
                tryPlaceLeaf(level, leaves, branchPos.above().relative(sideways, -1), config.foliageProvider().getState(random, branchPos.above().relative(sideways, -1)));

                // Получаем состояние блока древесины
                BlockState logState = config.trunkProvider().getState(random, branchPos);

                if (logState.hasProperty(BlockStateProperties.AXIS)) {
                    // Пытаемся установить ось блока в соответствии с направлением роста ветки
                    logState = logState.setValue(BlockStateProperties.AXIS, direction.getAxis());
                }

                // Устанавливаем блок ветки (древесина) с правильной ориентацией
                logs.accept(branchPos, logState);
            }
        }
    }

    /**
     * Безопасно устанавливает блок листвы, проверяя, можно ли его разместить в данной позиции.
     * Также копирует состояние водяного лога, если это необходимо.
     * @return true, если блок был успешно установлен.
     */
    @SuppressWarnings("UnusedReturnValue")
    protected static boolean tryPlaceLeaf(
        LevelAccessor level,
        FoliagePlacer.FoliageSetter setter,
        BlockPos pos,
        BlockState state
    ) {
        if (!TreeFeature.validTreePos(level, pos)) return false;
        setter.set(pos, copyWaterloggedFrom(level, pos, state));
        return true;
    }
}
