package io.github.mjcro.objects;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Defines safe to deliver error responses.
 */
public interface ErrorResponse {
    /**
     * Constructs new error response instance for given code, message and no context.
     *
     * @param code    Error code.
     * @param message Error message.
     * @return Error response.
     */
    static ErrorResponse of(int code, String message) {
        return new ErrorResponseImpl(code, message, null);
    }

    /**
     * Constructs enw error response instance for given code, message and context.
     *
     * @param code    Error code.
     * @param message Error message.
     * @param context Error context.
     * @return Error message.
     */
    static ErrorResponse of(int code, String message, Collection<?> context) {
        return new ErrorResponseImpl(code, message, context);
    }

    /**
     * @return Error code.
     */
    int getCode();

    /**
     * @return Error message.
     */
    String getMessage();

    /**
     * @return Error context.
     */
    List<Object> getContext();

    /**
     * @return Error context as string list.
     */
    default List<String> getContextString() {
        return getContext().stream().filter(Objects::nonNull).map(Object::toString).collect(Collectors.toList());
    }

    /**
     * Searches for given value in error context.
     *
     * @param value Value to find.
     * @return True if value found, false otherwise.
     */
    default boolean has(Object value) {
        return getContext().contains(value);
    }
}
