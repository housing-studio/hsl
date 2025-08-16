package org.housingstudio.hsl.compiler.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.value.Annotation;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.token.*;
import org.jetbrains.annotations.NotNull;

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
public class ParserContext {
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
     * The content of the source file that is being parsed.
     */
    private final @NotNull String data;

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

        syntaxError(token, "Expected: " + expectedTokens);
    }

    public void syntaxError(@NotNull Token token, @NotNull String message) {
        error(Errno.UNEXPECTED_TOKEN, "Unexpected token: " + formatToken(token), token, message);
    }

    public void error(
        @NotNull Errno code, @NotNull String title, @NotNull Token token, @NotNull String message
    ) {
        error(code, title, new TokenError(token, message));
    }

    public void error(
        @NotNull Errno code, @NotNull String title, @NotNull TokenError... errors
    ) {
        assert errors.length > 0 : "Must specify at least 1 token error";

        TokenError first = errors[0];

        System.err.println(
            Format.RED + "error[E" + code.code() + "]" + Format.WHITE + ": " + title
        );
        Meta firstMeta = first.token().meta();
        System.err.println(
            Format.CYAN + " --> " + Format.LIGHT_GRAY + "filename.hsl" + ":" + firstMeta.lineNumber() +
            ":" + firstMeta.lineIndex()
        );

        int longestSize = Stream.of(errors)
            .map(e -> e.token().meta().lineNumber())
            .map(n -> String.valueOf(n).length())
            .max(Integer::compare)
            .orElse(0);

        for (TokenError error : errors) {
            Meta meta = error.token().meta();

            int lineSize = String.valueOf(meta.lineNumber()).length();
            int padding = lineSize < longestSize ? longestSize - (longestSize - lineSize) : lineSize;

            // display the line number
            System.err.print(Format.CYAN + " ".repeat(padding + 1));
            System.err.println(" | ");

            System.err.print(" " + meta.lineNumber() + " | ");

            // get the line of the error
            String line = data.split("\n")[meta.lineNumber() - 1];

            // get the start and end index of the line
            int start = Math.max(0, meta.lineIndex() - Tokenizer.MAX_ERROR_LINE_LENGTH);
            int end = Math.min(line.length(), firstMeta.lineIndex() + Tokenizer.MAX_ERROR_LINE_LENGTH);

            // display the line of the error
            System.err.println(Format.LIGHT_GRAY + line.substring(start, end));

            // display the error pointer
            System.err.print(Format.CYAN + " ".repeat(lineSize + 1));
            String pointerPad = " ".repeat(lineSize + (meta.lineIndex() - start) - 1);
            System.err.println(" | " + pointerPad + Format.RED + "^".repeat(meta.endIndex() - meta.beginIndex()));

            // display the expected tokens below the pointer
            System.err.print(Format.CYAN + " ".repeat(lineSize + 1));
            System.err.println(" | " + pointerPad + Format.LIGHT_GRAY + error.message());
        }

        // display a final separator
        System.err.print(Format.CYAN + " ".repeat(longestSize + 1));
        System.err.println(" | ");

        System.err.print(Format.DEFAULT);

        // exit the program with the error code
        //System.exit(Errno.UNEXPECTED_TOKEN.code());
        throw new IllegalStateException("AST parse error");
    }
}
