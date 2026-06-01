package by.langvest.plantopia.worldgen.feature.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaFrazilConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaFrazilFeature extends Feature<PlantopiaFrazilConfiguration> {
    public PlantopiaFrazilFeature(Codec<PlantopiaFrazilConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PlantopiaFrazilConfiguration> context) {
        var level = context.level();
        var originPos = context.origin();
        var random = context.random();
        var config = context.config();
        var predicate = config.predicate();
        var noiseConfig = config.noiseConfig();
        var activationType = config.activationType();
        var allowedBiomes = config.allowedBiomes();
        float noiseThreshold = config.noiseLevel();

        var mutablePos = new BlockPos.MutableBlockPos();
        int seaLevel = level.getSeaLevel();

        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                int x = originPos.getX() + dx;
                int z = originPos.getZ() + dz;
                mutablePos.set(x, seaLevel, z);

                double noiseLevel = noiseConfig.getValue(x, z);

                boolean shouldPlace;
                if (activationType.isAbove()) {
                    shouldPlace = noiseLevel > noiseThreshold;
                } else {
                    shouldPlace = noiseLevel < noiseThreshold;
                }

                if (!shouldPlace) {
                    continue;
                }

                if (!isBiomeAllowed(allowedBiomes, level.getBiome(mutablePos))) {
                    continue;
                }

                if (predicate.test(level, mutablePos)) {
                    var state = getIceCrustState();
                    PlantopiaNaturalBlockFeature.place(level, state, mutablePos, random, Block.UPDATE_CLIENTS);
                }
            }
        }

        return true;
    }

    private boolean isBiomeAllowed(HolderSet<Biome> allowedBiomes, Holder<Biome> biomeHolder) {
        return allowedBiomes.contains(biomeHolder);
    }

    private @NotNull BlockState getIceCrustState() {
        return PlantopiaBlocks.ICE_CRUST.get().defaultBlockState()
            .setValue(BlockStateProperties.DOWN, true);
    }
}
