package by.langvest.plantopia.adv;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.adv.special.PlantopiaSimpleAdvancement;
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
	public static final RegistryObject<PlantopiaSimpleAdvancement> ROOT = registerAdvancement("root", PlantopiaSimpleAdvancement::new, MetaProperties.of(MetaType.ROOT).group(Plantopia.MOD_ID).background("dirt").icon(PlantopiaBlocks.FIREWEED));
	public static final RegistryObject<PlantopiaSimpleAdvancement> COLLECT_ALL_FLOWERS = registerAdvancement("collect_all_flowers", PlantopiaSimpleAdvancement::new, MetaProperties.of(MetaType.CHALLENGE).parent(ROOT).icon(PlantopiaItems.FLOWERS_ICON));
	public static final RegistryObject<PlantopiaSimpleAdvancement> PLACE_HOGWEED = registerAdvancement("place_hogweed", PlantopiaSimpleAdvancement::new, MetaProperties.of(MetaType.CHILD).parent(ROOT).icon(PlantopiaBlocks.HOGWEED));
	public static final RegistryObject<PlantopiaSimpleAdvancement> PLACE_COBBLESTONE_SHARD_PET = registerAdvancement("place_cobblestone_shard_pet", PlantopiaSimpleAdvancement::new, MetaProperties.of(MetaType.CHILD).parent(ROOT).hidden().icon(PlantopiaBlocks.COBBLESTONE_SHARD));
	public static final RegistryObject<PlantopiaSimpleAdvancement> WALK_ON_QUICKSAND_WITH_LEATHER_BOOTS = registerAdvancement("walk_on_quicksand_with_leather_boots", PlantopiaSimpleAdvancement::new, MetaProperties.of(MetaType.CHILD).parent(ROOT).icon(Items.LEATHER_BOOTS));
	public static final RegistryObject<PlantopiaSimpleAdvancement> PLUCK_LUCKY_DAISY_PETAL = registerAdvancement("pluck_lucky_daisy_petal", PlantopiaSimpleAdvancement::new, MetaProperties.of(MetaType.CHILD).parent(COLLECT_ALL_FLOWERS).icon(PlantopiaBlocks.WHITE_LUCKY_DAISY));
	public static final RegistryObject<PlantopiaSimpleAdvancement> OBTAIN_TANSY = registerAdvancement("obtain_tansy", PlantopiaSimpleAdvancement::new, MetaProperties.of(MetaType.CHILD).parent(ROOT).icon(PlantopiaBlocks.TANSY).hidden());

	public static RegistryObject<PlantopiaSimpleAdvancement> registerAdvancement(String name, @NotNull Supplier<PlantopiaSimpleAdvancement> supplier, @NotNull MetaProperties metaProperties) {
		return registerAdvancement(plantopia(name), supplier, metaProperties);
	}

	public static <T extends PlantopiaAdvancement> RegistryObject<T> registerAdvancement(ResourceLocation identifier, @NotNull Supplier<T> supplier, @NotNull MetaProperties metaProperties) {
		PlantopiaMetaBuckets.ADVANCEMENT.associate(identifier, new PlantopiaAdvancementMeta(identifier, metaProperties));

		return PlantopiaRegistries.ADVANCEMENT.register(identifier, () -> {
			var advancement = supplier.get();
			advancement.bindLocation(identifier);
			return advancement;
		});
	}
}