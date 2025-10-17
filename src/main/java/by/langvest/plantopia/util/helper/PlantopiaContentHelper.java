package by.langvest.plantopia.util.helper;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.toolkit.meta.SimpleMetaObject;
import by.langvest.toolkit.platform.RegistryHelper;
import com.google.common.collect.Sets;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.*;

public final class PlantopiaContentHelper {
	private static List<ItemLike> allFlowers;

	public static List<ItemLike> getAllFlowers() {
		if(allFlowers != null) return allFlowers;

		Set<ItemLike> allFlowersSet = Sets.newHashSet();

		allFlowersSet.add(Blocks.FLOWERING_AZALEA);
		allFlowersSet.add(Blocks.FLOWERING_AZALEA_LEAVES);
		allFlowersSet.add(Blocks.SPORE_BLOSSOM);
		allFlowersSet.add(Blocks.CHORUS_FLOWER);
		allFlowersSet.add(Blocks.TORCHFLOWER);
		allFlowersSet.add(Blocks.PINK_PETALS);
		allFlowersSet.add(Blocks.PITCHER_PLANT);
		allFlowersSet.add(PlantopiaBlocks.FLOWERING_TINY_CACTUS.get());

		var registryHelper = Plantopia.getPlatform().getRegistryHelper();
		var blockRegistry = registryHelper.getKnownRegistryOrThrow(Registries.BLOCK);

		blockRegistry.stream()
			.filter(block -> metaOf(block)
				.map(blockMeta -> blockMeta.hasItem() && blockMeta.getType().instanceOf(PlantopiaBlockMeta.MetaType.FLOWER))
				.orElseGet(() -> block instanceof FlowerBlock || block instanceof TallFlowerBlock)
			)
			.forEach(allFlowersSet::add);

		allFlowers = allFlowersSet.stream()
			.sorted(PlantopiaResourceHelper::compareById)
			.toList();

		return allFlowers;
	}

	/* POTTED OF *************************************************************************************/

	public static Optional<Block> pottedBlockOf(Block plant) {
		var supplier = RegistryHelper.getEmptyFlowerPotBlock().getFullPotsView().get(locationOf(plant));

		if(supplier != null) {
			return Optional.ofNullable(supplier.get());
		}

		return PlantopiaMetaBuckets.BLOCK.findValue(blockMeta -> {
			var block = blockMeta.get();

			if(block instanceof FlowerPotBlock pottedBlock) {
				return pottedBlock.getContent().equals(plant);
			}

			return false;
		}).map(SimpleMetaObject::get);
	}

	public static @NotNull String pottedNameOf(String baseName) {
		return "potted_" + baseName;
	}

	public static @NotNull String pottedNameOf(Block plant) {
		String baseName = nameOf(plant);

		return pottedNameOf(baseName);
	}
}
