package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.worldgen.feature.config.PlantopiaFirTreeConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * Фича для генерации большой ели (пихты) с кастомной логикой.
 * <p>
 * В отличие от ванильных деревьев, эта фича использует собственный, более сложный алгоритм
 * для создания реалистичной и детализированной формы дерева.
 * <p>
 * Ключевые особенности:
 * <ul>
 *     <li>Ствол, плавно сужающийся кверху.</li>
 *     <li>Пышная крона, состоящая из простых слоев листвы наверху и полноценных веток внизу.</li>
 *     <li>Гибкая настройка через {@link PlantopiaFirTreeConfiguration}.</li>
 * </ul>
 */
@ParametersAreNonnullByDefault
public class PlantopiaFirTreeFeature extends PlantopiaAbstractTreeFeature<PlantopiaFirTreeConfiguration> {
    public PlantopiaFirTreeFeature(Codec<PlantopiaFirTreeConfiguration> codec) {
        super(codec);
    }

    /**
     * Определяет пайплайн (последовательность) операций для генерации дерева.
     * Этот метод является точкой входа для всей фичи.
     *
     * @return Список модификаторов, которые будут применены последовательно для создания дерева.
     */
    @Override
    protected List<TreeModifier> getTreePipeline(FeaturePlaceContext<PlantopiaFirTreeConfiguration> context) {
        var config = context.config();

        return List.of(
            // 1. Создание основной структуры: ствол и листва. Это "скелет" дерева.
            makeFirStructure(config),
            // 2. Применение декораторов: снег, грибы, лианы и т.д.
            applyDecorators(config.decorators()),
            // 3. Обновление состояния блоков листвы, чтобы они не опадали.
            updateLeaves()
        );
    }

    /**
     * Создает основной {@link TreeModifier}, который отвечает за генерацию ствола и листвы ели.
     * Этот метод подготавливает все данные и запускает основную логику генерации.
     *
     * @param config Конфигурация дерева, содержащая все необходимые параметры.
     * @return {@link TreeModifier}, который при вызове сгенерирует структуру дерева.
     */
    @Contract(pure = true)
    protected static @NotNull TreeModifier makeFirStructure(PlantopiaFirTreeConfiguration config) {
        // Возвращаем лямбда-функцию, которая и будет выполнять всю работу по генерации.
        // Это позволяет отделить определение логики от ее выполнения.
        return (context, pool, setter) -> {
            var level = context.level();
            var random = context.random();
            var origin = context.origin();

            // --- 1. Определение размеров дерева ---
            // Получаем случайные значения высоты и ширины из диапазонов, заданных в конфигурации.
            int trunkHeight = config.trunkHeight().sample(random); // вся высота дерева целиком.
            int foliageHeight = config.foliageHeight().sample(random, trunkHeight); // Высота кроны дерева.
            int foliageOffset = config.foliageOffset().sample(random); // Отступ кроны от точки крепления к стволу.
            int maxTrunkWidth = config.trunkWidth().sample(random); // Определяем максимальную ширину ствола у основания.
            int totalTreeHeight = trunkHeight + 1 + Math.max(foliageOffset, -1); // Здесь 1 блок зарезервирован пот точку крепления листвы при foliageOffset 0 и более.
            int baseTrunkHeight = totalTreeHeight - foliageHeight;
            var trunkWidthProvider = createTreeTrunkWidthProvider(totalTreeHeight, maxTrunkWidth);

            // --- 2. Проверка свободного места ---
            // Перед тем как ставить блоки, убедимся, что для дерева достаточно места.
            // Это важная оптимизация, чтобы не генерировать "обрезанные" деревья.
            if (!checkSpace(level, origin, totalTreeHeight, baseTrunkHeight, trunkWidthProvider)) {
                return false; // Если места нет, генерация отменяется.
            }

            // --- 3. Генерация ---
            // Если все проверки пройдены, запускаем генерацию ствола и листвы.
            var foliageAttachment = placeTrunk(random, origin, trunkHeight, trunkWidthProvider, setter, config);
            placeFoliage(random, foliageAttachment, foliageHeight, foliageOffset, totalTreeHeight, trunkWidthProvider, setter, config);
            return true; // Генерация успешно завершена.
        };
    }

