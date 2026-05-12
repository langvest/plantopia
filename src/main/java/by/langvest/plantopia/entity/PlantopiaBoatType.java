package by.langvest.plantopia.entity;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.toolkit.event.LifecycleEvent;
import by.langvest.toolkit.registry.RegistryObject;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public record PlantopiaBoatType(
    Function<Boat, ? extends Block> planks,
    Function<Boat, ? extends Item> item
) {
    private static final BiMap<Byte, PlantopiaBoatType> ID_MAP = HashBiMap.create();

    public static void setup(LifecycleEvent.CommonSetupEvent event) {
        ID_MAP.clear();

        List<ResourceLocation> sortedIdentifiers = PlantopiaRegistries.BOAT_TYPE.getAll()
            .stream()
            .map(RegistryObject::getIdentifier)
            .sorted(Comparator.naturalOrder())
            .toList();

        if (sortedIdentifiers.size() > Byte.MAX_VALUE) {
            throw new IllegalStateException("Exceeded the maximum number of boat types that can be represented by a byte: " + sortedIdentifiers.size());
        }

        for (int i = 0; i < sortedIdentifiers.size(); i++) {
            var identifier = sortedIdentifiers.get(i);
            var type = PlantopiaRegistries.BOAT_TYPE.getValueOrThrow(identifier).get();
            ID_MAP.put((byte) i, type);
        }
    }

    public static PlantopiaBoatType byId(byte id) {
        return ID_MAP.get(id);
    }

    public static byte toId(PlantopiaBoatType type) {
        Byte id = ID_MAP.inverse().get(type);
        if (id == null) {
            throw new IllegalArgumentException("Tried to get ID for an unregistered boat type: " + type);
        }
        return id;
    }
}
