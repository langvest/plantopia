package by.langvest.toolkit.platform;

import by.langvest.toolkit.event.Cancellable;
import by.langvest.toolkit.event.Event;
import net.jodah.typetools.TypeResolver;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventEmitter {
    protected static final EventEmitter DEFAULT_INSTANCE = new EventEmitter();
    protected final Map<Class<? extends Event>, List<Consumer<? extends Event>>> listeners = new ConcurrentHashMap<>();

    public static EventEmitter getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public <E extends Event> void subscribe(Consumer<E> listener) {
        Class<E> eventType = getEventTypeFromListener(listener);
        getOrCreateListeners(eventType).add(listener);
    }

    public <E extends Event> void once(Consumer<E> listener) {
        Class<E> eventType = getEventTypeFromListener(listener);

        Consumer<E> listenerWrapper = new Consumer<>() {
            @Override
            public void accept(E event) {
                removeListener(eventType, this);
                listener.accept(event);
            }
        };

        getOrCreateListeners(eventType).add(listenerWrapper);
    }

    public <E extends Event> void unsubscribe(Consumer<E> listener) {
        Class<E> eventType = getEventTypeFromListener(listener);
        removeListener(eventType, listener);
    }

    public <E extends Event> void emit(@NotNull E event) {
        List<Consumer<? extends Event>> eventListeners = listeners.get(getEventTypeFromEvent(event));

        if (eventListeners == null) return;

        for (var listener : new ArrayList<>(eventListeners)) {
            @SuppressWarnings("unchecked")
            Consumer<E> typedListener = (Consumer<E>) listener;
            typedListener.accept(event);

            if (event instanceof Cancellable cancellable && cancellable.isCancelled()) {
                break;
            }
        }
    }

    public void clear() {
        listeners.clear();
    }

    public <E extends Event> void clearByEventType(@NotNull Class<E> eventType) {
        listeners.remove(eventType);
    }

    public Set<Class<? extends Event>> getEventTypes() {
        return Collections.unmodifiableSet(listeners.keySet());
    }

    protected List<Consumer<? extends Event>> getOrCreateListeners(Class<? extends Event> eventType) {
        return listeners.computeIfAbsent(eventType, key -> new CopyOnWriteArrayList<>());
    }

    protected void removeListener(Class<? extends Event> eventType, Consumer<? extends Event> listener) {
        var eventListeners = listeners.get(eventType);

        if (eventListeners == null) return;

        eventListeners.remove(listener);

        if (eventListeners.isEmpty()) {
            listeners.remove(eventType);
        }
    }

    @SuppressWarnings("unchecked")
    protected <E extends Event> Class<E> getEventTypeFromListener(@NotNull Consumer<E> listener) {
        Class<E> eventType = (Class<E>) TypeResolver.resolveRawArgument(Consumer.class, listener.getClass());

        if ((Class<?>) eventType == TypeResolver.Unknown.class) {
            throw new IllegalStateException(String.format("Failed to resolve event type from listener %s", listener));
        }

        return eventType;
    }

    @SuppressWarnings("unchecked")
    protected <E extends Event> Class<? extends E> getEventTypeFromEvent(@NotNull E event) {
        Class<? extends E> eventType = (Class<? extends E>) event.getClass();

        if (eventType.isAnonymousClass()) {
            eventType = (Class<? extends E>) eventType.getSuperclass();
        }

        return eventType;
    }
}
