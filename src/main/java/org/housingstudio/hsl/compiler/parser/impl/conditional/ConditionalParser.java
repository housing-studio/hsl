package org.housingstudio.hsl.compiler.parser.impl.conditional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.codegen.builder.ConditionBuilder;
import org.housingstudio.hsl.compiler.ast.impl.control.ConditionalNode;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Errno;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ConditionalParser extends ParserAlgorithm<ConditionalNode> {
    /**
     * Parse the next {@link ConditionalNode} node from the token stream.
     *
     * @param parser  the AST node parser
     * @param context the token parser context
     * @return the next {@link ConditionalNode} node from the token stream
     */
    @Override
    public @NotNull ConditionalNode parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        get(TokenType.EXPRESSION, "if");

        Conditions conditions = parseConditions(parser, context);
        Scope ifScope = parser.nextScope();
        Scope elseScope = null;

        if (peek().is(TokenType.EXPRESSION, "else")) {
            get(TokenType.EXPRESSION, "else");
            elseScope = parser.nextScope();
        }

        return new ConditionalNode(
            conditions.conditions(), conditions.operator == Operator.OR, ifScope, elseScope
        );
    }

    private @NotNull Conditions parseConditions(@NotNull AstParser parser, @NotNull ParserContext context) {
        List<ConditionBuilder> conditions = new ArrayList<>();

        Operator operator = null;
        get(TokenType.LPAREN);

        while (!peek().is(TokenType.RPAREN)) {
            conditions.add(parser.nextCondition());

            // TODO check for mixed operators in function actions only - does not effect macros

            if (peek().is(TokenType.OPERATOR, "&")) {
                if (operator == Operator.OR) {
                    context.error(
                        Errno.CANNOT_MIX_OPERATORS,
                        "illegal sequence of operators",
                        peek(),
                        "cannot mix AND and OR operators in condition actions"
                    );
                    throw new UnsupportedOperationException("Cannot mix AND and OR operators");
                }

                get(TokenType.OPERATOR, "&");
                get(TokenType.OPERATOR, "&");

                operator = Operator.AND;
            }

            else if (peek().is(TokenType.OPERATOR, "|")) {
                if (operator == Operator.AND) {
                    context.error(
                        Errno.CANNOT_MIX_OPERATORS,
                        "illegal sequence of operators",
                        peek(),
                        "cannot mix AND and OR operators in condition actions"
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

        get(TokenType.RPAREN);
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
