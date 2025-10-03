package by.langvest.plantopia.block;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class PlantopiaBlockStateProperties {
	public static final int INFESTED_DIRT_MAX_AGE = BlockStateProperties.MAX_AGE_25;
	public static final IntegerProperty LEAF_AMOUNT = IntegerProperty.create("leaf_amount", 1, 4);
	public static final IntegerProperty SHARDS = IntegerProperty.create("shards", 1, 4);
	public static final IntegerProperty POLLINATION_COUNT = IntegerProperty.create("pollination_count", 1, 4);
	public static final EnumProperty<PlantopiaQuarter> QUARTER = EnumProperty.create("quarter", PlantopiaQuarter.class);
	public static final EnumProperty<PlantopiaTripleBlockHalf> TRIPLE_BLOCK_HALF = EnumProperty.create("half", PlantopiaTripleBlockHalf.class);
	public static final IntegerProperty INFESTED_DIRT_AGE = BlockStateProperties.AGE_25;
}
