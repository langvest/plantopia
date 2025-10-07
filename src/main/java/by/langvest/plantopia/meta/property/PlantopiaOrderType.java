package by.langvest.plantopia.meta.property;

public enum PlantopiaOrderType {
	BLOCK,
	PLANT,
	CLOVER,
	COBBLESTONE_SHARD,
	SHELL,
	WET_PLANT,
	EXOTIC_PLANT,
	FLOWER,
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
