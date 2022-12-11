package io.github.mjcro.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Multi-functional container that inmemory stores and indexes data
 * by hash using indexing function given in constructor.
 * <p>
 * This class is not thread-safe.
 *
 * @param <T>
 */
public class HashIndex<T> implements Iterable<T> {
    private final ArrayList<T> data = new ArrayList<>();
    private final HashMap<Function<T, ?>, HashMap<Object, List<T>>> index = new HashMap<>();

    /**
     * Constructor.
     *
     * @param indexers Indexer functions.
     */
    public HashIndex(Iterable<Function<T, ?>> indexers) {
        if (indexers != null) {
            for (Function<T, ?> indexer : indexers) {
                index.put(indexer, new HashMap<>());
            }
        }
    }

    /**
     * Constructor.
     *
     * @param indexers Indexer functions.
     */
    @SafeVarargs
    public HashIndex(Function<T, ?>... indexers) {
        this(indexers == null ? Collections.emptyList() : Arrays.asList(indexers));
    }

    /**
     * @return Count of items.
     */
    public int size() {
        return data.size();
    }

    /**
     * @return True if index has no data.
     */
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return data.iterator();
    }

    /**
     * @return Values stream.
     */
    public Stream<T> stream() {
        return data.stream();
    }

    /**
     * Adds new value to index.
     *
     * @param values Value to add.
     */
    @SafeVarargs
    public final void add(T... values) {
        Objects.requireNonNull(values, "values");

        for (final T value : values) {
            if (value != null) {
                // Adding raw data
                data.add(value);

                // Indexing
                for (Function<T, ?> indexer : index.keySet()) {
                    Object qualifier = indexer.apply(value);
                    if (qualifier != null) {
                        index.get(indexer)
                                .computeIfAbsent(qualifier, o -> new ArrayList<>())
                                .add(value);
                    }
                }
            }
        }
    }

    /**
     * Fetches multiple values.
     *
     * @param indexer    Indexing function.
     * @param qualifiers Qualifiers to find.
     * @param <Z>        Item type.
     * @return Map of matched data.
     */
    public <Z> Map<Z, List<T>> get(Function<T, Z> indexer, Iterable<Z> qualifiers) {
        if (qualifiers == null) {
            return Collections.emptyMap();
        }

        HashMap<?, List<T>> values = index.get(indexer);
        if (values != null) {
            HashMap<Z, List<T>> response = new HashMap<>();
            for (Z qualifier : qualifiers) {
                List<T> list = values.get(qualifier);
                if (list != null) {
                    response.put(qualifier, list);
                }
            }
            return response;
        }

        return Collections.emptyMap();
    }

    /**
     * Fetches single value.
     *
     * @param indexer   Indexing function.
     * @param qualifier Qualifier to find.
     * @param <Z>       Item type.
     * @return Collection of matched data.
     */
    public <Z> List<T> get(Function<T, Z> indexer, Z qualifier) {
        return get(indexer, Collections.singleton(qualifier)).getOrDefault(qualifier, Collections.emptyList());
    }
}
