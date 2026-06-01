package by.langvest.plantopia.meta.property;

public enum PlantopiaBlockWidthType {
    SINGLE(1),
    DOUBLE(2);

    private final int baseWidth;

    PlantopiaBlockWidthType(int baseWidth) {
        this.baseWidth = baseWidth;
    }

    public int getBaseWidth() {
        return baseWidth;
    }
}
