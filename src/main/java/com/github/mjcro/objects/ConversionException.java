package com.github.mjcro.objects;

import java.util.Locale;

/**
 * Exception thrown on conversion errors.
 */
public class ConversionException extends RuntimeException {
    private static String contextMessage(ConversionContext<?> context) {
        return context == null
                ? "Void conversion context"
                : String.format(
                Locale.ROOT,
                "Unable to convert %s to %s",
                context.getSourceClass().getName(),
                context.getTargetClass().getName()
        );
    }

    public ConversionException() {
        super("Unable to perform conversion - no target class given");
    }

    public ConversionException(ConversionContext<?> context, Throwable cause) {
        super((contextMessage(context) + " - " + cause.getMessage()).trim(), cause);
    }

    public ConversionException(ConversionContext<?> context) {
        super(contextMessage(context));
    }
}
