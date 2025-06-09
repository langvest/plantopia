package by.langvest.plantopia.particle;

import by.langvest.plantopia.Plantopia;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class PlantopiaParticleTypes {
	private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Plantopia.MOD_ID);

	public static final RegistryObject<SimpleParticleType> FLUFFY_DANDELION_SEED = registerParticleType("fluffy_dandelion_seed", false);
	public static final RegistryObject<SimpleParticleType> QUICKSAND = registerParticleType("quicksand", false);
	public static final RegistryObject<ParticleType<ItemParticleOption>> BREAKING_ITEM = registerParticleType("breaking_item", false, ItemParticleOption.DESERIALIZER, ItemParticleOption::codec);

	private static RegistryObject<SimpleParticleType> registerParticleType(String name, boolean overrideLimiter) {
		return registerParticleType(name, () -> new SimpleParticleType(overrideLimiter));
	}

	private static <T extends ParticleOptions> RegistryObject<ParticleType<T>> registerParticleType(String name, boolean overrideLimiter, ParticleOptions.Deserializer<T> deserializer, final Function<ParticleType<T>, Codec<T>> codecFactory) {
		return registerParticleType(name, () -> new ParticleType<>(overrideLimiter, deserializer) {
			public @NotNull Codec<T> codec() {
				return codecFactory.apply(this);
			}
		});
	}

	private static <T extends ParticleType<?>> RegistryObject<T> registerParticleType(String name, Supplier<T> supplier) {
		return PARTICLE_TYPE_REGISTER.register(name, supplier);
	}

	public static void setup(IEventBus bus) {
		PARTICLE_TYPE_REGISTER.register(bus);
	}
}