package by.langvest.toolkit.collection;

import com.google.common.collect.Streams;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Streamable<T> extends Iterable<T> {
    default Stream<T> stream() {
        return Streams.stream(this);
    }

    default List<T> getAll() {
        return stream().toList();
    }

    default List<T> findAll(Predicate<T> predicate) {
        return stream().filter(predicate).toList();
    }

    default Optional<T> findValue(Predicate<T> predicate) {
        for (T item : this) {
            if (predicate.test(item)) {
                return Optional.of(item);
            }
        }

        return Optional.empty();
    }
}
