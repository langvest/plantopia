package by.langvest.plantopia.mixin.client.render;

import by.langvest.plantopia.extension.PlantopiaBiomeGetterExtension;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderChunkRegion.class)
public abstract class PlantopiaRenderChunkRegionMixin implements PlantopiaBiomeGetterExtension {
    @Final
    @Shadow
    protected Level level;

    @Override
    public Holder<Biome> plantopia$getBiome(BlockPos pos) {
        return level.getBiome(pos);
    }
}
