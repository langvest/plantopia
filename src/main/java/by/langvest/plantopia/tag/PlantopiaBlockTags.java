package by.langvest.plantopia.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaBlockTags {
	public static final TagKey<Block> IGNORED_BY_BEES = createBlockTag("ignored_by_bees");
	public static final TagKey<Block> PREFERRED_BY_BEES = createBlockTag("preferred_by_bees");
	public static final TagKey<Block> BONEMEAL_SPREAD_GROWABLE = createBlockTag("bonemeal_spread_growable");
	public static final TagKey<Block> BONEMEAL_SPREAD_ON = createBlockTag("bonemeal_spread_on");
	public static final TagKey<Block> COBBLESTONE_SHARD_CAN_GENERATE_ON = createBlockTag("cobblestone_shard_can_generate_on");
	public static final TagKey<Block> INFESTED_DIRT_CAN_SPREAD_TO = createBlockTag("infested_dirt_can_spread_to");
	public static final TagKey<Block> BREAKS_INTO_AIR_BY_COBBLESTONE_SHARDS = createBlockTag("breaks_into_air_by_cobblestone_shards");
	public static final TagKey<Block> BREAKS_INTO_WATER_BY_COBBLESTONE_SHARDS = createBlockTag("breaks_into_water_by_cobblestone_shards");

	private PlantopiaBlockTags() {}

	public static @NotNull TagKey<Block> createBlockTag(String name) {
		return TagKey.create(Registries.BLOCK, plantopia(name));
	}
}