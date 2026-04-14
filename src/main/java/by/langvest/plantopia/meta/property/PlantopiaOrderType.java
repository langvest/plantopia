package by.langvest.plantopia.meta.property;

public enum PlantopiaOrderType {
	BLOCK,
	ICE,
	MUSHROOM,
	PLANT,
	CLOVER,
	COBBLESTONE_SHARD,
	SHELL,
	WET_PLANT,
	SEA_MOSS,
	EXOTIC_PLANT,
	FLOWER,
	FOOD,
	ITEM;

	private static int counter = 0;
	private final int order;

	PlantopiaOrderType() {
		this.order = nextOrder();
	}

	private static int nextOrder() {
		return counter++;
	}

	public int getOrder() {
		return order;
	}
}
