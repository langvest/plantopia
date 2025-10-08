package by.langvest.plantopia.registry;

import by.langvest.toolkit.registry.Registry;
import by.langvest.plantopia.adv.PlantopiaAdvancement;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public final class PlantopiaRegistries {
	public static final Registry<Block> BLOCK = createRegistry("block");
	public static final Registry<Item> ITEM = createRegistry("item");
	public static final Registry<SoundEvent> SOUND_EVENT = createRegistry("sound_event");
	public static final Registry<PlantopiaAdvancement> ADVANCEMENT = createRegistry("advancement");
	public static final Registry<CreativeModeTab> CREATIVE_MODE_TAB = createRegistry("creative_mode_tab");
	public static final Registry<EntityType<?>> ENTITY_TYPE = createRegistry("entity_type");
	public static final Registry<BlockEntityType<?>> BLOCK_ENTITY_TYPE = createRegistry("block_entity_type");
	public static final Registry<ParticleType<?>> PARTICLE_TYPE = createRegistry("particle_type");
	public static final Registry<Feature<?>> FEATURE_TYPE = createRegistry("feature_type");
	public static final Registry<TreeDecoratorType<?>> TREE_DECORATOR_TYPE = createRegistry("tree_decorator_type");

	@Contract("_ -> new")
	private static <T> @NotNull Registry<T> createRegistry(String name) {
		return new Registry<>(plantopia(name));
	}
}
