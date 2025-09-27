package org.housingstudio.hsl.compiler.parser.impl.operator;

import org.housingstudio.hsl.compiler.ast.impl.operator.Operator;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.parser.ParserException;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a parser algorithm for parsing an {@link Operator} enum from the token stream.
 * <p>
 * Since, the tokenizer outputs operator chars as separate tokens, this parser is used to combine them into a single
 * operator token, according to the operator grammar.
 * <p>
 * For example, the operators in code {@code foo = !bar} will be parsed as {@code =} (assign) and {@code !} (negate).
 */
public class OperatorParser extends ParserAlgorithm<Operator> {
    /**
     * Parse the next {@link Operator} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     * @return the next {@link Operator} node from the token stream
     */
    @Override
    public @NotNull Operator parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        // loop until the token is an operator
        StringBuilder builder = new StringBuilder();

        // handle slice and lambda operators
        if (peek().is(TokenType.COLON)) {
            builder.append(get().value());
            if (peek().is(TokenType.COLON))
                builder.append(get().value());
            return parseOperator(builder);
        }

        // loop until the token is an operator
        while (peek().is(TokenType.OPERATOR)) {
            // append the token to the final operator
            String value = get().value();
            builder.append(value);

            // check if the built operator should be terminated
            if (shouldOperatorTerminate(builder.toString()))
                return parseOperator(builder);

            // check if the next operator should terminate the current operator
            String next = peek().value();
            if (shouldOperatorTerminate(builder.toString(), next))
                return parseOperator(builder);
        }

        // a non-operator token has blocked the operator processing, resolve the built operator
        return parseOperator(builder);
    }

    /**
     * Parse the operator from the specified operator builder.
     * <p>
     * If the operator is invalid, a syntax error will be thrown.
     *
     * @param operator the operator builder
     * @return the parsed operator
     */
    private @NotNull Operator parseOperator(@NotNull StringBuilder operator) {
        Operator result = Operator.of(operator.toString());
        if (result == Operator.UNKNOWN) {
            useContext().syntaxError(peek(), "invalid operator: " + operator);
            throw new ParserException("invalid operator: " + operator);
        }
        return result;
    }

    /**
     * Indicate, whether the specified operator should be terminated as it is.
     *
     * @param operator the target operator
     * @return {@code true} if the operator parsing should terminate, {@code false} otherwise
     */
    private boolean shouldOperatorTerminate(@NotNull String operator) {
        switch (operator) {
            case "&&":
            case "||":
            case "??":
            case "?.":
            case "++":
            case "--":
            case "==":
            case "!=":
                return true;
            default:
                return false;
        }
    }

    /**
     * Indicate, whether the specified operator should be terminated as it is, based on the next operator.
     *
     * @param operator the current operator
     * @param next the next operator
     *
     * @return {@code true} if the operator parsing should terminate, {@code false} otherwise
     */
    private boolean shouldOperatorTerminate(@NotNull String operator, @NotNull String next) {
        switch (operator) {
            case "?":
                return !next.equals(".") && !next.equals("?");
            case "=":
            case "!":
                return !next.equals("=");
            case "&":
                return !next.equals("&");
            default:
                return false;
        }
    }
}
