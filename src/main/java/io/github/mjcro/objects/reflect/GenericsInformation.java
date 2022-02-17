package io.github.mjcro.objects.reflect;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GenericsInformation {

    public static Class<?>[] typeArgumentsOf(Method method, int index) {
        if (method == null) {
            return new Class<?>[0];
        }

        Type genericParameterType = method.getGenericParameterTypes()[index];
        if (genericParameterType instanceof ParameterizedType) {
            return typeArgumentsOf((ParameterizedType) genericParameterType);
        }

        return new Class<?>[0];
    }

    public static Class<?>[] typeArgumentsOf(Field field) {
        if (field == null) {
            return new Class<?>[0];
        }

        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        return typeArgumentsOf(parameterizedType);
    }

    public static Class<?>[] typeArgumentsOf(Object source) {
        if (source == null) {
            return new Class<?>[0];
        }

        ParameterizedType parameterizedType = (ParameterizedType) source.getClass().getGenericSuperclass();
        return typeArgumentsOf(parameterizedType);
    }

    private static Class<?> typeArgumentOf(ParameterizedType parameterizedType, int index) {
        return typeArgumentsOf(parameterizedType)[index];
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

    public static ArrayList<Boolean> booleans;

    public static void main(String[] args) throws NoSuchFieldException {
//        System.out.println(Generics.first(new ArrayList<String>()));
        System.out.println(Arrays.toString(GenericsInformation.typeArgumentsOf(new ConcreteString())));
        System.out.println(Arrays.toString(GenericsInformation.typeArgumentsOf(GenericsInformation.class.getField("booleans"))));
    }

    public static class ConcreteString extends SomethingGeneric<String> {
    }

    public static class SomethingGeneric<T> {

    }
}
