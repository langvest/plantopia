package by.langvest.plantopia.neoforge.handler;

import by.langvest.plantopia.Plantopia;
import by.langvest.toolkit.event.game.EntitySpawnEvent;
import by.langvest.toolkit.platform.EventEmitter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Plantopia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class PlantopiaEntitySpawnHandler {
    @SubscribeEvent
    public static void onEntityJoinWorld(@NotNull EntityJoinLevelEvent event) {
        var globalEventEmitter = EventEmitter.getDefaultInstance();

        globalEventEmitter.emit(new EntitySpawnEvent() {
            @Override
            public Entity getEntity() {
                return event.getEntity();
            }

            @Override
            public Level getLevel() {
                return event.getLevel();
            }
        });
    }
}
