package io.github.mjcro.objects;

import java.util.Objects;
import java.util.Optional;

class ParamImpl<T> implements Param<T> {
    private final String name;
    private final T value;

    ParamImpl(String name, T value) {
        this.name = Objects.requireNonNull(name, "name");
        this.value = Objects.requireNonNull(value, "value");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<T> getValue() {
        return Optional.of(value);
    }
}
