package com.github.mjcro.objects;

import java.util.Objects;
import java.util.function.Function;

class MixedObject implements ConverterAwareMixed {
    private final Converter converter;
    private final Object data;

    static MixedObject of(Converter converter, Object data) {
        return new MixedObject(
                converter,
                data instanceof Mixed
                        ? ((Mixed) data).get()
                        : data
        );
    }

    MixedObject(Converter converter, Object data) {
        this.converter = Objects.requireNonNull(converter, "converter");
        this.data = data;
    }

    @Override
    public Converter getConverter() {
        return converter;
    }

    @Override
    public Object get() {
        return data;
    }

    @Override
    public Mixed map(Function<Object, Object> mapper) {
        return of(converter, mapper.apply(data));
    }

    @Override
    public boolean isPresent() {
        return data != null;
    }

    @Override
    public boolean isEmpty() {
        return data == null;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(data);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MixedObject)) {
            return false;
        }
        return Objects.equals(this.data, ((MixedObject) obj).data);
    }

    @Override
    public String toString() {
        return "Mixed[" + data + "]";
    }
}
