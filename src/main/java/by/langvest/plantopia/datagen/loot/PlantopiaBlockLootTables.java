package by.langvest.plantopia.datagen.loot;

import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaQuarter;
import by.langvest.plantopia.block.PlantopiaTripleBlockHalf;
import by.langvest.plantopia.block.special.PlantopiaCloverBlock;
import by.langvest.plantopia.block.special.PlantopiaCobblestoneShardBlock;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.meta.store.PlantopiaMetaStore;
import by.langvest.plantopia.meta.property.PlantopiaBlockDropType;
import by.langvest.plantopia.meta.property.PlantopiaBlockHeightType;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PlantopiaBlockLootTables extends BlockLootSubProvider {
	protected PlantopiaBlockLootTables() {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags());
	}

	private static final LootItemConditionalFunction.Builder<?> EXPLOSION_DECAY = ApplyExplosionDecay.explosionDecay();
	private static final LootItemCondition.Builder SURVIVES_EXPLOSION = ExplosionCondition.survivesExplosion();
	private static final LootItemCondition.Builder HAS_SHEARS = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS));
	private static final float SEEDS_CHANCE = 0.125F;
	private static final Pair<Property<DoubleBlockHalf>, DoubleBlockHalf> DOUBLE_BLOCK_HALF_LOWER = Pair.of(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER);
	private static final Pair<Property<DoubleBlockHalf>, DoubleBlockHalf> DOUBLE_BLOCK_HALF_UPPER = Pair.of(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER);
	private static final Pair<Property<PlantopiaTripleBlockHalf>, PlantopiaTripleBlockHalf> TRIPLE_BLOCK_HALF_LOWER = Pair.of(PlantopiaBlockStateProperties.TRIPLE_BLOCK_HALF, PlantopiaTripleBlockHalf.LOWER);
	private static final Pair<Property<PlantopiaTripleBlockHalf>, PlantopiaTripleBlockHalf> TRIPLE_BLOCK_HALF_CENTRAL = Pair.of(PlantopiaBlockStateProperties.TRIPLE_BLOCK_HALF, PlantopiaTripleBlockHalf.CENTRAL);
	private static final Pair<Property<PlantopiaTripleBlockHalf>, PlantopiaTripleBlockHalf> TRIPLE_BLOCK_HALF_UPPER = Pair.of(PlantopiaBlockStateProperties.TRIPLE_BLOCK_HALF, PlantopiaTripleBlockHalf.UPPER);
	private static final Pair<Property<PlantopiaQuarter>, PlantopiaQuarter> QUARTER_SOUTH_WEST = Pair.of(PlantopiaBlockStateProperties.QUARTER, PlantopiaQuarter.SOUTH_WEST);
	private static final Pair<Property<PlantopiaQuarter>, PlantopiaQuarter> QUARTER_WEST_NORTH = Pair.of(PlantopiaBlockStateProperties.QUARTER, PlantopiaQuarter.WEST_NORTH);
	private static final Pair<Property<PlantopiaQuarter>, PlantopiaQuarter> QUARTER_NORTH_EAST = Pair.of(PlantopiaBlockStateProperties.QUARTER, PlantopiaQuarter.NORTH_EAST);
	private static final Pair<Property<PlantopiaQuarter>, PlantopiaQuarter> QUARTER_EAST_SOUTH = Pair.of(PlantopiaBlockStateProperties.QUARTER, PlantopiaQuarter.EAST_SOUTH);
	private static final Set<Block> EXPLOSION_RESISTANT_BLOCKS = Sets.newHashSet();

	@Override
	protected void generate() {
		generateAll();

		add(PlantopiaBlocks.GIANT_GRASS.get(), block -> createTriplePlantWithSeedDrops(block, Blocks.GRASS, Items.WHEAT_SEEDS));
		add(PlantopiaBlocks.GIANT_FERN.get(), block -> createTriplePlantWithSeedDrops(block, Blocks.FERN, Items.WHEAT_SEEDS));
		add(PlantopiaBlocks.CLOVER.get(), PlantopiaBlockLootTables::createCloverDrops);
		add(PlantopiaBlocks.COBBLESTONE_SHARD.get(), PlantopiaBlockLootTables::createCobblestoneShardDrops);
		add(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD.get(), PlantopiaBlockLootTables::createCobblestoneShardDrops);
		add(PlantopiaBlocks.BUSH.get(), PlantopiaBlockLootTables::createBushDrops);
		add(PlantopiaBlocks.POLLINATED_DANDELION.get(), PlantopiaBlockLootTables::createPollinatedDandelionDrops);
		add(PlantopiaBlocks.BRANCHING_SHRUB.get(), PlantopiaBlockLootTables::createBranchingShrubDrops);
		add(PlantopiaBlocks.BRANCHING_SHRUB_PLANT.get(), PlantopiaBlockLootTables::createBranchingShrubDrops);
	}

	private void generateAll() {
		PlantopiaMetaStore.getBlocks().forEach(blockMeta -> {
			if(!blockMeta.shouldGenerateLootTable()) return;

			PlantopiaBlockDropType dropType = blockMeta.getDropType();

			if(dropType == PlantopiaBlockDropType.SELF) {
				dropSelf(blockMeta);
				return;
			}

			if(dropType == PlantopiaBlockDropType.SELF_BY_SHEARS) {
				dropSelfByShears(blockMeta);
				return;
			}

			dropGenerated(blockMeta);
		});
	}

	@Override
	protected @NotNull Iterable<Block> getKnownBlocks() {
		return PlantopiaMetaStore.getBlocks().stream().filter(PlantopiaBlockMeta::hasDrop).map(PlantopiaBlockMeta::getBlock)::iterator;
	}

	/* DROPS GENERATION ******************************************/

	private void dropGenerated(@NotNull PlantopiaBlockMeta blockMeta) {
		Block block = blockMeta.getBlock();

		if(block instanceof FlowerPotBlock) {
			dropPottedContents(block);
			return;
		}

		dropSelf(blockMeta);
	}

	private void dropSelf(@NotNull PlantopiaBlockMeta blockMeta) {
		Block block = blockMeta.getBlock();
		MetaType type = blockMeta.getType();
		PlantopiaBlockHeightType blockHeightType = blockMeta.getBlockHeightType();
		int baseHeight = blockHeightType.getBaseHeight();

		if(baseHeight == 1) {
			dropSelf(block);
			return;
		}

		if(type != MetaType.PLANT) {
			dropSelfIf(block, hasLowerHalfProperty(block, blockHeightType));
			return;
		}

		LootPoolEntryContainer.Builder<?> lootEntry = withSurvivesExplosionCondition(block, LootItem.lootTableItem(block));

		add(block, createTable(blockMeta, lootEntry));
	}

	private void dropSelfByShears(@NotNull PlantopiaBlockMeta blockMeta) {
		Block block = blockMeta.getBlock();
		MetaType type = blockMeta.getType();
		PlantopiaBlockHeightType blockHeightType = blockMeta.getBlockHeightType();
		int baseHeight = blockHeightType.getBaseHeight();

		if(baseHeight == 1) {
			dropSelfByShears(block);
			return;
		}

		if(type != MetaType.PLANT) {
			dropSelfByShearsIf(block, hasLowerHalfProperty(block, blockHeightType));
			return;
		}

		LootPoolEntryContainer.Builder<?> lootEntry = LootItem.lootTableItem(block).when(HAS_SHEARS);

		add(block, createTable(blockMeta, lootEntry));
	}

	/* SIMPLE DROPS GENERATION ******************************************/

	@Override
	public void dropSelf(@NotNull Block block) {
		LootTable.Builder lootTable = LootTable.lootTable()
			.withPool(
				withSurvivesExplosionCondition(block, LootPool.lootPool())
					.add(item(block))
			);

		add(block, lootTable);
	}

	private void dropSelfIf(Block block, LootItemCondition.Builder condition) {
		LootTable.Builder lootTable = LootTable.lootTable()
			.withPool(
				withSurvivesExplosionCondition(block, LootPool.lootPool())
					.add(item(block).when(condition))
			);

		add(block, lootTable);
	}

	private void dropSelfByShears(Block block) {
		LootTable.Builder lootTable = LootTable.lootTable()
			.withPool(
				LootPool.lootPool()
					.add(item(block))
					.when(HAS_SHEARS)
			);

		add(block, lootTable);
	}

	private void dropSelfByShearsIf(Block block, LootItemCondition.Builder condition) {
		LootTable.Builder lootTable = LootTable.lootTable()
			.withPool(
				LootPool.lootPool()
					.add(item(block).when(condition))
					.when(HAS_SHEARS)
			);

		add(block, lootTable);
	}

	/* CUSTOM DROPS ******************************************/

	private static LootTable.@NotNull Builder createTriplePlantWithSeedDrops(Block block, Block sheared, Item seeds) {
		LootPoolEntryContainer.Builder<?> lootEntry = item(sheared)
			.apply(setCount(3))
			.when(HAS_SHEARS)
			.otherwise(
				withSurvivesExplosionCondition(block, item(seeds))
					.when(randomChance(SEEDS_CHANCE))
			);

		return createTripleHighPlantTable(block, lootEntry);
	}

	private static LootTable.@NotNull Builder createCloverDrops(Block block) {
		LootPoolEntryContainer.Builder<?> lootEntry = createPartialLootEntry(block, PlantopiaCloverBlock.AMOUNT)
			.when(HAS_SHEARS)
			.otherwise(
				withSurvivesExplosionCondition(block, item(Items.WHEAT_SEEDS))
					.when(randomChance(SEEDS_CHANCE * 0.75F))
			);

		return createBlockTable(block, lootEntry);
	}

	private static LootTable.@NotNull Builder createCobblestoneShardDrops(Block block) {
		LootPoolEntryContainer.Builder<?> lootEntry = withExplosionDecayFunction(block, createPartialLootEntry(block, PlantopiaCobblestoneShardBlock.SHARDS));

		return createSurvivedExplosionBlockTable(block, lootEntry);
	}

	private static LootTable.@NotNull Builder createBushDrops(Block block) {
		LootPoolEntryContainer.Builder<?> lootEntry = item(block)
			.when(HAS_SHEARS)
			.otherwise(
				withExplosionDecayFunction(
					block,
					withSurvivesExplosionCondition(block, item(Items.STICK))
						.when(randomChance(SEEDS_CHANCE))
						.apply(setCount(1, 2))
				)
			);

		return createBlockTable(block, lootEntry);
	}

	private static LootTable.@NotNull Builder createPollinatedDandelionDrops(Block block) {
		return createSurvivedExplosionBlockTable(block, item(Blocks.DANDELION));
	}

	private static LootTable.@NotNull Builder createBranchingShrubDrops(Block block) {
		LootPoolEntryContainer.Builder<?> lootEntry = item(PlantopiaBlocks.BRANCHING_SHRUB.get())
			.when(HAS_SHEARS)
			.otherwise(
				withExplosionDecayFunction(
					block,
					withSurvivesExplosionCondition(block, item(Items.STICK))
						.when(randomChance(SEEDS_CHANCE * 1.75F))
						.apply(setCount(1, 2))
				)
			);

		return createBlockTable(block, lootEntry);
	}

	/* HELPER METHODS ******************************************/

	private static LootPoolSingletonContainer.@NotNull Builder<?> item(ItemLike item) {
		return LootItem.lootTableItem(item);
	}

	private static LootItemConditionalFunction.@NotNull Builder<?> setCount(int count) {
		return SetItemCountFunction.setCount(ConstantValue.exactly(count));
	}

	private static LootItemConditionalFunction.@NotNull Builder<?> setCount(int from, int to) {
		return SetItemCountFunction.setCount(UniformGenerator.between(from, to));
	}

	private static LootItemCondition.@NotNull Builder randomChance(float chance) {
		return LootItemRandomChanceCondition.randomChance(chance);
	}

	private static <T extends Comparable<T> & StringRepresentable, P extends Property<T>> LootItemBlockStatePropertyCondition.@NotNull Builder hasProperty(Block block, @NotNull Pair<P, T> property) {
		return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(
			StatePropertiesPredicate.Builder.properties().hasProperty(property.getFirst(), property.getSecond())
		);
	}

	private static <P extends Property<Integer>> LootItemBlockStatePropertyCondition.@NotNull Builder hasProperty(Block block, P property, Integer value) {
		return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(
			StatePropertiesPredicate.Builder.properties().hasProperty(property, value)
		);
	}

	private static LootItemCondition.@NotNull Builder hasLowerHalfProperty(Block block, PlantopiaBlockHeightType blockHeightType) {
		if(blockHeightType == PlantopiaBlockHeightType.TRIPLE) return hasProperty(block, TRIPLE_BLOCK_HALF_LOWER);
		return hasProperty(block, DOUBLE_BLOCK_HALF_LOWER);
	}

	private static <T extends Comparable<T> & StringRepresentable> LootItemCondition.@NotNull Builder checkPropertyAt(Block block, @NotNull Pair<Property<T>, T> property, BlockPos pos) {
		return LocationCheck.checkLocation(
			LocationPredicate.Builder.location().setBlock(
				BlockPredicate.Builder.block().of(block).setProperties(
					StatePropertiesPredicate.Builder.properties().hasProperty(property.getFirst(), property.getSecond()).build()
				).build()
			), pos
		);
	}

	@Contract(value = "_ -> new", pure = true)
	private static @NotNull BlockPos y(int offset) {
		return new BlockPos(0, offset, 0);
	}

	private static <T extends FunctionUserBuilder<T>> T withExplosionDecayFunction(@NotNull Block block, FunctionUserBuilder<T> function) {
		return EXPLOSION_RESISTANT_BLOCKS.contains(block) ? function.unwrap() : function.apply(EXPLOSION_DECAY);
	}

	private static <T extends ConditionUserBuilder<T>> T withSurvivesExplosionCondition(@NotNull Block block, ConditionUserBuilder<T> condition) {
		return EXPLOSION_RESISTANT_BLOCKS.contains(block) ? condition.unwrap() : condition.when(SURVIVES_EXPLOSION);
	}

	/* LOOT ENTRY PATTERNS ******************************************/

	private static LootPoolSingletonContainer.@NotNull Builder<?> createPartialLootEntry(Block block, IntegerProperty property) {
		return createPartialLootEntry(block, block, property);
	}

	private static LootPoolSingletonContainer.@NotNull Builder<?> createPartialLootEntry(Block block, ItemLike drop, IntegerProperty property) {
		LootPoolSingletonContainer.Builder<?> lootEntry = item(drop);
		if(property != null) for(Integer value : property.getPossibleValues()) {
			if(value == 1) continue;
			lootEntry.apply(setCount(value).when(hasProperty(block, property, value)));
		}
		return lootEntry;
	}

	/* LOOT TABLE PATTERNS ******************************************/

	private static LootTable.@NotNull Builder createTable(@NotNull PlantopiaBlockMeta blockMeta, LootPoolEntryContainer.Builder<?> @NotNull ... lootEntries) {
		Block block = blockMeta.getBlock();
		MetaType type = blockMeta.getType();
		int height = blockMeta.getBlockHeightType().getBaseHeight();
		int width = blockMeta.getBlockWidthType().getBaseWidth();

		if(height == 3 && width == 1) {
			if(type == MetaType.PLANT) return createTripleHighPlantTable(block, lootEntries);
			return createTripleHighBlockTable(block, lootEntries);
		}

		if(height == 3 && width == 2) {
			if(type == MetaType.PLANT) return createWideTripleHighPlantTable(block, lootEntries);
			// return createWideTripleHighBlockTable(block, lootEntries);
		}

		if(height == 2) {
			if(type == MetaType.PLANT) return createDoubleHighPlantTable(block, lootEntries);
			return createDoubleHighBlockTable(block, lootEntries);
		}

		return createBlockTable(block, lootEntries);
	}

	private static LootTable.@NotNull Builder createBlockTable(@SuppressWarnings("unused") Block block, LootPoolEntryContainer.Builder<?> @NotNull ... lootEntries) {
		LootPool.Builder lootPool = LootPool.lootPool();
		for(LootPoolEntryContainer.Builder<?> lootEntry : lootEntries) lootPool.add(lootEntry);
		return LootTable.lootTable().withPool(lootPool);
	}

	private static LootTable.@NotNull Builder createSurvivedExplosionBlockTable(Block block, LootPoolEntryContainer.Builder<?> @NotNull ... lootEntries) {
		LootPool.Builder lootPool = withSurvivesExplosionCondition(block, LootPool.lootPool());
		for(LootPoolEntryContainer.Builder<?> lootEntry : lootEntries) lootPool.add(lootEntry);
		return LootTable.lootTable().withPool(lootPool);
	}

	private static LootTable.@NotNull Builder createDoubleHighBlockTable(Block block, LootPoolEntryContainer.Builder<?> @NotNull ... lootEntries) {
		return createLowerBlockTable(block, DOUBLE_BLOCK_HALF_LOWER, lootEntries);
	}

	private static LootTable.@NotNull Builder createTripleHighBlockTable(Block block, LootPoolEntryContainer.Builder<?> @NotNull ... lootEntries) {
		return createLowerBlockTable(block, TRIPLE_BLOCK_HALF_LOWER, lootEntries);
	}

	private static <T extends Comparable<T> & StringRepresentable> LootTable.@NotNull Builder createLowerBlockTable(Block block, Pair<Property<T>, T> property, LootPoolEntryContainer.Builder<?> @NotNull ... lootEntries) {
		LootPool.Builder lootPool = LootPool.lootPool();
		for(LootPoolEntryContainer.Builder<?> lootEntry : lootEntries) lootPool.add(lootEntry);
		return LootTable.lootTable().withPool(lootPool.when(hasProperty(block, property)));
	}

	private static LootTable.@NotNull Builder createDoubleHighPlantTable(Block block, LootPoolEntryContainer.Builder<?> @NotNull ... lootEntries) {
		LootPool.Builder lowerLootPool = LootPool.lootPool();
		LootPool.Builder upperLootPool = LootPool.lootPool();

		for(LootPoolEntryContainer.Builder<?> lootEntry : lootEntries) {
			lowerLootPool.add(lootEntry);
			upperLootPool.add(lootEntry);
		}

		return LootTable.lootTable()
			.withPool(
				lowerLootPool
					.when(hasProperty(block, DOUBLE_BLOCK_HALF_LOWER))
					.when(checkPropertyAt(block, DOUBLE_BLOCK_HALF_UPPER, y(1)))
			)
			.withPool(
				upperLootPool
					.when(hasProperty(block, DOUBLE_BLOCK_HALF_UPPER))
					.when(checkPropertyAt(block, DOUBLE_BLOCK_HALF_LOWER, y(-1)))
			);
	}

	private static LootTable.@NotNull Builder createTripleHighPlantTable(Block block, LootPoolEntryContainer.Builder<?> @NotNull ... lootEntries) {
		LootPool.Builder lowerLootPool = LootPool.lootPool();
		LootPool.Builder centralLootPool = LootPool.lootPool();
		LootPool.Builder upperLootPool = LootPool.lootPool();

		for(LootPoolEntryContainer.Builder<?> lootEntry : lootEntries) {
			lowerLootPool.add(lootEntry);
			centralLootPool.add(lootEntry);
			upperLootPool.add(lootEntry);
		}

		return LootTable.lootTable()
			.withPool(
				lowerLootPool
					.when(hasProperty(block, TRIPLE_BLOCK_HALF_LOWER))
					.when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_CENTRAL, y(1)))
					.when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_UPPER, y(2)))
			)
			.withPool(
				centralLootPool
					.when(hasProperty(block, TRIPLE_BLOCK_HALF_CENTRAL))
					.when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_LOWER, y(-1)))
					.when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_UPPER, y(1)))
			)
			.withPool(
				upperLootPool
					.when(hasProperty(block, TRIPLE_BLOCK_HALF_UPPER))
					.when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_LOWER, y(-2)))
					.when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_CENTRAL, y(-1)))
			);
	}

	private static LootTable.@NotNull Builder createWideTripleHighPlantTable(Block block, LootPoolEntryContainer.Builder<?> @NotNull ... lootEntries) {
		LootPool.Builder lowerLootPool = LootPool.lootPool();
		LootPool.Builder centralLootPool = LootPool.lootPool();
		LootPool.Builder upperLootPool = LootPool.lootPool();

		for(LootPoolEntryContainer.Builder<?> lootEntry : lootEntries) {
			lowerLootPool.add(lootEntry);
			centralLootPool.add(lootEntry);
			upperLootPool.add(lootEntry);
		}

		return LootTable.lootTable()
			.withPool(
				lowerLootPool
					.when(hasProperty(block, TRIPLE_BLOCK_HALF_LOWER))
					.when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_CENTRAL, y(1)))
					.when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_UPPER, y(2)))
			)
			.withPool(
				centralLootPool
					.when(hasProperty(block, TRIPLE_BLOCK_HALF_CENTRAL))
					.when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_LOWER, y(-1)))
					.when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_UPPER, y(1)))
			)
			.withPool(
				upperLootPool
					.when(hasProperty(block, TRIPLE_BLOCK_HALF_UPPER))
					.when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_LOWER, y(-2)))
					.when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_CENTRAL, y(-1)))
			);




		// LootPool.Builder southWestLootPool = LootPool.lootPool();
		// LootPool.Builder westNorthLootPool = LootPool.lootPool();
		// LootPool.Builder northEastLootPool = LootPool.lootPool();
		// LootPool.Builder eastSouthLootPool = LootPool.lootPool();
		//
		// for(LootPoolEntryContainer.Builder<?> lootEntry : lootEntries) {
		// 	southWestLootPool.add(lootEntry);
		// 	westNorthLootPool.add(lootEntry);
		// 	northEastLootPool.add(lootEntry);
		// 	eastSouthLootPool.add(lootEntry);
		// }
		//
		// return LootTable.lootTable()
		// 	.withPool(
		// 		southWestLootPool
		// 			.when(hasProperty(block, QUARTER_SOUTH_WEST))
		// 			// .when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_CENTRAL, y(1)))
		// 			// .when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_UPPER, y(2)))
		// 	)
		// 	.withPool(
		// 		westNorthLootPool
		// 			.when(hasProperty(block, QUARTER_WEST_NORTH))
		// 			// .when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_LOWER, y(-1)))
		// 			// .when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_UPPER, y(1)))
		// 	)
		// 	.withPool(
		// 		northEastLootPool
		// 			.when(hasProperty(block, QUARTER_NORTH_EAST))
		// 			// .when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_LOWER, y(-2)))
		// 			// .when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_CENTRAL, y(-1)))
		// 	)
		// 	.withPool(
		// 		eastSouthLootPool
		// 			.when(hasProperty(block, QUARTER_EAST_SOUTH))
		// 			// .when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_LOWER, y(-2)))
		// 			// .when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_CENTRAL, y(-1)))
		// 	);
	}
}