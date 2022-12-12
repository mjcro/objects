package io.github.mjcro.objects;

/**
 * Generic interface for decorators.
 *
 * @param <T> Decorated type.
 */
@FunctionalInterface
public interface Decorator<T> {
    /**
     * Checks if given source object is decorator and if true - extracts
     * its decoration target recursively.
     * If false - returns source itself.
     *
     * @param source Source object.
     * @param <X>    Decoration target type.
     * @return Decoration root or source object.
     */
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
