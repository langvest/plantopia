package by.langvest.plantopia.util.helper;

import by.langvest.plantopia.meta.PlantopiaMetaRegistries;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.locationOf;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;

public final class PlantopiaContentHelper {
	public static final FlowerPotBlock FLOWER_POT_BLOCK = (FlowerPotBlock)Blocks.FLOWER_POT;
	public static final FireBlock FIRE_BLOCK = (FireBlock)Blocks.FIRE;
	public static final Object2FloatMap<ItemLike> COMPOSTABLES = ComposterBlock.COMPOSTABLES;
	private static List<Block> allFlowers = null;

	public static List<Block> getAllFlowers() {
		if(allFlowers != null) return allFlowers;

		Set<Block> allFlowers = Sets.newHashSet();

		allFlowers.add(Blocks.FLOWERING_AZALEA);
		allFlowers.add(Blocks.FLOWERING_AZALEA_LEAVES);
		allFlowers.add(Blocks.SPORE_BLOSSOM);
		allFlowers.add(Blocks.CHORUS_FLOWER);
		allFlowers.add(Blocks.TORCHFLOWER);
		allFlowers.add(Blocks.PINK_PETALS);
		allFlowers.add(Blocks.PITCHER_PLANT);

		ForgeRegistries.BLOCKS.getEntries()
			.stream()
			.filter(blockEntry -> {
				var block = blockEntry.getValue();
				var blockMeta = PlantopiaMetaRegistries.BLOCKS.getValue(block);

				if(blockMeta != null) {
					return blockMeta.hasItem() && blockMeta.getType().instanceOf(PlantopiaBlockMeta.MetaType.FLOWER);
				}

				return block instanceof FlowerBlock || block instanceof TallFlowerBlock;
			})
			.forEach(blockEntry -> allFlowers.add(blockEntry.getValue()));

		PlantopiaContentHelper.allFlowers = allFlowers.stream()
			.sorted(Comparator.comparing(PlantopiaResourceHelper::idOf))
			.toList();

		return PlantopiaContentHelper.allFlowers;
	}

	/* POTTED OF *************************************************************************************/

	@Nullable
	public static Block pottedBlockOf(Block plant) {
		var supplier = FLOWER_POT_BLOCK.getFullPotsView().get(locationOf(plant));

		if(supplier != null) {
			return supplier.get();
		}

		var pottedBlockMeta = PlantopiaMetaRegistries.BLOCKS.findValue(blockMeta -> {
			Block block = blockMeta.getBlock();

			if(!(block instanceof FlowerPotBlock flowerPotBlock)) return false;

			return flowerPotBlock.getContent().equals(plant);
		});

		if(pottedBlockMeta != null) {
			return pottedBlockMeta.getBlock();
		}

		return null;
	}

	public static @NotNull String pottedNameOf(String baseName) {
		return "potted_" + baseName;
	}

	public static @NotNull String pottedNameOf(Block plant) {
		String baseName = nameOf(plant);

		return pottedNameOf(baseName);
	}
}
