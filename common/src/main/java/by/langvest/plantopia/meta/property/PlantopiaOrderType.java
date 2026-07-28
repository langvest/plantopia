package by.langvest.plantopia.meta.property;

public enum PlantopiaOrderType {
    BLOCK,
    BIRCH,
    MAPLE,
    JACARANDA,
    DEADWOOD,
    PALM,
    ICE,
    MUSHROOM,
    PLANT,
    CLOVER,
    COBBLESTONE_SHARD,
    SHELL,
    WET_PLANT,
    SEA_MOSS,
    EXOTIC_PLANT,
    TREE_FRUIT,
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
