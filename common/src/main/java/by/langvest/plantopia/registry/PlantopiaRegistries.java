package by.langvest.plantopia.registry;

import by.langvest.plantopia.adv.special.PlantopiaAdvancement;
import by.langvest.plantopia.entity.PlantopiaBoatType;
import by.langvest.plantopia.worldgen.feature.PlantopiaBlockPlacerType;
import by.langvest.plantopia.worldgen.placement.PlantopiaVerticalAnchorType;
import by.langvest.toolkit.registry.Registry;
import by.langvest.toolkit.registry.SimpleRegistry;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import terrablender.api.Region;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaRegistries {
    public static final Registry<Block> BLOCK = createSimpleRegistry("block");
    public static final Registry<Item> ITEM = createSimpleRegistry("item");
    public static final Registry<SoundEvent> SOUND_EVENT = createSimpleRegistry("sound_event");
    public static final Registry<CreativeModeTab> CREATIVE_MODE_TAB = createSimpleRegistry("creative_mode_tab");
    public static final Registry<EntityType<?>> ENTITY_TYPE = createSimpleRegistry("entity_type");
    public static final Registry<BlockEntityType<?>> BLOCK_ENTITY_TYPE = createSimpleRegistry("block_entity_type");
    public static final Registry<ParticleType<?>> PARTICLE_TYPE = createSimpleRegistry("particle_type");
    public static final Registry<Feature<?>> FEATURE_TYPE = createSimpleRegistry("feature_type");
    public static final Registry<TreeDecoratorType<?>> TREE_DECORATOR_TYPE = createSimpleRegistry("tree_decorator_type");
    public static final Registry<TrunkPlacerType<?>> TRUNK_PLACER_TYPE = createSimpleRegistry("trunk_placer_type");
    public static final Registry<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPE = createSimpleRegistry("foliage_placer_type");
    public static final Registry<BlockStateProviderType<?>> BLOCK_STATE_PROVIDER_TYPE = createSimpleRegistry("block_state_provider_type");
    public static final Registry<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPE = createSimpleRegistry("placement_modifier_type");
    public static final Registry<PlantopiaBlockPlacerType<?>> BLOCK_PLACER_TYPE = createSimpleRegistry("block_placer_type");
    public static final Registry<PlantopiaVerticalAnchorType<?>> VERTICAL_ANCHOR_TYPE = createSimpleRegistry("vertical_anchor_type");
    public static final Registry<PlantopiaAdvancement> ADVANCEMENT = createSimpleRegistry("advancement");
    public static final Registry<RecipeSerializer<?>> RECIPE_SERIALIZER = createSimpleRegistry("recipe_serializer");
    public static final Registry<PlantopiaBoatType> BOAT_TYPE = createSimpleRegistry("boat_type");
    public static final Registry<Region> REGION = createSimpleRegistry("region");
    public static final Registry<CriterionTrigger<?>> TRIGGER_TYPE = createSimpleRegistry("criterion_trigger");

    @Contract("_ -> new")
    private static <T> @NotNull SimpleRegistry<T> createSimpleRegistry(String name) {
        return new SimpleRegistry<>(ResourceKey.createRegistryKey(plantopia(name)));
    }
}
