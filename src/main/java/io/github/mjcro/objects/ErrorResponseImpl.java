package io.github.mjcro.objects;

import java.util.*;

class ErrorResponseImpl implements ErrorResponse {
    private final int code;
    private final String message;
    private final List<Object> context;

    ErrorResponseImpl(int code, String message, Collection<?> context) {
        this.code = code;
        this.message = Objects.requireNonNull(message, "message");
        this.context = context == null || context.isEmpty() ? Collections.emptyList() : new ArrayList<>(context);
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public List<Object> getContext() {
        return context;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (!(other instanceof ErrorResponseImpl)) {
            return false;
        }

        ErrorResponseImpl i = (ErrorResponseImpl) other;
        return getCode() == i.getCode()
                && Objects.equals(getMessage(), i.getMessage())
                && Objects.equals(getContext(), i.getContext());
    }

    @Override
    public String toString() {
        return "[" + getCode() + " " + getMessage() + "]";
    }
}
