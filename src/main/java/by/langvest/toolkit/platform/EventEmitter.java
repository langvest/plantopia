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
		Class<E> eventType = extractEventTypeFrom(listener);
		listeners.computeIfAbsent(eventType, key -> new CopyOnWriteArrayList<>()).add(listener);
	}

	public <E extends Event> void unsubscribe(Consumer<E> listener) {
		Class<E> eventType = extractEventTypeFrom(listener);
		List<Consumer<? extends Event>> eventListeners = listeners.get(eventType);

		if(eventListeners == null) return;

		eventListeners.remove(listener);

		if(eventListeners.isEmpty()) {
			listeners.remove(eventType);
		}
	}

	public <E extends Event> void emit(@NotNull E event) {
		List<Consumer<? extends Event>> eventListeners = listeners.get(event.getClass());

		if(eventListeners == null) return;

		for(Consumer<?> listener : new ArrayList<>(eventListeners)) {
			@SuppressWarnings("unchecked")
			Consumer<E> typedListener = (Consumer<E>)listener;
			typedListener.accept(event);

			if(event instanceof Cancellable cancellable && cancellable.isCancelled()) {
				break;
			}
		}
	}

	public <E extends Event> void clearListeners(@NotNull Class<E> eventType) {
		listeners.remove(eventType);
	}

	public Set<Class<? extends Event>> getActiveEventTypes() {
		return Collections.unmodifiableSet(listeners.keySet());
	}

	@SuppressWarnings("unchecked")
	protected <E extends Event> Class<E> extractEventTypeFrom(@NotNull Consumer<E> listener) {
		Class<E> eventType = (Class<E>)TypeResolver.resolveRawArgument(Consumer.class, listener.getClass());

		if((Class<?>)eventType == TypeResolver.Unknown.class) {
			throw new IllegalStateException("Failed to resolve event type: " + listener);
		}

		return eventType;
	}
}
