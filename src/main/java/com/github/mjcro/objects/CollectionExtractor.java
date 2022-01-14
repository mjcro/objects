package com.github.mjcro.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Extracts collection of values from object value.
 */
public class CollectionExtractor {
    private final Converter converter;

    /**
     * Constructs new collection extractor with given converter.
     *
     * @param converter Converter to use, mandatory.
     */
    public CollectionExtractor(Converter converter) {
        this.converter = Objects.requireNonNull(converter, "converter");
    }

    /**
     * Converts object from conversion context into list.
     *
     * @param source    Source object.
     * @param target    Target collection's item class.
     * @param separator Value separator, comma is used by default.
     * @param <T>       Collection type type.
     * @return Collection.
     * @throws ConversionException On conversion error.
     */
    public <T> List<T> toList(
            Object source,
            Class<T> target,
            CharSequence separator
    ) throws ConversionException {
        return this.toList(new ConversionContext<>(source, target), separator);
    }

    /**
     * Converts object from conversion context into list.
     *
     * @param context   Conversion context.
     * @param separator Value separator, comma is used by default.
     * @param <T>       Collection type type.
     * @return Collection.
     * @throws ConversionException On conversion error.
     */
    public <T> List<T> toList(
            ConversionContext<T> context,
            CharSequence separator
    ) throws ConversionException {
        return (List<T>) this.toCollection(context, separator, ArrayList::new);
    }

    /**
     * Converts object from conversion context into set.
     *
     * @param source    Source object.
     * @param target    Target collection's item class.
     * @param separator Value separator, comma is used by default.
     * @param <T>       Collection type type.
     * @return Collection.
     * @throws ConversionException On conversion error.
     */
    public <T> Set<T> toSet(
            Object source,
            Class<T> target,
            CharSequence separator
    ) throws ConversionException {
        return this.toSet(new ConversionContext<>(source, target), separator);
    }

    /**
     * Converts object from conversion context into set.
     *
     * @param context   Conversion context.
     * @param separator Value separator, comma is used by default.
     * @param <T>       Collection type type.
     * @return Collection.
     * @throws ConversionException On conversion error.
     */
    public <T> Set<T> toSet(
            ConversionContext<T> context,
            CharSequence separator
    ) throws ConversionException {
        return (Set<T>) this.toCollection(context, separator, LinkedHashSet::new);
    }

    /**
     * Converts given object to collection.
     *
     * @param source    Source object.
     * @param target    Target collection's item class.
     * @param separator Value separator, comma is used by default.
     * @param factory   Collection factory, reference to it's constructor in most cases.
     * @param <T>       Collection item type.
     * @return Collection.
     * @throws ConversionException On conversion error.
     */
    public <T> Collection<T> toCollection(
            Object source,
            Class<T> target,
            CharSequence separator,
            Supplier<Collection<T>> factory
    ) throws ConversionException {
        return this.toCollection(new ConversionContext<>(source, target), separator, factory);
    }

    /**
     * Converts object from conversion context into collection.
     *
     * @param context   Conversion context.
     * @param separator Value separator, comma is used by default.
     * @param factory   Collection factory, reference to it's constructor in most cases.
     * @param <T>       Collection type type.
     * @return Collection.
     * @throws ConversionException On conversion error.
     */
    public <T> Collection<T> toCollection(
            ConversionContext<T> context,
            CharSequence separator,
            Supplier<Collection<T>> factory
    ) throws ConversionException {
        if (context == null) {
            throw new ConversionException(null);
        }
        if (factory == null) {
            return toList(context, separator);
        }
        if (context.getSource() instanceof Mixed) {
            // Delegating to mixed
            Collection<T> collection = ((Mixed) context.getSource()).getCollection(separator, context.getTargetClass());
            // Applying factory
            Collection<T> result = factory.get();
            result.addAll(collection);
            return result;
        }
        if (separator == null) {
            separator = ",";
        }

        List<?> sourceList;
        if (context.getSource() instanceof Collection<?>) {
            sourceList = new ArrayList<>((Collection<?>) context.getSource());
        } else {
            sourceList = Arrays.asList(context.getSource().toString().split(Pattern.quote(separator.toString())));
        }

        return sourceList.stream()
                .map(converter.curry(context.getTargetClass()))
                .collect(Collectors.toCollection(factory));
    }
}
