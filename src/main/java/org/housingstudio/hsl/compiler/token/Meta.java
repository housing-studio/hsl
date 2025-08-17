package org.housingstudio.hsl.compiler.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents the metadata of a parsed token, that helps to identify the token's position in the source code.
 * <p>
 * This ensures, that we can provide better error messages and debugging information.
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class Meta {
    /**
     * The beginning index of the token.
     */
    private final int beginIndex;

    /**
     * The ending index of the token.
     */
    private final int endIndex;

    /**
     * The index of the first character in the line of the token being processed.
     */
    private final int lineIndex;

    /**
     * The number of the current line being processed for the token.
     */
    private final int lineNumber;

    /**
     * An empty token meta, used for dummy token creation.
     */
    public static final Meta EMPTY = new Meta(0, 0, 0, 0);
}
