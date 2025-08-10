package org.hsl.compiler.parser;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an exception that occurred during the parsing of the source code.
 *
 * @author AdvancedAntiSkid
 */
public class ParserException extends RuntimeException {
    /**
     * Create a new parser exception with the specified message and cause.
     *
     * @param message the message of the exception
     * @param cause the cause of the exception
     */
    public ParserException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }

    /**
     * Create a new parser exception with the specified message.
     *
     * @param message the message of the exception
     */
    public ParserException(@NotNull String message) {
        super(message);
    }
}
