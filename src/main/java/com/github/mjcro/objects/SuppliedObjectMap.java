package com.github.mjcro.objects;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SuppliedObjectMap<K> implements ConverterAwareObjectMap<K> {
    private final HashMap<K, Node> data = new HashMap<>();
    private final Converter converter;

    public static <T> SuppliedObjectMap<T> build(Converter converter, Consumer<BiConsumer<T, Supplier<?>>> builder) {
        SuppliedObjectMap<T> map = new SuppliedObjectMap<>(converter);
        builder.accept(map::put);
        return map;
    }

    public static <T> SuppliedObjectMap<T> of(Converter converter, Map<T, Supplier<?>> suppliers) {
        return ofEntries(converter, suppliers.entrySet());
    }

    public static <T> SuppliedObjectMap<T> ofEntries(Converter converter, Collection<Map.Entry<T, Supplier<?>>> suppliers) {
        SuppliedObjectMap<T> map = new SuppliedObjectMap<>(converter);
        for (Map.Entry<T, Supplier<?>> entry : suppliers) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    protected SuppliedObjectMap(Converter converter) {
        this.converter = converter == null ? Converter.standard() : converter;
    }

    protected void put(K key, Supplier<?> supplier) {
        data.put(key, new Node(supplier));
    }

    public void load(ExecutorService executorService, Collection<K> keys) {
        Objects.requireNonNull(executorService, "executorService");
        if (keys != null && !keys.isEmpty()) {
            for (final K key : keys) {
                Node node = data.get(key);
                if (!node.initialized) {
                    executorService.submit(node::get);
                }
            }
        }
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public Set<K> keySet() {
        return data.keySet();
    }

    @Override
    public boolean containsKey(final K key) {
        return data.containsKey(key);
    }

    @Override
    public Object get(final K key) {
        return data.get(key).get();
    }

    @Override
    public Converter getConverter() {
        return converter;
    }

    private static class Node {
        private final Supplier<?> supplier;
        private volatile Object value = null;
        private volatile RuntimeException error = null;
        private volatile boolean initialized = false;

        private Node(Supplier<?> supplier) {
            this.supplier = Objects.requireNonNull(supplier, "supplier");
        }

        private Object get() {
            if (!initialized) {
                synchronized (supplier) {
                    if (!initialized) {
                        try {
                            System.out.println("Loading");
                            this.value = supplier.get();
                        } catch (Exception error) {
                            this.error = new RuntimeException(error);
                        } finally {
                            initialized = true;
                        }
                    }
                }
            }

            if (error != null) {
                throw error;
            }

            return value;
        }
    }
}
