package by.langvest.plantopia.util.helper;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.toolkit.meta.SimpleMetaObject;
import by.langvest.toolkit.platform.RegistryHelper;
import by.langvest.toolkit.registry.RegistryObject;
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
	private static List<ItemLike> allHerbs;
	private static List<ItemLike> allMushrooms;

	public static List<ItemLike> getAllFlowers() {
		if(allFlowers != null) return allFlowers;

		var registryHelper = Plantopia.getPlatform().getRegistryHelper();
		var blockRegistry = registryHelper.getKnownRegistryOrThrow(Registries.BLOCK);

		Set<ItemLike> allFlowersSet = Sets.newHashSet();

		allFlowersSet.add(Blocks.FLOWERING_AZALEA);
		allFlowersSet.add(Blocks.FLOWERING_AZALEA_LEAVES);
		allFlowersSet.add(Blocks.SPORE_BLOSSOM);
		allFlowersSet.add(Blocks.CHORUS_FLOWER);
		allFlowersSet.add(Blocks.TORCHFLOWER);
		allFlowersSet.add(Blocks.PINK_PETALS);
		allFlowersSet.add(Blocks.PITCHER_PLANT);
		allFlowersSet.add(PlantopiaBlocks.FLOWERING_TINY_CACTUS.get());

		blockRegistry.stream()
			.filter(block -> metaOf(block)
				.map(blockMeta -> blockMeta.hasItem() && blockMeta.getType().instanceOf(MetaType.FLOWER))
				.orElseGet(() -> block instanceof FlowerBlock || block instanceof TallFlowerBlock)
			)
			.forEach(allFlowersSet::add);

		allFlowers = allFlowersSet.stream()
			.sorted(PlantopiaResourceHelper::compareById)
			.toList();

		return allFlowers;
	}

	public static List<ItemLike> getAllHerbs() {
		if(allHerbs != null) return allHerbs;

		var registryHelper = Plantopia.getPlatform().getRegistryHelper();
		var blockRegistry = registryHelper.getKnownRegistryOrThrow(Registries.BLOCK);

		Set<ItemLike> allHerbsSet = Sets.newHashSet();

		allHerbsSet.add(Blocks.CRIMSON_ROOTS);
		allHerbsSet.add(Blocks.WARPED_ROOTS);
		allHerbsSet.add(PlantopiaBlocks.CLOVER.get());
		allHerbsSet.add(PlantopiaBlocks.HOGWEED.get());

		blockRegistry.stream()
			.filter(block -> metaOf(block)
				.map(blockMeta -> {
					var metaType = blockMeta.getType();
					return blockMeta.hasItem()
						&& (metaType.instanceOf(MetaType.HERB) || metaType.instanceOf(MetaType.GRASS));
				})
				.orElseGet(() -> block instanceof TallGrassBlock || block instanceof DeadBushBlock)
			)
			.forEach(allHerbsSet::add);

		allHerbs = allHerbsSet.stream()
			.sorted(PlantopiaResourceHelper::compareById)
			.toList();

		return allHerbs;
	}

	public static List<ItemLike> getAllMushrooms() {
		if(allMushrooms != null) return allMushrooms;

		var registryHelper = Plantopia.getPlatform().getRegistryHelper();
		var blockRegistry = registryHelper.getKnownRegistryOrThrow(Registries.BLOCK);

		Set<ItemLike> allMushroomsSet = Sets.newHashSet();

		blockRegistry.stream()
			.filter(block -> metaOf(block)
				.map(blockMeta -> blockMeta.hasItem() && blockMeta.getType().instanceOf(PlantopiaBlockMeta.MetaType.MUSHROOM))
				.orElseGet(() -> block instanceof MushroomBlock || block instanceof FungusBlock)
			)
			.forEach(allMushroomsSet::add);

		allMushrooms = allMushroomsSet.stream()
			.sorted(PlantopiaResourceHelper::compareById)
			.toList();

		return allMushrooms;
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

	public static @NotNull String pottedNameOf(RegistryObject<? extends Block> plant) {
		String baseName = nameOf(plant);

		return pottedNameOf(baseName);
	}

	public static @NotNull String pottedNameOf(Block plant) {
		String baseName = nameOf(plant);

		return pottedNameOf(baseName);
	}
}
