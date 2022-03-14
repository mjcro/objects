package io.github.mjcro.objects;

import java.util.Objects;
import java.util.Optional;

class ParamEmpty<T> implements Param<T> {
    private final String name;

    ParamEmpty(String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<T> getValue() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getValueString() {
        return Optional.empty();
    }
}
