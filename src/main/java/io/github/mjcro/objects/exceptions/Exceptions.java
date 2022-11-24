package io.github.mjcro.objects.exceptions;

import java.util.Optional;

/**
 * Exception utils.
 */
public class Exceptions {
    /**
     * Searches for expected exception class in exception causes.
     *
     * @param original Original exception.
     * @param clazz    Exception class to find.
     * @param <T>      Exception clazz.
     * @return Found type.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> Optional<T> find(Throwable original, Class<T> clazz) {
        if (original == null) {
            return Optional.empty();
        } else if (original.getClass() == clazz) {
            return (Optional<T>) Optional.of(original);
        }

        Throwable cause = original.getCause();
        return find(cause == null || cause == original ? null : cause, clazz);
    }

    private Exceptions() {
    }
}
