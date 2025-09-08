package org.housingstudio.hsl.compiler.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a section of the parsed source string that holds specific information of a file part.
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class Token implements Printable {
    /**
     * The type of the token.
     */
    private final @NotNull TokenType type;

    /**
     * The value of the token.
     */
    private final @NotNull String value;

    /**
     * The source file metadata of the token.
     */
    private final @NotNull Meta meta;

    /**
     * Indicate, whether this token is of the specified type.
     *
     * @param type the type to compare with
     * @return {@code true} if this token is of the specified type, {@code false} otherwise
     */
    public boolean is(@NotNull TokenType type) {
        return this.type == type;
    }

    /**
     * Indicate, whether this token is of the specified types.
     *
     * @param types the types to compare with
     * @return {@code true} if this token is of the specified types, {@code false} otherwise
     */
    public boolean is(@NotNull TokenType @NotNull ... types) {
        for (TokenType type : types) {
            if (is(type))
                return true;
        }
        return false;
    }

    /**
     * Indicate, whether this token has the specified type and value.
     *
     * @param type the type of the token
     * @param value the value of the token
     * @return {@code true} if the type and value matches, {@code false} otherwise
     */
    public boolean is(@NotNull TokenType type, @NotNull String value) {
        return this.type == type && this.value.equals(value);
    }

    /**
     * Indicate, whether this token has any of the specified values.
     *
     * @param values the types to compare with
     * @return {@code true} if this token is of the specified values, {@code false} otherwise
     */
    public boolean val(@NotNull String @NotNull ... values) {
        for (String value : values) {
            if (this.value.equals(value))
                return true;
        }
        return false;
    }

    /**
     * Indicate, whether this token is not a finish token, and there are more tokens expected to be parsed.
     *
     * @return {@code true} if there are more tokens to be parsed, {@code false} otherwise
     */
    public boolean hasNext() {
        switch (type) {
            case UNEXPECTED:
            case EOF:
                return false;
            default:
                return true;
        }
    }

    /**
     * Indicate, whether the type of this token is a number.
     *
     * @return {@code true} if this token is a number
     */
    public boolean isNumber() {
        switch (type) {
            case INT:
            case FLOAT:
            case HEXADECIMAL:
            case BINARY:
            case DURATION:
                return true;
            default:
                return false;
        }
    }

    /**
     * Indicate, whether the type of this token is a literal token type.
     *
     * @return {@code true} if this token is a constant literal
     */
    public boolean isLiteral() {
        switch (type) {
            case STRING:
            case BOOL:
                return true;
            default:
                return isNumber();
        }
    }

    /**
     * Indicate, whether this token is of the specified type.
     *
     * @param o the reference object with which to compare
     * @return {@code true} if this token is of the specified type, {@code false} otherwise
     */
    @Override
    public boolean equals(@Nullable Object o) {
        if (o == this)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Token token = (Token) o;
        return type == token.type && value.equals(token.value);
    }

    /**
     * Returns the unique hash code value for this token.
     *
     * @return the hash code value for this token
     */
    @Override
    public int hashCode() {
        return Objects.hash(type, value, meta);
    }

    /**
     * Create a new token with the specified type and value.
     *
     * @param type the type of the token
     * @param value the value of the token
     *
     * @return a new parsed token
     */
    public static @NotNull Token of(@NotNull TokenType type, @NotNull String value) {
        return new Token(type, value, Meta.EMPTY);
    }

    /**
     * Create a new token with the specified type.
     *
     * @param type the type of the token
     *
     * @return a new parsed token
     */
    public static @NotNull Token of(@NotNull TokenType type) {
        return new Token(type, "", Meta.EMPTY);
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return Format.YELLOW + type.name() + Format.DARK_GRAY + '|'
            + Format.WHITE + value + Format.DARK_GRAY + '|'
            + Format.WHITE;
    }
}
