package by.langvest.toolkit.event;

public abstract class LifecycleEvent extends Event {
	public static class CommonSetup extends LifecycleEvent {}
	public static class ClientSetup extends LifecycleEvent {}
}
