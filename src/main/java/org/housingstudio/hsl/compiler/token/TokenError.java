package org.housingstudio.hsl.compiler.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a token error information container.
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class TokenError {
    /**
     * The token that caused the error.
     */
    private final @NotNull Token token;

    /**
     * The message describing the error.
     */
    private final @NotNull String message;
}
