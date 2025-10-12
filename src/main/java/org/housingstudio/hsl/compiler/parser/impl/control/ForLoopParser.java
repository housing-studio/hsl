package org.housingstudio.hsl.compiler.parser.impl.control;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.control.ForLoop;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.codegen.builder.ConditionBuilder;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ForLoopParser extends ParserAlgorithm<Node> {
    /**
     * Parse the next {@link Node} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     *
     * @return the next {@link Node} node from the token stream
     */
    @Override
    public @NotNull Node parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        get(TokenType.EXPRESSION, "for");
        get(TokenType.LPAREN);

        Node init = null;
        if (!peek().is(TokenType.SEMICOLON))
            init = parser.nextStatement();
        else
            get(TokenType.SEMICOLON);

        List<ConditionBuilder> conditions = null;
        boolean matchAny = false;
        if (!peek().is(TokenType.SEMICOLON)) {
            Conditions cond = parseConditions(parser, context);
            conditions = cond.conditions();
            matchAny = cond.operator == Operator.OR;
        } else
            get(TokenType.SEMICOLON);

        Node step = null;
        if (!peek().is(TokenType.RPAREN))
            step = parser.nextStatement();

        get(TokenType.RPAREN);

        Scope body = parser.nextScope();

        if (peek().is(TokenType.SEMICOLON, "auto"))
            get(TokenType.SEMICOLON);

        return new ForLoop(init, conditions, matchAny, step, body);
    }

    private @NotNull Conditions parseConditions(@NotNull AstParser parser, @NotNull ParserContext context) {
        List<ConditionBuilder> conditions = new ArrayList<>();

        Operator operator = null;

        while (!peek().is(TokenType.SEMICOLON)) {
            conditions.add(parser.nextCondition());

            // TODO check for mixed operators in function actions only - does not effect macros

            if (peek().is(TokenType.OPERATOR, "&")) {
                if (operator == Operator.OR) {
                    context.errorPrinter().print(
                        Notification.error(Errno.CANNOT_MIX_OPERATORS, "illegal sequence of operators")
                            .error("cannot mix AND and OR operators in condition actions", peek())
                            .note("use either `foo && bar && baz` or `foo || bar || baz`")
                    );
                    throw new UnsupportedOperationException("Cannot mix AND and OR operators");
                }

                get(TokenType.OPERATOR, "&");
                get(TokenType.OPERATOR, "&");

                operator = Operator.AND;
            }

            else if (peek().is(TokenType.OPERATOR, "|")) {
                if (operator == Operator.AND) {
                    context.errorPrinter().print(
                        Notification.error(Errno.CANNOT_MIX_OPERATORS, "illegal sequence of operators")
                            .error("cannot mix AND and OR operators in condition actions", peek())
                            .note("use either `foo && bar && baz` or `foo || bar || baz`")
                    );
                    throw new UnsupportedOperationException("Cannot mix AND and OR operators");
                }

                get(TokenType.OPERATOR, "|");
                get(TokenType.OPERATOR, "|");
                operator = Operator.OR;
            }

            // condition list is terminated, exit the loop
            else
                break;
        }

        get(TokenType.SEMICOLON);

        return new Conditions(conditions, operator != null ? operator : Operator.AND);
    }

    @RequiredArgsConstructor
    @Accessors(fluent = true)
    @Getter
    private static class Conditions {
        private final @NotNull List<ConditionBuilder> conditions;
        private final @NotNull Operator operator;
    }

    private enum Operator {
        AND,
        OR
    }
}
