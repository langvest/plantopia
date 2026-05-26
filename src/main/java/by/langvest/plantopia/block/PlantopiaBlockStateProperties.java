package by.langvest.plantopia.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.*;

/**
 * @see net.minecraft.world.level.block.state.properties.BlockStateProperties
 */
public class PlantopiaBlockStateProperties {
	public static final IntegerProperty SEGMENT_AMOUNT = IntegerProperty.create("segment_amount", 1, 4);
	public static final IntegerProperty PETAL_AMOUNT = IntegerProperty.create("petal_amount", 0, 8);
	public static final IntegerProperty SHARDS = IntegerProperty.create("shards", 1, 4);
	public static final IntegerProperty POLLINATION_COUNT = IntegerProperty.create("pollination_count", 1, 4);
	public static final EnumProperty<PlantopiaQuarter> QUARTER = EnumProperty.create("quarter", PlantopiaQuarter.class);
	public static final EnumProperty<PlantopiaTripleBlockHalf> TRIPLE_BLOCK_HALF = EnumProperty.create("half", PlantopiaTripleBlockHalf.class);
	public static final IntegerProperty INFESTED_AGE = BlockStateProperties.AGE_25;
	public static final BooleanProperty BASE = BooleanProperty.create("base");
	public static final BooleanProperty TIP = BooleanProperty.create("tip");
	public static final BooleanProperty FLOATING = BooleanProperty.create("floating");
	public static final DirectionProperty CACTUS_FACING = DirectionProperty.create("facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP);
}
