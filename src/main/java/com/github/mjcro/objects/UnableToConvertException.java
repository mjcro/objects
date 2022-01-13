package com.github.mjcro.objects;

import java.util.Locale;

/**
 * Exception thrown on conversion errors.
 */
public class UnableToConvertException extends RuntimeException {
    private static String contextMessage(ConversionContext<?> context) {
        return context == null
                ? "Void conversion context"
                : (
                context.getTargetClass() == null
                        ? "Unable to convert from " + context.getSourceClass().getName()
                        : String.format(
                        Locale.ROOT,
                        "Unable to convert %s to %s",
                        context.getSourceClass().getName(),
                        context.getTargetClass().getName()
                )
        );
    }

    public UnableToConvertException() {
        super("Unable to perform conversion - no target class given");
    }

    public UnableToConvertException(ConversionContext<?> context, Throwable cause) {
        super((contextMessage(context) + " " + cause.getMessage()).trim(), cause);
    }

    public UnableToConvertException(ConversionContext<?> context) {
        super(contextMessage(context));
    }
}
