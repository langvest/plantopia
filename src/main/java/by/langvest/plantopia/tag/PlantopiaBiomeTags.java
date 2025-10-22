package by.langvest.plantopia.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaBiomeTags {
	public static final TagKey<Biome> HAS_COBBLESTONE_SHARD = createBlockTag("has_feature/cobblestone_shard");
	public static final TagKey<Biome> HAS_COBBLESTONE_SHARD_IN_WATER = createBlockTag("has_feature/cobblestone_shard_in_water");
	public static final TagKey<Biome> HAS_MOSSY_COBBLESTONE_SHARD = createBlockTag("has_feature/mossy_cobblestone_shard");
	public static final TagKey<Biome> HAS_MOSSY_COBBLESTONE_SHARD_2 = createBlockTag("has_feature/mossy_cobblestone_shard_2");
	public static final TagKey<Biome> HAS_MOSSY_COBBLESTONE_SHARD_IN_WATER = createBlockTag("has_feature/mossy_cobblestone_shard_in_water");
	public static final TagKey<Biome> HAS_TINY_CACTUS = createBlockTag("has_feature/tiny_cactus");
	public static final TagKey<Biome> HAS_FIREWEED = createBlockTag("has_feature/fireweed");

	private PlantopiaBiomeTags() {}

	public static @NotNull TagKey<Biome> createBlockTag(String name) {
		return TagKey.create(Registries.BIOME, plantopia(name));
	}
}