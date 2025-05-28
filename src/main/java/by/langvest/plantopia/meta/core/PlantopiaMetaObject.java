package by.langvest.plantopia.meta.core;

public abstract class PlantopiaMetaObject<T> {
	protected final T target;

	public PlantopiaMetaObject(T target) {
		this.target = target;
	}

	public T getTarget() {
		return target;
	}
}