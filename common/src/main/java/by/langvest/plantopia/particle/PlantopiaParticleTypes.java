package by.langvest.plantopia.particle;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.registry.RegistryObject;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaParticleTypes {
    public static final RegistryObject<SimpleParticleType> FLUFFY_DANDELION_SEED = registerParticleType("fluffy_dandelion_seed", false);
    public static final RegistryObject<SimpleParticleType> YELLOW_MAPLE_LEAVES = registerParticleType("yellow_maple_leaves", false);
    public static final RegistryObject<SimpleParticleType> ORANGE_MAPLE_LEAVES = registerParticleType("orange_maple_leaves", false);
    public static final RegistryObject<SimpleParticleType> RED_MAPLE_LEAVES = registerParticleType("red_maple_leaves", false);
    public static final RegistryObject<SimpleParticleType> JACARANDA_LEAVES = registerParticleType("jacaranda_leaves", false);
    public static final RegistryObject<SimpleParticleType> QUICKSAND = registerParticleType("quicksand", false);
    public static final RegistryObject<SimpleParticleType> WITCHY_TOADSTOOL_SPORE = registerParticleType("witchy_toadstool_spore", false);
    public static final RegistryObject<ParticleType<ItemParticleOption>> BREAKING_ITEM = registerParticleType("breaking_item", false, ItemParticleOption.DESERIALIZER, ItemParticleOption::codec);

    private static RegistryObject<SimpleParticleType> registerParticleType(String name, boolean overrideLimiter) {
        return registerParticleType(plantopia(name), () -> new SimpleParticleType(overrideLimiter));
    }

    private static <T extends ParticleOptions> RegistryObject<ParticleType<T>> registerParticleType(String name, boolean overrideLimiter, ParticleOptions.Deserializer<T> deserializer, final Function<ParticleType<T>, Codec<T>> codecFactory) {
        return registerParticleType(plantopia(name), () -> new ParticleType<>(overrideLimiter, deserializer) {
            public @NotNull Codec<T> codec() {
                return codecFactory.apply(this);
            }
        });
    }

    private static <T extends ParticleType<?>> RegistryObject<T> registerParticleType(ResourceLocation identifier, Supplier<T> supplier) {
        return PlantopiaRegistries.PARTICLE_TYPE.register(identifier, supplier);
    }

    public static void setup(@NotNull RegisterEvent event) {
        event.registerAll(Registries.PARTICLE_TYPE, PlantopiaRegistries.PARTICLE_TYPE);
    }
}
