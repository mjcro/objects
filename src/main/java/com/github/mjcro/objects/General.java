package com.github.mjcro.objects;

import com.github.mjcro.objects.converters.*;

/**
 * Utility class that contains general converter.
 */
public class General {
    /**
     * General converter instance to use when no explicit converter supplied.
     * This is mutable value and can be changed whilest this is not recommended.
     */
    public static Converter CONVERTER = new ConverterSame(
            new ConverterMixed(
                    new ConverterToString(
                            new ConverterUnboxing(
                                    new ConverterStringBooleans(
                                            new ConverterStringNumbers(
                                                    new ConverterStringEnums(
                                                            new ConverterConstructing(
                                                                    null
                                                            )
                                                    )
                                            )
                                    )
                            )
                    )
            )
    );

    /**
     * Always return non-null converter.
     * If null given will return general converter instance.
     *
     * @param candidate Converter candidate. If null, general converter instance
     *                  will be returned.
     * @return Converter, non null.
     */
    public static Converter ensureConverter(Converter candidate) {
        return candidate == null ? CONVERTER : candidate;
    }

    /**
     * Private constructor for utility class.
     */
    private General() {
    }
}
