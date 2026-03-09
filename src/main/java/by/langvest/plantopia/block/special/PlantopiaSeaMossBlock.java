package by.langvest.plantopia.block.special;

import by.langvest.plantopia.worldgen.feature.PlantopiaVegetationFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PlantopiaSeaMossBlock extends Block implements BonemealableBlock {
    public PlantopiaSeaMossBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean isClient) {
        return level.getBlockState(pos.above()).is(Blocks.WATER);
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        level.registryAccess()
            .registry(Registries.CONFIGURED_FEATURE)
            .flatMap(configuredFeatures -> configuredFeatures.getHolder(PlantopiaVegetationFeatures.SEA_MOSS_PATCH_BONEMEAL))
            .ifPresent(featureHolder -> featureHolder.value().place(level, level.getChunkSource().getGenerator(), random, pos.above()));
    }
}
