package org.housingstudio.hsl.compiler.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.TokenContext;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.value.Annotation;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.ErrorMode;
import org.housingstudio.hsl.compiler.error.ErrorPrinter;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.token.*;
import org.housingstudio.hsl.lsp.Diagnostics;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Represents a class, that utilises methods to resolve tokens from the token stream.
 * <p>
 * The implementations of the {@link ParserAlgorithm} use this class to parse a part of the token stream.
 *
 * @author AdvancedAntiSkid
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class ParserContext implements TokenContext {
    private final ErrorPrinter errorPrinter = new ErrorPrinter(this);

    /**
     * The list of annotations that are declared by a type/function definition.
     */
    @Getter
    private final List<Annotation> currentAnnotations = new ArrayList<>();

    /**
     * The list of tokens received by the {@link Tokenizer}, to be parsed to a tree of {@link Node}s.
     */
    private final @NotNull List<@NotNull Token> tokens;

    /**
     * The underlying file that was tokenized.
     */
    private final @NotNull File file;

    /**
     * The content of the source file that is being parsed.
     */
    private final @NotNull String data;

    private final @NotNull Diagnostics diagnostics;
    private final @NotNull ErrorMode mode;

    /**
     * The index of the currently parsed token.
     */
    @Getter
    private int cursor;

    /**
     * Retrieve the token at the current cursor position from the token stream.
     *
     * @return the token at the current cursor position, or {@code EOF} if the cursor is out of bounds
     */
    public @NotNull Token peek() {
        return at(cursor);
    }

    /**
     * Retrieve the token at the current cursor position from the token stream and advance the cursor.
     *
     * @return the token at the current cursor position, or {@code EOF} if the cursor is out of bounds
     */
    public @NotNull Token get() {
        return at(cursor++);
    }

    /**
     * Retrieve the token at the specified index from the token stream.
     *
     * @param index the index of the token to retrieve
     * @return the token at the specified index, or {@code EOF} if the index is out of bounds
     */
    public @NotNull Token at(int index) {
        return has(index) ? tokens.get(index) : Token.of(TokenType.EOF);
    }

    /**
     * Indicate, whether a token is present in the token stream at the specified index.
     *
     * @param index the index of the token to check
     * @return {@code true} if the token is present, {@code false} otherwise
     */
    public boolean has(int index) {
        return index >= 0 && index < tokens.size();
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
        Token token = peek();
        if (token.is(type))
            return token;
        syntaxError(token, Token.of(type));
        throw new ParserException("Invalid token. Expected " + type + " but found " + token);
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
        Token token = peek();
        for (TokenType type : types) {
            if (token.is(type))
                return token;
        }
        syntaxError(token, Arrays.stream(types).map(Token::of).toArray(Token[]::new));
        throw new ParserException("Invalid token. Expected " + Arrays.toString(types) + " but found " + token);
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
        Token token = get();
        if (token.is(type))
            return token;
        syntaxError(token, Token.of(type));
        throw new ParserException("Invalid token. Expected " + type + " but found " + token);
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
        Token token = get();
        if (token.is(type, value))
            return token;
        syntaxError(token, Token.of(type, value));
        throw new ParserException("Invalid token. Expected " + Token.of(type, value) + " but found " + token);
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
        Token token = get();
        for (TokenType type : types) {
            if (token.is(type))
                return token;
        }
        syntaxError(token, Arrays.stream(types).map(Token::of).toArray(Token[]::new));
        throw new ParserException("Invalid token. Expected " + Arrays.toString(types) + " but found " + token);
    }

    private @NotNull String formatToken(@NotNull Token token) {
        String name = token.type().name();
        if (!token.value().isEmpty())
            name += " `" + token.value() + "`";
        return Format.WHITE + name;
    }

    public void syntaxError(@NotNull Token token, @NotNull Token @NotNull ... expected) {
        String expectedTokens = Stream.of(expected)
            .map(this::formatToken)
            .reduce((a, b) -> a + Format.LIGHT_GRAY + ", " + b)
            .orElse("");

        syntaxError(token, "expected: " + expectedTokens);
    }

    public void syntaxError(@NotNull Token token, @NotNull String message) {
        errorPrinter.print(
            Notification.error(Errno.UNEXPECTED_TOKEN, "unexpected token: " + token.value())
                .error(message, token)
        );
    }
}
