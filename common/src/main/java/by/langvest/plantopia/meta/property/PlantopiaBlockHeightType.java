package by.langvest.plantopia.meta.property;

public enum PlantopiaBlockHeightType {
    SINGLE(1),
    DOUBLE(2),
    TRIPLE(3);

    private final int baseHeight;

    PlantopiaBlockHeightType(int baseHeight) {
        this.baseHeight = baseHeight;
    }

    public int getBaseHeight() {
        return baseHeight;
    }
}
