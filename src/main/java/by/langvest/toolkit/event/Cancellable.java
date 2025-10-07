package by.langvest.toolkit.event;

public interface Cancellable {
	boolean isCancelled();

	void setCancelled(boolean cancelled);
}
