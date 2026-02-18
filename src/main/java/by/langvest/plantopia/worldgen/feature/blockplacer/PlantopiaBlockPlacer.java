package by.langvest.plantopia.worldgen.feature.blockplacer;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.worldgen.feature.PlantopiaBlockPlacerType;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public abstract class PlantopiaBlockPlacer {
    public static final Codec<PlantopiaBlockPlacer> CODEC = PlantopiaRegistries.BLOCK_PLACER_TYPE.byNameCodec().dispatch(PlantopiaBlockPlacer::type, PlantopiaBlockPlacerType::codec);

    protected abstract PlantopiaBlockPlacerType<?> type();

    public abstract int weight();

    public abstract boolean place(Context context);

    public record Context(
        WorldGenLevel level,
        BlockPos targetPos,
        BlockPos centerPos,
        RandomSource random,
        double xzSpread,
        double ySpread
    ) {}
}
