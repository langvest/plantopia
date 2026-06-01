package by.langvest.plantopia.worldgen.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaDamageTypes {
    public static final ResourceKey<DamageType> THORNY_SHRUB = createKey("thorny_shrub");
    public static final ResourceKey<DamageType> QUICKSAND = createKey("quicksand");

    public static void bootstrap(@NotNull BootstapContext<DamageType> context) {
        context.register(THORNY_SHRUB, new DamageType("thornyShrub", 0.1F, DamageEffects.POKING));
        context.register(QUICKSAND, new DamageType("quicksand", 0.0F));
    }

    protected static @NotNull ResourceKey<DamageType> createKey(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, plantopia(name));
    }
}
