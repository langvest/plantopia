package by.langvest.plantopia.entity;

import net.minecraft.world.level.block.Block;

public interface PlantopiaBoatLike {
    PlantopiaBoatType getBoatType();

    void setBoatType(PlantopiaBoatType type);

    Block getDropPlanks();
}
