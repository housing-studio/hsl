package org.hsl.compiler.parser;

import lombok.Setter;
import org.hsl.compiler.token.Token;
import org.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an abstract parser algorithm, that is used to convert a part of the token stream into a {@link T node}
 * for the Abstract Syntax Tree.
 *
 * @param <T> the type of the node that is created by the parser algorithm
 *
 * @author AdvancedAntiSkid
 */
@Setter
public abstract class ParserAlgorithm<T> {
    /**
     * Parse the next {@link T} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     *
     * @return the next {@link T} node from the token stream
     */
    public abstract @NotNull T parse(@NotNull AstParser parser, @NotNull ParserContext context);

    /**
     * The parser context that is used to retrieve tokens from the token stream.
     */
    private @Nullable ParserContext context;

    /**
     * Retrieve the token at the current cursor position from the token stream.
     *
     * @return the token at the current cursor position, or {@code EOF} if the cursor is out of bounds
     */
    public @NotNull Token peek() {
        return useContext().peek();
    }

    /**
     * Retrieve the token at the current cursor position from the token stream and advance the cursor.
     *
     * @return the token at the current cursor position, or {@code EOF} if the cursor is out of bounds
     */
    public @NotNull Token get() {
        return useContext().get();
    }

    /**
     * Retrieve the token at the specified index from the token stream.
     *
     * @param index the index of the token to retrieve
     * @return the token at the specified index, or {@code EOF} if the index is out of bounds
     */
    public @NotNull Token at(int index) {
        return useContext().at(index);
    }

    /**
     * Indicate, whether a token is present in the token stream at the specified index.
     *
     * @param index the index of the token to check
     * @return {@code true} if the token is present, {@code false} otherwise
     */
    public boolean has(int index) {
        return useContext().has(index);
    }

    /**
     * Indicate, whether the token at the current cursor position is of the specified type.
     *
     * @param type the expected type of the token
     * @return the token at the current cursor position, or {@code EOF} if the cursor is out of bounds
     *
     * @throws ParserException if the token is not of the specified type
     */
    public @NotNull Token peek(@NotNull TokenType type) {
        return useContext().peek(type);
    }

    /**
     * Indicate, whether the token at the current cursor position is of the specified type.
     *
     * @param types the expected types of the token
     * @return the token at the current cursor position, or {@code EOF} if the cursor is out of bounds
     *
     * @throws ParserException if the token is not of the specified types
     */
    public @NotNull Token peek(@NotNull TokenType @NotNull ... types) {
        return useContext().peek(types);
    }

    /**
     * Retrieve the token at the current cursor position from the token stream and advance the cursor.
     *
     * @param type the expected type of the token
     * @return the token at the current cursor position, or {@code EOF} if the cursor is out of bounds
     *
     * @throws ParserException if the token is not of the specified type
     */
    public @NotNull Token get(@NotNull TokenType type) {
        return useContext().get(type);
    }

    /**
     * Retrieve the token at the current cursor position from the token stream and advance the cursor.
     *
     * @param type the expected type of the token
     * @param value the expected value of the token
     * @return the token at the current cursor position, or {@code EOF} if the cursor is out of bounds
     *
     * @throws ParserException if the token is not of the specified type and value
     */
    public @NotNull Token get(@NotNull TokenType type, @NotNull String value) {
        return useContext().get(type, value);
    }

    /**
     * Retrieve the token at the current cursor position from the token stream and advance the cursor.
     *
     * @param types the expected types of the token
     * @return the token at the current cursor position, or {@code EOF} if the cursor is out of bounds
     *
     * @throws ParserException if the token is not of the specified types
     */
    public @NotNull Token get(@NotNull TokenType @NotNull ... types) {
        return useContext().get(types);
    }

    /**
     * Retrieve the index of the currently parsed token.
     *
     * @return the index of the currently parsed token
     */
    public int cursor() {
        return useContext().cursor();
    }

    /**
     * Retrieve the currently using parser context.
     *
     * @return the parser context implementation
     *
     * @throws UnsupportedOperationException if the parser context is not set
     */
    protected @NotNull ParserContext useContext() {
        if (context == null)
            throw new UnsupportedOperationException("Parser context is not set");
        return context;
    }
}
