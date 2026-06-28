package by.langvest.plantopia.worldgen.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public final class PlantopiaDamageTypes {
    public static final ResourceKey<DamageType> THORNY_SHRUB = createKey("thorny_shrub");
    public static final ResourceKey<DamageType> QUICKSAND = createKey("quicksand");

    protected static @NotNull ResourceKey<DamageType> createKey(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, plantopia(name));
    }
}
