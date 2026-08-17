package by.langvest.plantopia.client.color;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

@FunctionalInterface
public interface PlantopiaColorResolver {
    int getColor(Holder<Biome> biome, BlockPos pos);
}
