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

    public CollectionExtractor(Converter converter) {
        this.converter = Objects.requireNonNull(converter, "converter");
    }

    public <T> List<T> toList(
            ConversionContext<T> context,
            CharSequence separator
    ) {
        return (List<T>) toCollection(context, separator, ArrayList::new);
    }

    public <T> Set<T> toSet(
            ConversionContext<T> context,
            CharSequence separator
    ) {
        return (Set<T>) toCollection(context, separator, LinkedHashSet::new);
    }

    public <T> Collection<T> toCollection(
            ConversionContext<T> context,
            CharSequence separator,
            Supplier<Collection<T>> factory
    ) throws UnableToConvertException {
        if (context == null) {
            throw new UnableToConvertException(null);
        }
        if (factory == null) {
            return toList(context, separator);
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
