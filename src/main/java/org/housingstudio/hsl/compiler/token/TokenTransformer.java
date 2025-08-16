package org.housingstudio.hsl.compiler.token;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a class that automatically inserts semicolons at the end of lines when it is required.
 * <p>
 * This concept is similar to what Go does, however this is a bit more complex, and it allows developers to use
 * standard way of making chained method calls.
 * <p>
 * Basically what this class does, is it iterates over the token list, and for each {@link TokenType#NEW_LINE} token,
 * it checks if the previous token is in the {@link #REQUIRED_BEFORE} list, and if the next token is not in the
 * {@link #FORBIDDEN_AFTER} list, then it inserts a semicolon token.
 * <p>
 * This concept was inspired by <b>leddoo</b>.
 * @see <a href="https://www.youtube.com/watch?v=09-pyU0MLCY">how i got rid of semicolons in my scripting language</a>
 *
 * @author AdvancedAntiSkid
 * @author leddoo
 */
@RequiredArgsConstructor
public class TokenTransformer {
    /**
     * The list of tokens required before the new line for the semicolon to be inserted.
     */
    private static final @NotNull List<@NotNull Token> REQUIRED_BEFORE = List.of(
        Token.of(TokenType.IDENTIFIER),
        Token.of(TokenType.STRING),
        Token.of(TokenType.INT),
        Token.of(TokenType.FLOAT),
        Token.of(TokenType.HEXADECIMAL),
        Token.of(TokenType.DURATION),
        Token.of(TokenType.BOOL),
        Token.of(TokenType.NIL),
        Token.of(TokenType.EXPRESSION, "break"),
        Token.of(TokenType.EXPRESSION, "continue"),
        Token.of(TokenType.EXPRESSION, "return"),
        Token.of(TokenType.OPERATOR, "++"),
        Token.of(TokenType.OPERATOR, "--"),
        Token.of(TokenType.RPAREN),
        Token.of(TokenType.RBRACKET),
        Token.of(TokenType.RBRACE)
    );

    /**
     * The list of tokens forbidden after the new line for the semicolon to be inserted.
     */
    private static final @NotNull List<@NotNull Token> FORBIDDEN_AFTER = List.of(
        Token.of(TokenType.OPERATOR, "="),
        Token.of(TokenType.OPERATOR, "+"),
        Token.of(TokenType.OPERATOR, "-"),
        Token.of(TokenType.OPERATOR, "*"),
        Token.of(TokenType.OPERATOR, "/"),
        Token.of(TokenType.OPERATOR, "<"),
        Token.of(TokenType.OPERATOR, ">"),
        Token.of(TokenType.OPERATOR, "?"),
        Token.of(TokenType.OPERATOR, "!"),
        Token.of(TokenType.OPERATOR, "^"),
        Token.of(TokenType.OPERATOR, "&"),
        Token.of(TokenType.OPERATOR, "~"),
        Token.of(TokenType.OPERATOR, "$"),
        Token.of(TokenType.OPERATOR, "."),
        Token.of(TokenType.OPERATOR, "%"),
        Token.of(TokenType.OPERATOR, "|")
    );

    /**
     * The list of the input tokens to be transformed.
     */
    private final @NotNull List<@NotNull Token> tokens;

    /**
     * The currently parsed token.
     */
    private @NotNull Token token = Token.of(TokenType.NONE);

    /**
     * The previously parsed token.
     */
    private @NotNull Token lastToken = Token.of(TokenType.NONE);

    /**
     * The next parsed token.
     */
    private @NotNull Token nextToken = Token.of(TokenType.NONE);

    /**
     * The index of the currently parsed token.
     */
    private int cursor;

    /**
     * Apply the transformation on the tokens.
     *
     * @return the token list with inserted semicolons
     */
    public List<Token> transform() {
        List<Token> result = new ArrayList<>();

        // transform tokens while there are more to be parsed
        while (hasNext()) {
            // update the currently parsed tokens
            updateCursor();

            // ignore the token if it is not a new line
            if (!token.is(TokenType.NEW_LINE)) {
                if (!token.is(TokenType.NONE))
                    result.add(token);
                continue;
            }

            // check if the token before is one of the required tokens
            boolean requiredBefore = REQUIRED_BEFORE
                .stream()
                .anyMatch(element -> equals(element, lastToken));

            // check if the token after is one of the forbidden tokens
            boolean forbiddenAfter = FORBIDDEN_AFTER
                .stream()
                .anyMatch(element -> equals(element, nextToken));

            // place a semicolon if the token before the new line is one of the registered tokens,
            // and the token after the new line is not one of the forbidden tokens
            if (requiredBefore && !forbiddenAfter)
                // add a dummy "auto" value for the token, therefore the AST handle user-defined semicolons and
                // automatically inserted semicolons properly.
                // let's just copy the new line's token metadata to the semicolon, considering they are of the same size
                result.add(new Token(TokenType.SEMICOLON, "auto", token.meta()));

            // if the requirements do not meet, we are just going to ignore the token
            // there is no need to put a semicolon, because it seems like the expression
            // continues
            // eg:
            // database.fetchUser() <- new line detected, but a "." follows the token, do not place semicolon
            //     .then(|user| println("hi"))
            // return "hello" <- end of method declaration, place a semicolon after
        }

        return result;
    }

    /**
     * Update the currently parsed tokens.
     */
    private void updateCursor() {
        token     = get(cursor);
        lastToken = get(cursor - 1);
        nextToken = get(cursor + 1);
        cursor++;
    }

    /**
     * Indicate, whether two tokens are equal. Ignore value checking for certain token types.
     *
     * @param left the first token to check
     * @param right the second token to check
     *
     * @return {@code true} if the two tokens are equals
     */
    private boolean equals(@NotNull Token left, @NotNull Token right) {
        // make sure both the tokens has the same type
        if (left.type() != right.type())
            return false;

        // some tokens' values must be checked as well
        return switch (left.type()) {
            case OPERATOR, EXPRESSION -> left.value().equals(right.value());
            default -> true;
        };
    }

    /**
     * Indicate, whether there are more tokens to be parsed.
     *
     * @return {@code true} if there are more tokens
     */
    private boolean hasNext() {
        return cursor >= 0 && cursor < tokens.size();
    }

    /**
     * Safely get the token at the specified index.
     *
     * @return the token at the specified index
     */
    private @NotNull Token get(int index) {
        return index >= 0 && index < tokens.size() ? tokens.get(index) : Token.of(TokenType.NONE);
    }
}
