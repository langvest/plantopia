package by.langvest.plantopia.item.special;

import by.langvest.plantopia.entity.PlantopiaBoatType;
import by.langvest.plantopia.entity.special.PlantopiaBoatEntity;
import by.langvest.plantopia.entity.special.PlantopiaChestBoatEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaBoatItem extends BoatItem {
    protected final Supplier<PlantopiaBoatType> type;

    public PlantopiaBoatItem(boolean hasChest, Supplier<PlantopiaBoatType> type, Properties properties) {
        super(hasChest, Boat.Type.OAK, properties);
        this.type = type;
    }

    public PlantopiaBoatType getBoatType() {
        return type.get();
    }

    @Override
    public @NotNull Boat getBoat(Level level, HitResult hitResult) {
        var vec3 = hitResult.getLocation();
        return hasChest ? new PlantopiaChestBoatEntity(level, vec3.x, vec3.y, vec3.z) : new PlantopiaBoatEntity(level, vec3.x, vec3.y, vec3.z);
    }
}
