package by.langvest.plantopia.tag;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.PlantopiaContentHelper.plantopia;

public class PlantopiaBlockTags {
	public static final TagKey<Block> IGNORED_BY_BEES = createBlockTag("ignored_by_bees");
	public static final TagKey<Block> PREFERRED_BY_BEES = createBlockTag("preferred_by_bees");

	private PlantopiaBlockTags() {}

	public static @NotNull TagKey<Block> createBlockTag(String name) {
		return BlockTags.create(plantopia(name));
	}
}