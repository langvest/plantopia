package by.langvest.plantopia.worldgen.region.special;

import net.minecraft.resources.ResourceLocation;
import terrablender.api.Region;
import terrablender.api.RegionType;

public abstract class PlantopiaRegion extends Region {
    public PlantopiaRegion(ResourceLocation name, RegionType type, int weight) {
        super(name, type, weight);
    }
}
