package io.github.mjcro.objects;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public interface ParamSet {
    static ParamSet create(Param<?> param) {
        return append(null, param);
    }

    static ParamSet append(ParamSet origin, Param<?> param) {
        Objects.requireNonNull(param, "param");
        return new ParamSetNested(null, param);
    }

    Optional<Param<?>> find(String name);

    int size();

    Set<String> names();

    Iterator<Param<?>> iterator();

    Stream<Param<?>> stream();
}
