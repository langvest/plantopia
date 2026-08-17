package by.langvest.plantopia.extension;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public interface PlantopiaBiomeGetterExtension {
    Holder<Biome> plantopia$getBiome(BlockPos pos);
}