    protected static int calculateTrunkWidth(int deltaHeight, int totalHeight, int maxTrunkWidth) {
        int width = (maxTrunkWidth * (totalHeight - deltaHeight) / totalHeight) + 1;
        return Mth.clamp(width, 1, maxTrunkWidth);
    }

    protected record TreeTrunkWidthInfo(int width, int begin, int end) {
        @Contract("_ -> new")
        protected static @NotNull TreeTrunkWidthInfo of(int width) {
            // TODO добавить глобальную мемоизацию чтобы не создавать много объектов.
            return new TreeTrunkWidthInfo(width, Mth.ceil(0.25D - width / 2.0D), Mth.floor(0.25D + width / 2.0D));
        }
    }

    protected interface TreeTrunkWidthProvider {
        TreeTrunkWidthInfo provide(int height);
    }

    @Contract(pure = true)
    protected static @NotNull TreeTrunkWidthProvider createTreeTrunkWidthProvider(int totalTreeHeight, int maxTrunkWidth) {
        return height -> TreeTrunkWidthInfo.of(calculateTrunkWidth(height, totalTreeHeight, maxTrunkWidth));
    }

    /**
     * Проверяет, достаточно ли свободного места для размещения всего дерева (ствола и кроны).
     * Проверка идет по всем слоям (от основания до верхушки), вычисляя необходимый
     * радиус на каждой высоте.
     */
    protected static boolean checkSpace(LevelAccessor level, BlockPos trunkOrigin, int totalTreeHeight, int baseTrunkHeight, TreeTrunkWidthProvider trunkWidthProvider) {
        // Проверка, не выходит ли дерево за пределы высоты мира.
        if (trunkOrigin.getY() + totalTreeHeight >= level.getMaxBuildHeight()) {
            return false;
        }

        var mutablePos = new BlockPos.MutableBlockPos();

        // Итерация по высоте дерева, от корней до верхушки (dy - смещение по Y).
        for (int dy = 0; dy <= totalTreeHeight; dy++) {
            var trunkInfo = trunkWidthProvider.provide(dy);
            int scanBegin = (dy <= baseTrunkHeight ? trunkInfo.begin : trunkInfo.begin - 1);
            int sacnEnd = (dy <= baseTrunkHeight ? trunkInfo.end : trunkInfo.end + 1);

            // Проверка области вокруг ствола на текущем слое.
            for (int dx = scanBegin; dx <= sacnEnd; dx++) {
                for (int dz = scanBegin; dz <= sacnEnd; dz++) {
                    mutablePos.setWithOffset(trunkOrigin, dx, dy, dz);

                    // Используем ванильный метод для проверки, можно ли в этой позиции разместить часть дерева.
                    // Он проверяет, что блок можно заменить (не бедрок, не спаунер и т.д.).
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
    @Contract("_, _, _, _, _, _ -> new")
    protected static FoliagePlacer.@NotNull FoliageAttachment placeTrunk(
        RandomSource random,
        BlockPos origin,
        int trunkHeight,
        TreeTrunkWidthProvider trunkWidthProvider,
        TreeBlockSetter setter,
        PlantopiaFirTreeConfiguration config
    ) {
        var mutablePos = new BlockPos.MutableBlockPos();

        // Итерация по высоте дерева для создания ствола.
        for (int dy = 0; dy < trunkHeight; dy++) {
            var trunkInfo = trunkWidthProvider.provide(dy);

            for (int dx = trunkInfo.begin; dx <= trunkInfo.end; dx++) {
                for (int dz = trunkInfo.begin; dz <= trunkInfo.end; dz++) {
                    // На самом нижнем слое (dy == 0) заменяем землю под стволом на блок "корней" (обычно это земля или podzol).
                    if (dy == 0) {
                        mutablePos.setWithOffset(origin, dx, -1, dz);
                        setter.root().accept(mutablePos, Blocks.DIRT.defaultBlockState());
                    }

                    // Устанавливаем блок ствола.
                    mutablePos.setWithOffset(origin, dx, dy, dz);
                    setter.trunk().accept(mutablePos, config.trunkProvider().getState(random, mutablePos));
                }
            }
        }

        return new FoliagePlacer.FoliageAttachment(mutablePos.set(origin.getX(), mutablePos.getY() + 1, origin.getZ()), 0, false);
    }

    /**
     * Генерирует листву дерева, начиная с верхушки и спускаясь вниз.
     * Такой подход (сверху-вниз) упрощает логику формирования кроны.
     */
    protected static void placeFoliage(
        RandomSource random,
        FoliagePlacer.FoliageAttachment attachment,
        int foliageHeight,
        int foliageOffset,
        int totalTreeHeight,
        TreeTrunkWidthProvider trunkWidthProvider,
        TreeBlockSetter setter,
        PlantopiaFirTreeConfiguration config
    ) {
        var foliageApex = attachment.pos().offset(0, foliageOffset, 0);
        var mutablePos = new BlockPos.MutableBlockPos();

        for (int row = 0; row < foliageHeight; row++) {
            mutablePos.setWithOffset(foliageApex, 0, -row, 0);
            var trunkInfo = trunkWidthProvider.provide(totalTreeHeight - row);
            int radius = Math.min(Math.min((row + 2) / 3, 3 + (foliageHeight - row)), 6);
            placeFoliageRow(random, mutablePos, row, radius, trunkInfo, setter, config);
        }
    }

    /**
     * Генерирует один слой листвы или веток на заданной высоте.
     * Этот метод решает, какой тип кроны использовать: простой слой листвы или полноценные ветки.
     *
     * @param row Индекс слоя от верхушки (0 - самый верхний).
     * @param radius            Вычисленный радиус кроны для этого слоя.
     */
    protected static void placeFoliageRow(
        RandomSource random,
        BlockPos center,
        int row,
        int radius,
        TreeTrunkWidthInfo trunkInfo,
        TreeBlockSetter setter,
        PlantopiaFirTreeConfiguration config
    ) {
        // Если радиус 0, это самая верхушка дерева. Ставим один блок листвы.
        if (radius == 0) {
            setter.foliage().setSafely(center, config.foliageProvider().getState(random, center));
            return;
        }

        // Для небольшого радиуса (верхняя часть кроны) генерируем простые слои листвы.
        if (radius <= 3) {
            // Четные слои (от верха) имеют полный радиус, нечетные - половину.
            // Это создает "рваную", более естественную форму кроны.
            int patternRadius = (row % 2 == 0) ? radius : radius / 2;
            placeLeafRow(random, center, patternRadius, trunkInfo, setter, config);
            return;
        }

        // Для большого радиуса (нижняя, широкая часть кроны) генерируем полноценные ветки.
        // Ветки генерируются только на четных слоях, чтобы они не слипались и выглядели лучше.
        if (row % 2 == 0) {
            // Генерируем 4 ветки, расходящиеся в разные стороны от ствола.
            placeBranchRow(random, center.offset(trunkInfo.begin, 0, trunkInfo.begin), radius, Direction.NORTH, setter, config);
            placeBranchRow(random, center.offset(trunkInfo.end, 0, trunkInfo.begin), radius, Direction.EAST, setter, config);
            placeBranchRow(random, center.offset(trunkInfo.end, 0, trunkInfo.end), radius, Direction.SOUTH, setter, config);
            placeBranchRow(random, center.offset(trunkInfo.begin, 0, trunkInfo.end), radius, Direction.WEST, setter, config);
        }
    }

    /**
     * Генерирует плоский слой листвы в форме ромба ("алмаза").
     * Используется для верхней, более простой части кроны.
     */
    protected static void placeLeafRow(
        RandomSource random,
        BlockPos center,
        int range,
        TreeTrunkWidthInfo trunkInfo,
        TreeBlockSetter setter,
        PlantopiaFirTreeConfiguration config
    ) {
        var mutablePos = new BlockPos.MutableBlockPos();

        int begin = trunkInfo.begin - range;
        int end = trunkInfo.end + range;

        for (int dx = begin; dx <= end; dx++) {
            for (int dz = begin; dz <= end; dz++) {

                // Пропускаем углы квадрата, чтобы создать более округлую/ромбовидную форму.
                if ((range > 0) && (dx == begin || dx == end) && (dz == begin || dz == end)) {
                    continue;
                }

                // Вычисляем Манхэттенское расстояние от "центра" ствола.
                // Это нужно, чтобы листва не висела в воздухе слишком далеко от ствола.
                int distFromTrunk =
                    (dx < 0 ? trunkInfo.begin - dx : dx - trunkInfo.end)
                        + (dz < 0 ? trunkInfo.begin - dz : dz - trunkInfo.end);

                // Ставим листву, если она не слишком далеко (dist < 4).
                // На границе (dist == 4) ставим с шансом 50%, чтобы сделать край более "шумным".
                if (distFromTrunk < 4 || (distFromTrunk == 4 && random.nextInt(2) == 0)) {
                    mutablePos.set(center.getX() + dx, center.getY(), center.getZ() + dz);
                    setter.foliage().setSafely(mutablePos, config.foliageProvider().getState(random, mutablePos));
                }
            }
        }
    }

    protected static void placeBranchRow(
        RandomSource random,
        BlockPos branchOrigin,
        int distance,
        Direction direction,
        TreeBlockSetter setter,
        PlantopiaFirTreeConfiguration config
    ) {
        var mutablePos = new BlockPos.MutableBlockPos();
        // Направление, перпендикулярное росту ветки (для размещения листвы по бокам).
        var sideways = direction.getCounterClockWise();

        // Идем от ствола наружу, создавая сегменты ветки.
        for (int i = 1; i <= distance; i++) {
            var branchPos = branchOrigin.relative(direction, i);

            // Если это не последние два сегмента ветки, создаем более пышную структуру у основания.
            if (distance - i > 2) {
                // Добавляем листву над веткой, чтобы сделать ее объемнее.
                setter.foliage().setSafely(branchPos.above(), config.foliageProvider().getState(random, mutablePos));
                setter.foliage().setSafely(branchPos.above().relative(sideways, 1), config.foliageProvider().getState(random, branchPos.above().relative(sideways, 1)));
                setter.foliage().setSafely(branchPos.above().relative(sideways, -1), config.foliageProvider().getState(random, branchPos.above().relative(sideways, -1)));

                // Получаем состояние блока древесины.
                var logState = config.trunkProvider().getState(random, branchPos);

                // Если у блока есть свойство оси (как у бревен), устанавливаем его
                // в соответствии с направлением роста ветки.
                if (logState.hasProperty(BlockStateProperties.AXIS)) {
                    logState = logState.setValue(BlockStateProperties.AXIS, direction.getAxis());
                }

                // Устанавливаем блок ветки (древесина) с правильной ориентацией.
                setter.trunk().setSafely(branchPos, logState);
            }

            // Радиус листвы вокруг сегмента ветки. У начала и конца ветки он меньше (1), в середине - больше (2).
            int leafRange = (i == 1 || i == distance) ? 1 : 2;
            // Генерируем листву вокруг текущего сегмента ветки.
            for (int j = -leafRange; j <= leafRange; j++) {
                // На конце ветки (i == distance) листва генерируется с шансом 50%.
                if (i < distance || random.nextInt(2) == 0) {
                    mutablePos.set(branchPos).move(sideways, j);
                    setter.foliage().setSafely(mutablePos, config.foliageProvider().getState(random, mutablePos));
                }
            }
        }
    }
}
