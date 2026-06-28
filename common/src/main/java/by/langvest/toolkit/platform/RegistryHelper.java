package by.langvest.toolkit.platform;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatType;
import net.minecraft.util.valueproviders.FloatProviderType;
import net.minecraft.util.valueproviders.IntProviderType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeType;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifierType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.providers.nbt.LootNbtProviderType;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.score.LootScoreProviderType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public abstract class RegistryHelper extends PlatformHelper {
    protected static Map<ResourceLocation, RegistryEntry> knownRegistries;

    public RegistryHelper(Platform platform) {
        super(platform);
    }

    public abstract void registerBrewable(Potion inputPotion, @NotNull ItemLike ingredient, Potion outputPotion);

    public abstract void registerPottable(Block plantBlock, Block pottedBlock);

    public void registerFlammable(Block block, int encouragement, int flammability) {
        FireBlock fireBlock = (FireBlock) Blocks.FIRE;
        fireBlock.setFlammable(block, encouragement, flammability);
    }

    public void registerCompostable(@NotNull ItemLike itemLike, float compostability) {
        ComposterBlock.COMPOSTABLES.put(itemLike.asItem(), compostability);
    }

    public void registerStrippable(Block log, Block stripped) {
        AxeItem.STRIPPABLES = Maps.newHashMap(AxeItem.STRIPPABLES);
        AxeItem.STRIPPABLES.put(log, stripped);
    }

    public void registerFlattenable(Block block, BlockState flattened) {
        ShovelItem.FLATTENABLES = Maps.newHashMap(ShovelItem.FLATTENABLES);
        ShovelItem.FLATTENABLES.put(block, flattened);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<RegistryAdapter<T>> getKnownRegistry(@NotNull ResourceKey<? extends Registry<T>> registryKey) {
        var registries = getKnownRegistries();
        var entry = registries.get(registryKey.location());
        if (entry == null) return Optional.empty();
        return Optional.of((RegistryAdapter<T>) entry.registry());
    }

    public <T> RegistryAdapter<T> getKnownRegistryOrThrow(@NotNull ResourceKey<? extends Registry<T>> registryKey) {
        var registry = getKnownRegistry(registryKey);
        if (registry.isPresent()) return registry.get();
        throw new IllegalStateException("No any known registry found for key " + registryKey);
    }

    public Map<ResourceLocation, RegistryEntry> getKnownRegistries() {
        if (knownRegistries != null) return knownRegistries;
        knownRegistries = Maps.newHashMap();
        addKnownRegistries(knownRegistries);
        return knownRegistries;
    }

    protected void addKnownRegistries(Map<ResourceLocation, RegistryEntry> registries) {
        addBuiltInRegistries(registries);
    }

    protected void addBuiltInRegistries(Map<ResourceLocation, RegistryEntry> registries) {
        getBuiltInRegistries().forEach((builtInRegistry, clazz) -> {
            var location = builtInRegistry.key().location();
            var adapter = new BuiltInRegistryAdapter<>(builtInRegistry);
            registries.putIfAbsent(location, new RegistryEntry(location, clazz, adapter));
        });
    }

    protected static @NotNull Map<Registry<?>, Class<?>> getBuiltInRegistries() {
        Map<Registry<?>, Class<?>> map = Maps.newHashMap();
        add(map, BuiltInRegistries.GAME_EVENT, GameEvent.class);
        add(map, BuiltInRegistries.SOUND_EVENT, SoundEvent.class);
        add(map, BuiltInRegistries.FLUID, Fluid.class);
        add(map, BuiltInRegistries.MOB_EFFECT, MobEffect.class);
        add(map, BuiltInRegistries.BLOCK, Block.class);
        add(map, BuiltInRegistries.ENCHANTMENT, Enchantment.class);
        add(map, BuiltInRegistries.ENTITY_TYPE, EntityType.class);
        add(map, BuiltInRegistries.ITEM, Item.class);
        add(map, BuiltInRegistries.POTION, Potion.class);
        add(map, BuiltInRegistries.PARTICLE_TYPE, ParticleType.class);
        add(map, BuiltInRegistries.BLOCK_ENTITY_TYPE, BlockEntityType.class);
        add(map, BuiltInRegistries.PAINTING_VARIANT, PaintingVariant.class);
        add(map, BuiltInRegistries.CUSTOM_STAT, ResourceLocation.class);
        add(map, BuiltInRegistries.CHUNK_STATUS, ChunkStatus.class);
        add(map, BuiltInRegistries.RULE_TEST, RuleTestType.class);
        add(map, BuiltInRegistries.RULE_BLOCK_ENTITY_MODIFIER, RuleBlockEntityModifierType.class);
        add(map, BuiltInRegistries.POS_RULE_TEST, PosRuleTestType.class);
        add(map, BuiltInRegistries.MENU, MenuType.class);
        add(map, BuiltInRegistries.RECIPE_TYPE, RecipeType.class);
        add(map, BuiltInRegistries.RECIPE_SERIALIZER, RecipeSerializer.class);
        add(map, BuiltInRegistries.ATTRIBUTE, Attribute.class);
        add(map, BuiltInRegistries.POSITION_SOURCE_TYPE, PositionSourceType.class);
        add(map, BuiltInRegistries.COMMAND_ARGUMENT_TYPE, ArgumentTypeInfo.class);
        add(map, BuiltInRegistries.STAT_TYPE, StatType.class);
        add(map, BuiltInRegistries.VILLAGER_TYPE, VillagerType.class);
        add(map, BuiltInRegistries.VILLAGER_PROFESSION, VillagerProfession.class);
        add(map, BuiltInRegistries.POINT_OF_INTEREST_TYPE, PoiType.class);
        add(map, BuiltInRegistries.MEMORY_MODULE_TYPE, MemoryModuleType.class);
        add(map, BuiltInRegistries.SENSOR_TYPE, SensorType.class);
        add(map, BuiltInRegistries.SCHEDULE, Schedule.class);
        add(map, BuiltInRegistries.ACTIVITY, Activity.class);
        add(map, BuiltInRegistries.LOOT_POOL_ENTRY_TYPE, LootPoolEntryType.class);
        add(map, BuiltInRegistries.LOOT_FUNCTION_TYPE, LootItemFunctionType.class);
        add(map, BuiltInRegistries.LOOT_CONDITION_TYPE, LootItemConditionType.class);
        add(map, BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE, LootNumberProviderType.class);
        add(map, BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE, LootNbtProviderType.class);
        add(map, BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE, LootScoreProviderType.class);
        add(map, BuiltInRegistries.FLOAT_PROVIDER_TYPE, FloatProviderType.class);
        add(map, BuiltInRegistries.INT_PROVIDER_TYPE, IntProviderType.class);
        add(map, BuiltInRegistries.HEIGHT_PROVIDER_TYPE, HeightProviderType.class);
        add(map, BuiltInRegistries.BLOCK_PREDICATE_TYPE, BlockPredicateType.class);
        add(map, BuiltInRegistries.CARVER, WorldCarver.class);
        add(map, BuiltInRegistries.FEATURE, Feature.class);
        add(map, BuiltInRegistries.STRUCTURE_PLACEMENT, StructurePlacementType.class);
        add(map, BuiltInRegistries.STRUCTURE_PIECE, StructurePieceType.class);
        add(map, BuiltInRegistries.STRUCTURE_TYPE, StructureType.class);
        add(map, BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, PlacementModifierType.class);
        add(map, BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE, BlockStateProviderType.class);
        add(map, BuiltInRegistries.FOLIAGE_PLACER_TYPE, FoliagePlacerType.class);
        add(map, BuiltInRegistries.TRUNK_PLACER_TYPE, TrunkPlacerType.class);
        add(map, BuiltInRegistries.ROOT_PLACER_TYPE, RootPlacerType.class);
        add(map, BuiltInRegistries.TREE_DECORATOR_TYPE, TreeDecoratorType.class);
        add(map, BuiltInRegistries.FEATURE_SIZE_TYPE, FeatureSizeType.class);
        add(map, BuiltInRegistries.BIOME_SOURCE, Codec.class);
        add(map, BuiltInRegistries.CHUNK_GENERATOR, Codec.class);
        add(map, BuiltInRegistries.MATERIAL_CONDITION, Codec.class);
        add(map, BuiltInRegistries.MATERIAL_RULE, Codec.class);
        add(map, BuiltInRegistries.DENSITY_FUNCTION_TYPE, Codec.class);
        add(map, BuiltInRegistries.STRUCTURE_PROCESSOR, StructureProcessorType.class);
        add(map, BuiltInRegistries.STRUCTURE_POOL_ELEMENT, StructurePoolElementType.class);
        add(map, BuiltInRegistries.CAT_VARIANT, CatVariant.class);
        add(map, BuiltInRegistries.FROG_VARIANT, FrogVariant.class);
        add(map, BuiltInRegistries.BANNER_PATTERN, BannerPattern.class);
        add(map, BuiltInRegistries.INSTRUMENT, Instrument.class);
        add(map, BuiltInRegistries.DECORATED_POT_PATTERNS, String.class);
        add(map, BuiltInRegistries.CREATIVE_MODE_TAB, CreativeModeTab.class);
        return map;
    }

    protected static <T, C extends T> void add(@NotNull Map<Registry<?>, Class<?>> map, Registry<T> registry, Class<? extends C> clazz) {
        map.put(registry, clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<ResourceKey<T>> getResourceKey(T object) {
        var matchedRegistryEntries = getKnownRegistries()
            .values()
            .stream()
            .filter(entry -> entry.type() != null && entry.type().isInstance(object))
            .toList();

        if (matchedRegistryEntries.isEmpty()) {
            return Optional.empty();
        }

        for (var entry : matchedRegistryEntries) {
            var registry = (RegistryAdapter<T>) entry.registry();
            var key = registry.getKey(object);

            if (key.isPresent()) {
                return key;
            }
        }

        return Optional.empty();
    }

    public <T> ResourceKey<T> getResourceKeyOrThrow(T object) {
        var registryName = getResourceKey(object);
        if (registryName.isPresent()) return registryName.get();
        throw new IllegalArgumentException(String.format("Object %s is not registered in any known registry!", object));
    }

    public record RegistryEntry(ResourceLocation location, @Nullable Class<?> type, RegistryAdapter<?> registry) {}
}
