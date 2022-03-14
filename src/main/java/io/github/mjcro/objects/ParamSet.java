package io.github.mjcro.objects;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Parameters collection.
 * Immutable.
 */
public interface ParamSet extends Iterable<Param<?>> {
    /**
     * Creates new parameter collection.
     *
     * @param param Parameter to add.
     * @return Parameter collection.
     */
    static ParamSet create(Param<?> param) {
        return append(null, param);
    }

    /**
     * Constructs new parameter collection containing
     * data from original one plus new added param.
     *
     * @param origin Original collection, optional.
     * @param param  Newly added parameter.
     * @return Parameter collection.
     */
    static ParamSet append(ParamSet origin, Param<?> param) {
        Objects.requireNonNull(param, "param");
        if (origin == null) {
            return new ParamSetNested(null, param);
        }
        if (origin instanceof ParamSetNested) {
            return new ParamSetNested((ParamSetNested) origin, param);
        } else {
            throw new IllegalArgumentException("Unsupported parameter collection type");
        }
    }

    /**
     * Constructs new parameter collection containing
     * data from original one plus new added params
     * from iterable source.
     *
     * @param origin Original collection, optional.
     * @param params Iterable parameters.
     * @return Parameter collection.
     */
    static ParamSet append(ParamSet origin, Iterable<Param<?>> params) {
        ParamSet result = null;
        for (Param<?> param : params) {
            result = append(result, param);
        }
        if (result == null) {
            throw new IllegalArgumentException("Unable to create empty parameters collection");
        }
        return result;
    }

    /**
     * Searches for parameter by its name.
     *
     * @param name Parameter name.
     * @return Found parameter, optional.
     */
    Optional<Param<?>> find(String name);

    /**
     * @return Amount of items in collection.
     */
    int size();

    /**
     * @return All names of parameters within collection.
     */
    Set<String> names();

    /**
     * @return Iterator over collection.
     */
    Iterator<Param<?>> iterator();

    /**
     * @return Stream over collection.
     */
    Stream<Param<?>> stream();
}
