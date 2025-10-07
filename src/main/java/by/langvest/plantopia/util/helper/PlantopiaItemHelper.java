package by.langvest.plantopia.util.helper;

import by.langvest.plantopia.item.special.PlantopiaTripleHighBlockItem;
import by.langvest.plantopia.item.special.PlantopiaWideTripleHighBlockItem;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Function;

public final class PlantopiaItemHelper {
	public static @NotNull Function<Item.Properties, BlockItem> getBlockItemFactory(@NotNull PlantopiaBlockMeta blockMeta) {
		int height = blockMeta.getBlockHeightType().getBaseHeight();
		int width = blockMeta.getBlockWidthType().getBaseWidth();

		if(height == 1) return properties -> new BlockItem(blockMeta.get(), properties);
		if(height == 2) return properties -> new DoubleHighBlockItem(blockMeta.get(), properties);
		if(height == 3 && width == 1) return properties -> new PlantopiaTripleHighBlockItem(blockMeta.get(), properties);
		if(height == 3 && width == 2) return properties -> new PlantopiaWideTripleHighBlockItem(blockMeta.get(), properties);
		return properties -> new BlockItem(blockMeta.get(), properties);
	}

	@Nullable
	public static CompoundTag getBlockStateData(@NotNull ItemStack itemStack) {
		return itemStack.getTagElement("BlockStateTag");
	}

	public static void setBlockStateData(@NotNull ItemStack itemStack, BlockState state, Property<?>... properties) {
		if(state == null) return;

		CompoundTag tag = itemStack.getOrCreateTag();
		CompoundTag newTag;

		if(tag.contains("BlockStateTag", 10)) {
			newTag = tag.getCompound("BlockStateTag");
		} else {
			newTag = new CompoundTag();
			tag.put("BlockStateTag", newTag);
		}

		Arrays.stream(properties).filter(state::hasProperty).forEach((property) -> {
			newTag.putString(property.getName(), serializeState(state, property));
		});
	}

	@Nullable
	public static BlockState getBlockStateFrom(@NotNull ItemStack itemStack) {
		var item = itemStack.getItem();

		if(!(item instanceof BlockItem blockItem)) return null;

		var newState = blockItem.getBlock().defaultBlockState();
		var blockStateTag = getBlockStateData(itemStack);

		if(blockStateTag != null) {
			StateDefinition<Block, BlockState> statedefinition = blockItem.getBlock().getStateDefinition();

			for(String key : blockStateTag.getAllKeys()) {
				Property<?> property = statedefinition.getProperty(key);

				if(property == null) continue;

				var valueName = blockStateTag.get(key);

				if(valueName == null) continue;

				newState = updateState(newState, property, valueName.getAsString());
			}
		}

		return newState;
	}

	private static <T extends Comparable<T>> @NotNull String serializeState(@NotNull BlockState state, @NotNull Property<T> property) {
		T value = state.getValue(property);
		return property.getName(value);
	}

	private static <T extends Comparable<T>> BlockState updateState(@NotNull BlockState state, @NotNull Property<T> property, String valueName) {
		return property.getValue(valueName).map((value) -> state.setValue(property, value)).orElse(state);
	}
}
