package com.github.mjcro.objects;

import java.util.Objects;

/**
 * Represents conversion context.
 * Contains all required information for conversion to be made.
 *
 * @param <T> Target type
 */
public class ConversionContext<T> {
    private final Object source;
    private final Class<?> sourceClass;
    private final Class<T> targetClass;

    /**
     * Constructs new conversion context.
     *
     * @param source Source object, mandatory, must be not null.
     * @param target Target class.
     */
    public ConversionContext(Object source, Class<T> target) {
        this.source = Objects.requireNonNull(source, "source");
        this.sourceClass = source.getClass();
        this.targetClass = Objects.requireNonNull(target, "target");
    }

    /**
     * Applies given converter to current conversion context.
     *
     * @param converter Converter to use.
     * @return Converted result.
     */
    public T convert(Converter converter) {
        return converter.convert(this);
    }

    /**
     * @return Source object to convert.
     */
    public Object getSource() {
        return source;
    }

    /**
     * @return Source object class.
     */
    public Class<?> getSourceClass() {
        return sourceClass;
    }

    /**
     * Returns true if source class equals to given candidate.
     *
     * @param candidate Source class candidate.
     * @return True if given candidate equals to source class.
     */
    public boolean isSourceClass(Class<?> candidate) {
        return sourceClass == candidate;
    }

    /**
     * Return true if source class extends/implements given candidate.
     *
     * @param candidate Candidate to extend/implement.
     * @return True if source class extends/implements given candidate, false otherwise.
     */
    public boolean sourceExtends(Class<?> candidate) {
        return candidate.isAssignableFrom(sourceClass);
    }

    /**
     * @return Target class.
     */
    public Class<T> getTargetClass() {
        return targetClass;
    }

    /**
     * Returns true if target class equals to given candidate.
     *
     * @param candidate Target class candidate.
     * @return True if given candidate equals to target class.
     */
    public boolean isTargetClass(Class<?> candidate) {
        return targetClass == candidate;
    }

    /**
     * Return true if target class extends/implements given candidate.
     *
     * @param candidate Candidate to extend/implement.
     * @return True if target class extends/implements given candidate, false otherwise.
     */
    public boolean targetExtends(Class<?> candidate) {
        return candidate.isAssignableFrom(targetClass);
    }

    /**
     * Returns true if target class equals to one of given candidates.
     *
     * @param a Candidate.
     * @param b Candidate.
     * @return True if given candidate equals to one of target classes.
     */
    public boolean isTargetClass(Class<?> a, Class<?> b) {
        return targetClass == a || targetClass == b;
    }
}
