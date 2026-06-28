package by.langvest.plantopia.worldgen.feature.blockplacer;

import by.langvest.plantopia.util.PlantopiaIntegerPropertyHolder;
import by.langvest.plantopia.worldgen.feature.PlantopiaBlockPlacerType;
import by.langvest.plantopia.worldgen.feature.PlantopiaBlockPlacerTypes;
import by.langvest.plantopia.worldgen.feature.special.PlantopiaNaturalBlockFeature;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ParametersAreNonnullByDefault
public class PlantopiaGradientBlockPlacer extends PlantopiaBlockPlacer {
    public static final Codec<PlantopiaGradientBlockPlacer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BlockStateProvider.CODEC.fieldOf("provider").forGetter(it -> it.provider),
        PlantopiaIntegerPropertyHolder.CODEC.fieldOf("property").forGetter(it -> it.property),
        Codec.DOUBLE.fieldOf("erosion").forGetter(it -> it.erosion),
        ExtraCodecs.POSITIVE_INT.fieldOf("weight").forGetter(it -> it.weight)
    ).apply(instance, PlantopiaGradientBlockPlacer::new));

    private static final Map<Block, IntegerProperty> PROPERTY_CACHE = new ConcurrentHashMap<>();

    protected final BlockStateProvider provider;
    protected final PlantopiaIntegerPropertyHolder property;
    protected final double erosion;
    protected final int weight;

    public PlantopiaGradientBlockPlacer(BlockStateProvider provider, PlantopiaIntegerPropertyHolder property, double erosion, int weight) {
        this.provider = provider;
        this.property = property;
        this.erosion = erosion;
        this.weight = weight;
    }

    @Override
    protected PlantopiaBlockPlacerType<?> type() {
        return PlantopiaBlockPlacerTypes.GRADIENT.get();
    }

    @Override
    public int weight() {
        return weight;
    }

    @Override
    public boolean place(Context context) {
        var random = context.random();
        var targetPos = context.targetPos();
        var level = context.level();
        var baseState = provider.getState(random, targetPos);
        var intProperty = getIntegerProperty(baseState, property.name());

        if (intProperty == null) {
            return false;
        }

        int minValue = property.min();
        int maxValue = property.max();
        int range = maxValue - minValue;
        double densityIndex = getDensityIndex(context);
        double preciseValue = minValue + (range * densityIndex);

        if (erosion > 0 && range > 0) {
            double noise = (random.nextDouble() * 2.0 - 1.0) * erosion * range;
            preciseValue += noise;
        }

        var currentState = level.getBlockState(targetPos);

        if (currentState.is(baseState.getBlock())) {
            int existingValue = currentState.getValue(intProperty);
            preciseValue = Math.max(existingValue, preciseValue);
        }

        int finalValue = (int) Mth.clamp(Math.round(preciseValue), minValue, maxValue);
        var newState = baseState.setValue(intProperty, finalValue);

        return PlantopiaNaturalBlockFeature.place(level, newState, targetPos, random, Block.UPDATE_CLIENTS);
    }

    private double getDensityIndex(Context context) {
        var targetPos = context.targetPos();
        var centerPos = context.centerPos();
        var xzSpread = context.xzSpread();

        double dx = targetPos.getX() - centerPos.getX();
        double dz = targetPos.getZ() - centerPos.getZ();
        double distance = Math.sqrt(dx * dx + dz * dz);

        double densityIndex = 1.0 - (distance / xzSpread);

        return Mth.clamp(densityIndex, 0.0, 1.0);
    }

    @Nullable
    private IntegerProperty getIntegerProperty(BlockState state, String name) {
        var block = state.getBlock();

        if (PROPERTY_CACHE.containsKey(block)) {
            return PROPERTY_CACHE.get(block);
        }

        Property<?> property = block.getStateDefinition().getProperty(name);

        if (property instanceof IntegerProperty intProperty) {
            PROPERTY_CACHE.put(block, intProperty);
            return intProperty;
        }

        return null;
    }
}
