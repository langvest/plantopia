package by.langvest.plantopia.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaEntityTypeTags {
    public static final TagKey<EntityType<?>> QUICKSAND_WALKABLE_MOBS = createEntityTypeTag("quicksand_walkable_mobs");
    public static final TagKey<EntityType<?>> QUICKSAND_IMMUNE_ENTITY_TYPES = createEntityTypeTag("quicksand_immune_entity_types");

    public static @NotNull TagKey<EntityType<?>> createEntityTypeTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, plantopia(name));
    }
}
