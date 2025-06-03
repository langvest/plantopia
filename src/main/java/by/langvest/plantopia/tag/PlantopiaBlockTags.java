package by.langvest.plantopia.tag;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopiaLocationFrom;

public class PlantopiaBlockTags {
	public static final TagKey<Block> IGNORED_BY_BEES = createBlockTag("ignored_by_bees");
	public static final TagKey<Block> PREFERRED_BY_BEES = createBlockTag("preferred_by_bees");
	public static final TagKey<Block> BONEMEAL_SPREAD_GROWABLE = createBlockTag("bonemeal_spread_growable");
	public static final TagKey<Block> BONEMEAL_SPREAD_ON = createBlockTag("bonemeal_spread_on");
	public static final TagKey<Block> INFESTED_DIRT_CAN_SPREAD_TO = createBlockTag("infested_dirt_can_spread_to");

	private PlantopiaBlockTags() {}

	public static @NotNull TagKey<Block> createBlockTag(String name) {
		return BlockTags.create(plantopiaLocationFrom(name));
	}
}