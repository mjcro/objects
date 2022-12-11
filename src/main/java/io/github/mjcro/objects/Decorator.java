package io.github.mjcro.objects;

/**
 * Generic interface for decorators.
 *
 * @param <T> Decorated type.
 */
@FunctionalInterface
public interface Decorator<T> {
    @SuppressWarnings("unchecked")
    static <X> X undecorate(X source) {
        if (source instanceof Decorator<?>) {
            return ((Decorator<X>) source).getDecoratedRoot();
        }
        return source;
    }

    /**
     * @return Object been decorated.
     */
    T getDecorated();

    /**
     * @return Objects been decorated, recursively, if it's also a decorator.
     */
    @SuppressWarnings("unchecked")
    default T getDecoratedRoot() {
        T decorated = getDecorated();
        if (decorated instanceof Decorator<?>) {
            return ((Decorator<T>) decorated).getDecoratedRoot();
        }
        return decorated;
    }
}
