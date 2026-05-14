package by.langvest.plantopia.datagen.loot;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.*;
import by.langvest.plantopia.block.special.*;
import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.meta.property.PlantopiaBlockDropType;
import by.langvest.plantopia.meta.property.PlantopiaBlockHeightType;
import by.langvest.plantopia.registry.PlantopiaRegistries;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaBlockLootTableSubProvider extends BlockLootSubProvider {
    private static PlantopiaBlockLootTableSubProvider self;

    protected PlantopiaBlockLootTableSubProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    public static final LootItemConditionalFunction.Builder<?> EXPLOSION_DECAY = ApplyExplosionDecay.explosionDecay();
    public static final LootItemCondition.Builder SURVIVES_EXPLOSION = ExplosionCondition.survivesExplosion();
    public static final LootItemCondition.Builder HAS_SHEARS = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS));
    public static final LootItemCondition.Builder HAS_SHEARS_OR_SILK_TOUCH = HAS_SHEARS.or(HAS_SILK_TOUCH);
    public static final LootItemCondition.Builder HAS_NO_SHEARS_OR_SILK_TOUCH = HAS_SHEARS_OR_SILK_TOUCH.invert();
    public static final float SEEDS_CHANCE = 0.125F;
    public static final Pair<Property<DoubleBlockHalf>, DoubleBlockHalf> DOUBLE_BLOCK_HALF_LOWER = Pair.of(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER);
    public static final Pair<Property<DoubleBlockHalf>, DoubleBlockHalf> DOUBLE_BLOCK_HALF_UPPER = Pair.of(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER);
    public static final Pair<Property<PlantopiaTripleBlockHalf>, PlantopiaTripleBlockHalf> TRIPLE_BLOCK_HALF_LOWER = Pair.of(PlantopiaBlockStateProperties.TRIPLE_BLOCK_HALF, PlantopiaTripleBlockHalf.LOWER);
    public static final Pair<Property<PlantopiaTripleBlockHalf>, PlantopiaTripleBlockHalf> TRIPLE_BLOCK_HALF_CENTRAL = Pair.of(PlantopiaBlockStateProperties.TRIPLE_BLOCK_HALF, PlantopiaTripleBlockHalf.CENTRAL);
    public static final Pair<Property<PlantopiaTripleBlockHalf>, PlantopiaTripleBlockHalf> TRIPLE_BLOCK_HALF_UPPER = Pair.of(PlantopiaBlockStateProperties.TRIPLE_BLOCK_HALF, PlantopiaTripleBlockHalf.UPPER);
    public static final Pair<Property<PlantopiaQuarter>, PlantopiaQuarter> QUARTER_SOUTH_WEST = Pair.of(PlantopiaBlockStateProperties.QUARTER, PlantopiaQuarter.SOUTH_WEST);
    public static final Pair<Property<PlantopiaQuarter>, PlantopiaQuarter> QUARTER_WEST_NORTH = Pair.of(PlantopiaBlockStateProperties.QUARTER, PlantopiaQuarter.WEST_NORTH);
    public static final Pair<Property<PlantopiaQuarter>, PlantopiaQuarter> QUARTER_NORTH_EAST = Pair.of(PlantopiaBlockStateProperties.QUARTER, PlantopiaQuarter.NORTH_EAST);
    public static final Pair<Property<PlantopiaQuarter>, PlantopiaQuarter> QUARTER_EAST_SOUTH = Pair.of(PlantopiaBlockStateProperties.QUARTER, PlantopiaQuarter.EAST_SOUTH);
    public static final Set<Block> EXPLOSION_RESISTANT_BLOCKS = Sets.newHashSet();

    @Override
    protected void generate() {
        setSelf(this);
        generateAll();

        add(PlantopiaBlocks.GIANT_GRASS.get(), block -> createTriplePlantWithSeedDrops(block, Blocks.GRASS, Items.WHEAT_SEEDS));
        add(PlantopiaBlocks.GIANT_FERN.get(), block -> createTriplePlantWithSeedDrops(block, Blocks.FERN, Items.WHEAT_SEEDS));
        add(PlantopiaBlocks.TALL_DUNE_GRASS.get(), block -> createDoublePlantShearedDrops(block, PlantopiaBlocks.DUNE_GRASS.get()));
        add(PlantopiaBlocks.WITCHY_TOADSTOOL_BLOCK.get(), block -> createMushroomBlockDrop(block, PlantopiaBlocks.WITCHY_TOADSTOOL.get()));
        add(PlantopiaBlocks.BIRCH_BASE_LOG.get(), block -> createBirchBaseDrops(block, Blocks.BIRCH_LOG));
        add(PlantopiaBlocks.BIRCH_BASE_WOOD.get(), block -> createBirchBaseDrops(block, Blocks.BIRCH_WOOD));
        add(PlantopiaBlocks.CLOVER.get(), PlantopiaBlockLootTableSubProvider::createCloverDrops);
        add(PlantopiaBlocks.AZOLLA.get(), PlantopiaBlockLootTableSubProvider::createAzollaDrops);
        add(PlantopiaBlocks.COBBLESTONE_SHARD.get(), PlantopiaBlockLootTableSubProvider::createCobblestoneShardDrops);
        add(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD.get(), PlantopiaBlockLootTableSubProvider::createCobblestoneShardDrops);
        add(PlantopiaBlocks.COBBLESTONE_SHARD_PET.get(), PlantopiaBlockLootTableSubProvider::createCobblestoneShardPetDrops);
        add(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD_PET.get(), PlantopiaBlockLootTableSubProvider::createCobblestoneShardPetDrops);
        add(PlantopiaBlocks.POLLINATED_DANDELION.get(), PlantopiaBlockLootTableSubProvider::createPollinatedDandelionDrops);
        add(PlantopiaBlocks.BRANCHING_SHRUB.get(), PlantopiaBlockLootTableSubProvider::createBranchingShrubDrops);
        add(PlantopiaBlocks.INFESTED_DIRT.get(), PlantopiaBlockLootTableSubProvider::createInfestedDirtDrops);
        add(PlantopiaBlocks.INFESTED_GRASS_BLOCK.get(), PlantopiaBlockLootTableSubProvider::createInfestedDirtDrops);
        add(PlantopiaBlocks.BIG_PLATTERLEAF.get(), PlantopiaBlockLootTableSubProvider::createBigPlatterleafDrops);
        add(PlantopiaBlocks.COVERED_SNOWDROP.get(), PlantopiaBlockLootTableSubProvider::createCoveredSnowdropDrops);
        add(PlantopiaBlocks.WHITE_LUCKY_DAISY.get(), PlantopiaBlockLootTableSubProvider::createLuckyDaisyDrops);
        add(PlantopiaBlocks.PINK_LUCKY_DAISY.get(), PlantopiaBlockLootTableSubProvider::createLuckyDaisyDrops);
        add(PlantopiaBlocks.CARROTWEED.get(), PlantopiaBlockLootTableSubProvider::createCarrotweedDrops);
    }

    private static void setSelf(PlantopiaBlockLootTableSubProvider self) {
        PlantopiaBlockLootTableSubProvider.self = self;
    }

    private void generateAll() {
        var workScheduler = Plantopia.getPlatform().getWorkScheduler();

        workScheduler.executeWork("block_loot_table_datagen");

        PlantopiaMetaBuckets.BLOCK.forEach(blockMeta -> {
            if (!blockMeta.shouldGenerateLootTable()) return;

            var dropType = blockMeta.getDropType();

            if (dropType == PlantopiaBlockDropType.SELF) {
                dropSelf(blockMeta);
                return;
            }

            if (dropType == PlantopiaBlockDropType.SELF_BY_SHEARS) {
                dropSelfByShears(blockMeta);
                return;
            }

            if (dropType == PlantopiaBlockDropType.SELF_BY_SILK_TOUCH) {
                dropSelfBySilkTouch(blockMeta);
                return;
            }

            generatedDrops(blockMeta);
        });
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return PlantopiaMetaBuckets.BLOCK.getAll().stream().filter(PlantopiaBlockMeta::hasDrop).map(PlantopiaBlockMeta::get)::iterator;
    }

    public static void addTable(Block block, Function<Block, LootTable.Builder> factory) {
        self.add(block, factory);
    }

    public static void addTable(Block block, LootTable.Builder builder) {
        self.add(block, builder);
    }

    /* DROPS GENERATION ******************************************/

    public static void generatedDrops(@NotNull PlantopiaBlockMeta blockMeta) {
        var block = blockMeta.get();
        var type = blockMeta.getType();

        if (block instanceof DoorBlock) {
            addTable(block, self::createDoorTable);
            return;
        }

        if (block instanceof PlantopiaLeafLitterBlock) {
            addTable(block, PlantopiaBlockLootTableSubProvider::createLeafLitterDrops);
            return;
        }

        if (block instanceof PlantopiaFloweringWaterlilyBlock) {
            addTable(block, PlantopiaBlockLootTableSubProvider::createFloweringWaterlilyDrops);
            return;
        }

        if (block instanceof PlantopiaSeaShellBlock) {
            addTable(block, PlantopiaBlockLootTableSubProvider::createSeaShellDrops);
            return;
        }

        if (block instanceof PlantopiaHangingMossBlock) {
            addTable(block, PlantopiaBlockLootTableSubProvider::createHangingMossDrops);
            return;
        }

        if (block instanceof FlowerPotBlock) {
            self.dropPottedContents(block);
            return;
        }

        if (block instanceof AbstractCauldronBlock) {
            generatedCauldronDrops(blockMeta);
            return;
        }

        if (type.instanceOf(MetaType.LEAVES)) {
            generatedLeavesDrops(blockMeta);
            return;
        }

        dropSelf(blockMeta);
    }

    public static void dropSelf(@NotNull PlantopiaBlockMeta blockMeta) {
        var block = blockMeta.get();
        var type = blockMeta.getType();
        int baseHeight = blockMeta.getBlockHeightType().getBaseHeight();
        int baseWidth = blockMeta.getBlockWidthType().getBaseWidth();

        if (baseHeight == 1 && baseWidth == 1) {
            self.dropSelf(block);
            return;
        }

        if (!type.isSimplePlantLike()) {
            dropSelfIf(block, hasLowerHalfProperty(block, blockMeta.getBlockHeightType()));
            return;
        }

        var lootEntry = withSurvivesExplosionCondition(block, item(block));

        addTable(block, createTable(blockMeta, lootEntry));
    }

    public static void dropSelfByShears(@NotNull PlantopiaBlockMeta blockMeta) {
        var block = blockMeta.get();
        var type = blockMeta.getType();
        var blockHeightType = blockMeta.getBlockHeightType();
        int baseHeight = blockHeightType.getBaseHeight();

        if (baseHeight == 1) {
            dropSelfByShears(block);
            return;
        }

        if (!type.instanceOf(MetaType.PLANT)) {
            dropSelfByShearsIf(block, hasLowerHalfProperty(block, blockHeightType));
            return;
        }

        var lootEntry = LootItem.lootTableItem(block).when(HAS_SHEARS);

        addTable(block, createTable(blockMeta, lootEntry));
    }

    public static void dropSelfBySilkTouch(@NotNull PlantopiaBlockMeta blockMeta) {
        var block = blockMeta.get();
        var lootEntry = LootItem.lootTableItem(block).when(HAS_SILK_TOUCH);

        addTable(block, createTable(blockMeta, lootEntry));
    }

    public static void generatedLeavesDrops(@NotNull PlantopiaBlockMeta blockMeta) {
        String name = blockMeta.getName();
        String saplingName = name.replace("_leaves", "_sapling");
        var block = blockMeta.get();
        var saplingBlock = PlantopiaRegistries.BLOCK.getValueOrThrow(plantopia(saplingName));

        addTable(block, createLeavesWithSaplingDrops(block, saplingBlock.get(), NORMAL_LEAVES_SAPLING_CHANCES));
    }

    public static void generatedCauldronDrops(@NotNull PlantopiaBlockMeta blockMeta) {
        var block = blockMeta.get();
        var lootEntry = withSurvivesExplosionCondition(block, item(Items.CAULDRON));

        addTable(block, createTable(blockMeta, lootEntry));
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

    public static void dropSelfIf(Block block, LootItemCondition.Builder condition) {
        LootTable.Builder lootTable = LootTable.lootTable()
            .withPool(
                withSurvivesExplosionCondition(block, LootPool.lootPool())
                    .add(item(block).when(condition))
            );

        addTable(block, lootTable);
    }

    public static void dropSelfByShears(Block block) {
        LootTable.Builder lootTable = LootTable.lootTable()
            .withPool(
                LootPool.lootPool()
                    .add(item(block))
                    .when(HAS_SHEARS)
            );

        addTable(block, lootTable);
    }

    public static void dropSelfByShearsIf(Block block, LootItemCondition.Builder condition) {
        LootTable.Builder lootTable = LootTable.lootTable()
            .withPool(
                LootPool.lootPool()
                    .add(item(block).when(condition))
                    .when(HAS_SHEARS)
            );

        addTable(block, lootTable);
    }

    /* CUSTOM DROPS ******************************************/

    public static LootTable.@NotNull Builder createTriplePlantWithSeedDrops(Block block, Block sheared, Item seeds) {
        LootPoolEntryContainer.Builder<?> lootEntry = item(sheared)
            .apply(setCount(3))
            .when(HAS_SHEARS)
            .otherwise(
                withSurvivesExplosionCondition(block, item(seeds))
                    .when(randomChance(SEEDS_CHANCE))
            );

        return createTripleHighPlantTable(block, lootEntry);
    }

    public static LootTable.@NotNull Builder createBirchBaseDrops(Block block, Block simpleBirch) {
        LootPoolEntryContainer.Builder<?> lootEntry = item(block)
            .when(HAS_SILK_TOUCH)
            .otherwise(
                withSurvivesExplosionCondition(block, item(simpleBirch))
            );

        return createBlockTable(block, lootEntry);
    }

    public static LootTable.@NotNull Builder createLeavesWithSaplingDrops(Block block, Block sapling, float... chances) {
        return self.createLeavesDrops(block, sapling, chances);
    }

    public static LootTable.@NotNull Builder createDoublePlantShearedDrops(Block block, Block sheared) {
        LootPoolEntryContainer.Builder<?> lootEntry = item(sheared)
            .apply(setCount(2))
            .when(HAS_SHEARS);

        return createDoubleHighPlantTable(block, lootEntry);
    }

    public static LootTable.@NotNull Builder createCloverDrops(Block block) {
        LootPoolEntryContainer.Builder<?> lootEntry = createPartialLootEntry(block, PlantopiaCloverBlock.AMOUNT)
            .when(HAS_SHEARS)
            .otherwise(
                withSurvivesExplosionCondition(block, item(Items.WHEAT_SEEDS))
                    .when(randomChance(SEEDS_CHANCE * 0.75F))
            );

        return createBlockTable(block, lootEntry);
    }

    public static LootTable.@NotNull Builder createCarrotweedDrops(Block block) {
        LootPoolEntryContainer.Builder<?> lootEntry = item(block)
            .when(HAS_SHEARS)
            .otherwise(
                withExplosionDecayFunction(
                    block,
                    withSurvivesExplosionCondition(block, item(Items.CARROT))
                        .when(randomChance(SEEDS_CHANCE * 0.5F))
                )
            );

        return createDoubleHighPlantTable(block, lootEntry);
    }

    public static LootTable.@NotNull Builder createAzollaDrops(Block block) {
        LootPoolEntryContainer.Builder<?> lootEntry = createPartialLootEntry(block, PlantopiaBlockStateProperties.SEGMENT_AMOUNT)
            .when(HAS_SHEARS_OR_SILK_TOUCH);

        return createBlockTable(block, lootEntry);
    }

    public static LootTable.@NotNull Builder createLeafLitterDrops(Block block) {
        LootPoolEntryContainer.Builder<?> lootEntry = createPartialLootEntry(block, PlantopiaBlockStateProperties.SEGMENT_AMOUNT);

        return createBlockTable(block, lootEntry);
    }

    public static LootTable.@NotNull Builder createCobblestoneShardDrops(Block block) {
        LootPoolEntryContainer.Builder<?> lootEntry = withExplosionDecayFunction(block, createPartialLootEntry(block, PlantopiaCobblestoneShardBlock.AMOUNT));

        return createSurvivedExplosionBlockTable(block, lootEntry);
    }

    public static LootTable.@NotNull Builder createCobblestoneShardPetDrops(Block block) {
        var originalBlock = ((PlantopiaCobblestoneShardPetBlock) block).getOriginBlock();

        return self.createNameableBlockEntityTable(originalBlock);
    }

    public static LootTable.@NotNull Builder createPollinatedDandelionDrops(Block block) {
        return createSurvivedExplosionBlockTable(block, item(Blocks.DANDELION));
    }

    public static LootTable.@NotNull Builder createBranchingShrubDrops(Block block) {
        LootPoolEntryContainer.Builder<?> lootEntry = item(PlantopiaBlocks.BRANCHING_SHRUB.get())
            .when(HAS_SHEARS)
            .otherwise(
                withExplosionDecayFunction(
                    block,
                    withSurvivesExplosionCondition(block, item(Items.STICK))
                        .when(randomChance(SEEDS_CHANCE * 2))
                        .apply(setCount(1, 2))
                )
            );

        return createBlockTable(block, lootEntry);
    }

    public static LootTable.@NotNull Builder createInfestedDirtDrops(Block block) {
        LootPoolEntryContainer.Builder<?> lootEntry = item(block)
            .when(HAS_SILK_TOUCH)
            .otherwise(
                withSurvivesExplosionCondition(block, item(Blocks.DIRT))
            );

        return createBlockTable(block, lootEntry);
    }

    public static LootTable.@NotNull Builder createBigPlatterleafDrops(Block block) {
        LootPoolEntryContainer.Builder<?> lootEntry = item(block)
            .when(HAS_SILK_TOUCH)
            .otherwise(
                withSurvivesExplosionCondition(block, item(PlantopiaItems.SMALL_PLATTERLEAF.get()))
                    .apply(setCount(4))
            );

        return createWidePlantTable(block, lootEntry);
    }

    public static LootTable.@NotNull Builder createCoveredSnowdropDrops(Block block) {
        PlantopiaCoveredSnowdropBlock coveredSnowdropBlock = (PlantopiaCoveredSnowdropBlock) block;

        LootPoolEntryContainer.Builder<?> lootEntry = withSurvivesExplosionCondition(block, item(coveredSnowdropBlock.getFlowerBlock()));

        return createBlockTable(block, lootEntry);
    }

    public static LootTable.@NotNull Builder createSeaShellDrops(Block block) {
        LootPoolEntryContainer.Builder<?> lootEntry = item(block)
            .apply(
                CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                    .copy("Color", "BlockEntityTag.Color")
            );

        return createBlockTable(block, lootEntry);
    }

    public static LootTable.@NotNull Builder createHangingMossDrops(Block block) {
        LootPoolEntryContainer.Builder<?> lootEntry = item(block)
            .when(HAS_SHEARS_OR_SILK_TOUCH);

        return createBlockTable(block, lootEntry);
    }

    public static LootTable.@NotNull Builder createLuckyDaisyDrops(Block block) {
        LootPoolEntryContainer.Builder<?> lootEntry = item(block)
            .apply(
                CopyBlockState.copyState(block)
                    .copy(PlantopiaLuckyDaisyBlock.AMOUNT)
            );

        return createBlockTable(block, lootEntry);
    }

    public static LootTable.@NotNull Builder createFloweringWaterlilyDrops(Block block) {
        var floweringWaterlilyBlock = ((PlantopiaFloweringWaterlilyBlock) block);

        LootPoolEntryContainer.Builder<?> flowerLootEntry = withSurvivesExplosionCondition(block, item(floweringWaterlilyBlock.getFlowerBlock()));
        LootPoolEntryContainer.Builder<?> lilyPadLootEntry = withSurvivesExplosionCondition(block, item(floweringWaterlilyBlock.getOriginBlock()));

        return LootTable.lootTable()
            .withPool(LootPool.lootPool().add(flowerLootEntry))
            .withPool(LootPool.lootPool().add(lilyPadLootEntry));
    }

    /* HELPER METHODS ******************************************/

    public static LootPoolSingletonContainer.@NotNull Builder<?> item(ItemLike item) {
        return LootItem.lootTableItem(item);
    }

    public static LootItemConditionalFunction.@NotNull Builder<?> setCount(int count) {
        return SetItemCountFunction.setCount(ConstantValue.exactly(count));
    }

    public static LootItemConditionalFunction.@NotNull Builder<?> setCount(int from, int to) {
        return SetItemCountFunction.setCount(UniformGenerator.between(from, to));
    }

    public static LootItemConditionalFunction.@NotNull Builder<?> limitCount(IntRange range) {
        return LimitCount.limitCount(range);
    }

    public static LootItemCondition.@NotNull Builder randomChance(float chance) {
        return LootItemRandomChanceCondition.randomChance(chance);
    }

    public static <T extends Comparable<T> & StringRepresentable, P extends Property<T>> LootItemBlockStatePropertyCondition.@NotNull Builder hasProperty(Block block, @NotNull Pair<P, T> property) {
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(
            StatePropertiesPredicate.Builder.properties().hasProperty(property.getFirst(), property.getSecond())
        );
    }

    public static <P extends Property<Integer>> LootItemBlockStatePropertyCondition.@NotNull Builder hasProperty(Block block, P property, Integer value) {
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(
            StatePropertiesPredicate.Builder.properties().hasProperty(property, value)
        );
    }

    public static LootItemCondition.@NotNull Builder hasLowerHalfProperty(Block block, PlantopiaBlockHeightType blockHeightType) {
        if (blockHeightType == PlantopiaBlockHeightType.TRIPLE) return hasProperty(block, TRIPLE_BLOCK_HALF_LOWER);
        return hasProperty(block, DOUBLE_BLOCK_HALF_LOWER);
    }

    public static <T extends Comparable<T> & StringRepresentable> LootItemCondition.@NotNull Builder checkPropertyAt(Block block, @NotNull Pair<Property<T>, T> property, BlockPos pos) {
        return LocationCheck.checkLocation(
            LocationPredicate.Builder.location().setBlock(
                BlockPredicate.Builder.block().of(block).setProperties(
                    StatePropertiesPredicate.Builder.properties().hasProperty(property.getFirst(), property.getSecond()).build()
                ).build()
            ), pos
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull BlockPos x(int offset) {
        return new BlockPos(offset, 0, 0);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull BlockPos y(int offset) {
        return new BlockPos(0, offset, 0);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull BlockPos z(int offset) {
        return new BlockPos(0, 0, offset);
    }

    public static <T extends FunctionUserBuilder<T>> T withExplosionDecayFunction(@NotNull Block block, FunctionUserBuilder<T> function) {
        return EXPLOSION_RESISTANT_BLOCKS.contains(block) ? function.unwrap() : function.apply(EXPLOSION_DECAY);
    }

    public static <T extends ConditionUserBuilder<T>> T withSurvivesExplosionCondition(@NotNull Block block, ConditionUserBuilder<T> condition) {
        return EXPLOSION_RESISTANT_BLOCKS.contains(block) ? condition.unwrap() : condition.when(SURVIVES_EXPLOSION);
    }

    /* LOOT ENTRY PATTERNS ******************************************/

    public static LootPoolSingletonContainer.@NotNull Builder<?> createPartialLootEntry(Block block, IntegerProperty property) {
        return createPartialLootEntry(block, block, property);
    }

    public static LootPoolSingletonContainer.@NotNull Builder<?> createPartialLootEntry(Block block, ItemLike drop, IntegerProperty property) {
        LootPoolSingletonContainer.Builder<?> lootEntry = item(drop);
        if (property != null) for (Integer value : property.getPossibleValues()) {
            if (value == 1) continue;
            lootEntry.apply(setCount(value).when(hasProperty(block, property, value)));
        }
        return lootEntry;
    }

    /* LOOT TABLE PATTERNS ******************************************/

    public static LootTable.@NotNull Builder createTable(@NotNull PlantopiaBlockMeta blockMeta, LootPoolEntryContainer.Builder<?> @NotNull ... lootEntries) {
        var block = blockMeta.get();
        var type = blockMeta.getType();
        int baseHeight = blockMeta.getBlockHeightType().getBaseHeight();
        int baseWidth = blockMeta.getBlockWidthType().getBaseWidth();

        if (baseHeight == 1 && baseWidth == 2) {
            if (type.instanceOf(MetaType.PLANT)) return createWidePlantTable(block, lootEntries);
            // return createWideBlockTable(block, lootEntries);
        }

        if (baseHeight == 2 && baseWidth == 1) {
            if (type.instanceOf(MetaType.PLANT)) return createDoubleHighPlantTable(block, lootEntries);
            return createDoubleHighBlockTable(block, lootEntries);
        }

        if (baseHeight == 3 && baseWidth == 1) {
            if (type.instanceOf(MetaType.PLANT)) return createTripleHighPlantTable(block, lootEntries);
            return createTripleHighBlockTable(block, lootEntries);
        }

        if (baseHeight == 3 && baseWidth == 2) {
            if (type.instanceOf(MetaType.PLANT)) return createWideTripleHighPlantTable(block, lootEntries);
            // return createWideTripleHighBlockTable(block, lootEntries);
        }

        return createBlockTable(block, lootEntries);
    }

    public static LootTable.@NotNull Builder createBlockTable(@SuppressWarnings("unused") Block block, LootPoolEntryContainer.Builder<?> @NotNull ... lootEntries) {
        LootPool.Builder lootPool = LootPool.lootPool();
        for (LootPoolEntryContainer.Builder<?> lootEntry : lootEntries) lootPool.add(lootEntry);
        return LootTable.lootTable().withPool(lootPool);
    }

    public static LootTable.@NotNull Builder createSurvivedExplosionBlockTable(Block block, LootPoolEntryContainer.Builder<?> @NotNull ... lootEntries) {
        LootPool.Builder lootPool = withSurvivesExplosionCondition(block, LootPool.lootPool());
        for (LootPoolEntryContainer.Builder<?> lootEntry : lootEntries) lootPool.add(lootEntry);
        return LootTable.lootTable().withPool(lootPool);
    }

    public static LootTable.@NotNull Builder createDoubleHighBlockTable(Block block, LootPoolEntryContainer.Builder<?> @NotNull ... lootEntries) {
        return createLowerBlockTable(block, DOUBLE_BLOCK_HALF_LOWER, lootEntries);
    }

    public static LootTable.@NotNull Builder createTripleHighBlockTable(Block block, LootPoolEntryContainer.Builder<?> @NotNull ... lootEntries) {
        return createLowerBlockTable(block, TRIPLE_BLOCK_HALF_LOWER, lootEntries);
    }

    public static <T extends Comparable<T> & StringRepresentable> LootTable.@NotNull Builder createLowerBlockTable(Block block, Pair<Property<T>, T> property, LootPoolEntryContainer.Builder<?> @NotNull ... lootEntries) {
        LootPool.Builder lootPool = LootPool.lootPool();
        for (LootPoolEntryContainer.Builder<?> lootEntry : lootEntries) lootPool.add(lootEntry);
        return LootTable.lootTable().withPool(lootPool.when(hasProperty(block, property)));
    }

    public static LootTable.@NotNull Builder createDoubleHighPlantTable(Block block, LootPoolEntryContainer.Builder<?> @NotNull ... lootEntries) {
        LootPool.Builder lowerLootPool = LootPool.lootPool();
        LootPool.Builder upperLootPool = LootPool.lootPool();

        for (LootPoolEntryContainer.Builder<?> lootEntry : lootEntries) {
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

    public static LootTable.@NotNull Builder createTripleHighPlantTable(Block block, LootPoolEntryContainer.Builder<?> @NotNull ... lootEntries) {
        LootPool.Builder lowerLootPool = LootPool.lootPool();
        LootPool.Builder centralLootPool = LootPool.lootPool();
        LootPool.Builder upperLootPool = LootPool.lootPool();

        for (LootPoolEntryContainer.Builder<?> lootEntry : lootEntries) {
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

    public static LootTable.@NotNull Builder createWideTripleHighPlantTable(Block block, LootPoolEntryContainer.Builder<?> @NotNull ... lootEntries) {
        LootPool.Builder southWestLootPool = LootPool.lootPool();
        LootPool.Builder westNorthLootPool = LootPool.lootPool();
        LootPool.Builder northEastLootPool = LootPool.lootPool();
        LootPool.Builder eastSouthLootPool = LootPool.lootPool();

        List.of(southWestLootPool, westNorthLootPool, northEastLootPool, eastSouthLootPool).forEach(quarterLootPool -> {
            quarterLootPool.add(
                AlternativesEntry.alternatives(lootEntries)
                    .when(hasProperty(block, TRIPLE_BLOCK_HALF_LOWER))
                    .when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_CENTRAL, y(1)))
                    .when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_UPPER, y(2)))
            );

            quarterLootPool.add(
                AlternativesEntry.alternatives(lootEntries)
                    .when(hasProperty(block, TRIPLE_BLOCK_HALF_CENTRAL))
                    .when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_LOWER, y(-1)))
                    .when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_UPPER, y(1)))
            );

            quarterLootPool.add(
                AlternativesEntry.alternatives(lootEntries)
                    .when(hasProperty(block, TRIPLE_BLOCK_HALF_UPPER))
                    .when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_LOWER, y(-2)))
                    .when(checkPropertyAt(block, TRIPLE_BLOCK_HALF_CENTRAL, y(-1)))
            );
        });

        return LootTable.lootTable()
            .withPool(
                southWestLootPool
                    .when(hasProperty(block, QUARTER_SOUTH_WEST))
                    .when(checkPropertyAt(block, QUARTER_WEST_NORTH, z(-1)))
                    .when(checkPropertyAt(block, QUARTER_EAST_SOUTH, x(1)))
            )
            .withPool(
                westNorthLootPool
                    .when(hasProperty(block, QUARTER_WEST_NORTH))
                    .when(checkPropertyAt(block, QUARTER_NORTH_EAST, x(1)))
                    .when(checkPropertyAt(block, QUARTER_SOUTH_WEST, z(1)))
            )
            .withPool(
                northEastLootPool
                    .when(hasProperty(block, QUARTER_NORTH_EAST))
                    .when(checkPropertyAt(block, QUARTER_EAST_SOUTH, z(1)))
                    .when(checkPropertyAt(block, QUARTER_WEST_NORTH, x(-1)))
            )
            .withPool(
                eastSouthLootPool
                    .when(hasProperty(block, QUARTER_EAST_SOUTH))
                    .when(checkPropertyAt(block, QUARTER_SOUTH_WEST, x(-1)))
                    .when(checkPropertyAt(block, QUARTER_NORTH_EAST, z(-1)))
            );
    }

    public static LootTable.@NotNull Builder createWidePlantTable(Block block, LootPoolEntryContainer.Builder<?> @NotNull ... lootEntries) {
        LootPool.Builder southWestLootPool = LootPool.lootPool();
        LootPool.Builder westNorthLootPool = LootPool.lootPool();
        LootPool.Builder northEastLootPool = LootPool.lootPool();
        LootPool.Builder eastSouthLootPool = LootPool.lootPool();

        List.of(southWestLootPool, westNorthLootPool, northEastLootPool, eastSouthLootPool).forEach(quarterLootPool -> {
            for (LootPoolEntryContainer.Builder<?> lootEntry : lootEntries) {
                quarterLootPool.add(lootEntry);
            }
        });

        return LootTable.lootTable()
            .withPool(
                southWestLootPool
                    .when(hasProperty(block, QUARTER_SOUTH_WEST))
                    .when(checkPropertyAt(block, QUARTER_WEST_NORTH, z(-1)))
                    .when(checkPropertyAt(block, QUARTER_EAST_SOUTH, x(1)))
            )
            .withPool(
                westNorthLootPool
                    .when(hasProperty(block, QUARTER_WEST_NORTH))
                    .when(checkPropertyAt(block, QUARTER_NORTH_EAST, x(1)))
                    .when(checkPropertyAt(block, QUARTER_SOUTH_WEST, z(1)))
            )
            .withPool(
                northEastLootPool
                    .when(hasProperty(block, QUARTER_NORTH_EAST))
                    .when(checkPropertyAt(block, QUARTER_EAST_SOUTH, z(1)))
                    .when(checkPropertyAt(block, QUARTER_WEST_NORTH, x(-1)))
            )
            .withPool(
                eastSouthLootPool
                    .when(hasProperty(block, QUARTER_EAST_SOUTH))
                    .when(checkPropertyAt(block, QUARTER_SOUTH_WEST, x(-1)))
                    .when(checkPropertyAt(block, QUARTER_NORTH_EAST, z(-1)))
            );
    }
}