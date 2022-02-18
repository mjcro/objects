package io.github.mjcro.objects.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericsInformation {

    public static Class<?>[] typeArgumentsOf(Method method, int index) {
        if (method == null) {
            return new Class<?>[0];
        }

        return typeArgumentsOf(method.getGenericParameterTypes()[index]);
    }

    public static Class<?>[] typeArgumentsOf(Field field) {
        if (field == null) {
            return new Class<?>[0];
        }

        return typeArgumentsOf(field.getGenericType());
    }

    public static Class<?>[] typeArgumentsOf(Object source) {
        if (source == null) {
            return new Class<?>[0];
        }

        ParameterizedType parameterizedType = (ParameterizedType) source.getClass().getGenericSuperclass();
        return typeArgumentsOf(parameterizedType);
    }

    /**
     * Retrieves generics information.
     *
     * @param type Type to analyze.
     * @return Array of classes.
     * @throws ClassCastException On error
     */
    private static Class<?>[] typeArgumentsOf(Type type) {
        if (type instanceof ParameterizedType) {
            return typeArgumentsOf((ParameterizedType) type);
        }
        return new Class<?>[0];
    }

    /**
     * Retrieves generics information.
     *
     * @param parameterizedType Parametrized type to analyze.
     * @return Array of classes.
     * @throws ClassCastException On error
     */
    private static Class<?>[] typeArgumentsOf(ParameterizedType parameterizedType) {
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments == null || actualTypeArguments.length == 0) {
            return new Class<?>[0];
        }

        Class<?>[] result = new Class[actualTypeArguments.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (Class<?>) actualTypeArguments[i];
        }
        return result;
    }
}
