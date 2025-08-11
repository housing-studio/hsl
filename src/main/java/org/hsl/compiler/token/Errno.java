package org.hsl.compiler.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents an enumeration of token errors that can occur during tokenization.
 *
 * @author AdvancedAntiSkid
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum Errno {
    /**
     * `UNEXPECTED_CHARACTER` indicates, that the tokenizer encountered an unexpected character.
     */
    UNEXPECTED_CHARACTER(101),

    /**
     * `INVALID_ESCAPE_SEQUENCE` indicates, that the tokenizer detected an invalid character that follows a backslash.
     */
    INVALID_ESCAPE_SEQUENCE(102),

    /**
     * `MISSING_STRING_TERMINATOR` indicates, that the tokenizer did not find a closing quote for a string literal,
     * or the beginning and ending quotes are not the same.
     */
    MISSING_STRING_TERMINATOR(103),

    /**
     * `INVALID_UNSIGNED_LITERAL` indicates, that the tokenizer detected an invalid unsigned literal.
     */
    INVALID_UNSIGNED_LITERAL(104),

    /**
     * `MULTIPLE_DECIMAL_POINTS` indicates, that the tokenizer detected multiple decimal points in a number literal.
     */
    MULTIPLE_DECIMAL_POINTS(105),

    /**
     * `CANNOT_HAVE_DECIMAL_POINT` indicates, that the tokenizer detected a decimal point in a number literal that
     * cannot have a decimal point.
     */
    CANNOT_HAVE_DECIMAL_POINT(106),

    /**
     * `UNEXPECTED_TOKEN` indicates, that the tokenizer encountered an unexpected token.
     */
    UNEXPECTED_TOKEN(107),

    /**
     * `UNKNOWN_VARIABLE` indicates, that a variable is being accessed that is not found.
     */
    UNKNOWN_VARIABLE(108),

    /**
     * `UNKNOWN_VARIABLE` indicates, that a duration value has invalid format.
     */
    INVALID_DURATION_VALUE(109),

    /**
     * `UNKNOWN_METHOD` indicates, that a method is being called that is not found.
     */
    UNKNOWN_METHOD(110);

    /**
     * The error code of the token error.
     */
    private final int code;
}
