package by.langvest.plantopia.adv;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.object.PlantopiaAdvancementMeta;
import by.langvest.plantopia.meta.object.PlantopiaAdvancementMeta.MetaProperties;
import by.langvest.plantopia.meta.object.PlantopiaAdvancementMeta.MetaType;
import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaAdvancements {
	public static final RegistryObject<PlantopiaAdvancement> ROOT = registerAdvancement("root",PlantopiaAdvancement::new, MetaProperties.of(MetaType.ROOT).group(Plantopia.MOD_ID).background("dirt").icon(PlantopiaBlocks.FIREWEED));
	public static final RegistryObject<PlantopiaAdvancement> COLLECT_ALL_FLOWERS = registerAdvancement("collect_all_flowers", PlantopiaAdvancement::new, MetaProperties.of(MetaType.CHALLENGE).parent(ROOT).icon(PlantopiaItems.FLOWERS_ICON));
	public static final RegistryObject<PlantopiaAdvancement> PLACE_HOGWEED = registerAdvancement("place_hogweed", PlantopiaAdvancement::new, MetaProperties.of(MetaType.CHILD).parent(ROOT).icon(PlantopiaBlocks.HOGWEED));
	public static final RegistryObject<PlantopiaAdvancement> PLACE_COBBLESTONE_SHARD_PET = registerAdvancement("place_cobblestone_shard_pet", PlantopiaAdvancement::new, MetaProperties.of(MetaType.CHILD).parent(ROOT).hidden().icon(PlantopiaBlocks.COBBLESTONE_SHARD));
	public static final RegistryObject<PlantopiaAdvancement> WALK_ON_QUICKSAND_WITH_LEATHER_BOOTS = registerAdvancement("walk_on_quicksand_with_leather_boots", PlantopiaAdvancement::new, MetaProperties.of(MetaType.CHILD).parent(ROOT).icon(() -> Items.LEATHER_BOOTS));
	public static final RegistryObject<PlantopiaAdvancement> PLUCK_LUCKY_DAISY_PETAL = registerAdvancement("pluck_lucky_daisy_petal", PlantopiaAdvancement::new, MetaProperties.of(MetaType.CHILD).parent(COLLECT_ALL_FLOWERS).icon(PlantopiaBlocks.WHITE_LUCKY_DAISY));

	public static RegistryObject<PlantopiaAdvancement> registerAdvancement(String name, @NotNull Supplier<PlantopiaAdvancement> supplier, @NotNull MetaProperties metaProperties) {
		return registerAdvancement(plantopia(name), supplier, metaProperties);
	}

	public static RegistryObject<PlantopiaAdvancement> registerAdvancement(ResourceLocation identifier, @NotNull Supplier<PlantopiaAdvancement> supplier, @NotNull MetaProperties metaProperties) {
		PlantopiaMetaBuckets.ADVANCEMENT.associate(identifier, new PlantopiaAdvancementMeta(identifier, metaProperties));
		return PlantopiaRegistries.ADVANCEMENT.register(identifier, () -> supplier.get().bindLocation(identifier));
	}
}