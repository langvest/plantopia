package by.langvest.toolkit.event.game;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public abstract class EntitySpawnEvent extends GameEvent {
    public abstract Entity getEntity();

    public abstract Level getLevel();
}
