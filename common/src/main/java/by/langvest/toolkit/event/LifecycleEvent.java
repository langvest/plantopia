package by.langvest.toolkit.event;

public abstract class LifecycleEvent extends Event {
    public static class CommonSetupEvent extends LifecycleEvent {}

    public static class ClientSetupEvent extends LifecycleEvent {}
}
