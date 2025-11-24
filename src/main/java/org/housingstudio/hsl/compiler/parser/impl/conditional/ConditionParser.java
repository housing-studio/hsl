package org.housingstudio.hsl.compiler.parser.impl.conditional;

import org.housingstudio.hsl.compiler.ast.impl.conditional.PlaceholderComparator;
import org.housingstudio.hsl.compiler.ast.impl.conditional.StatComparator;
import org.housingstudio.hsl.compiler.ast.impl.operator.Operator;
import org.housingstudio.hsl.compiler.ast.impl.placeholder.PlaceholderValue;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.codegen.builder.ConditionBuilder;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

public class ConditionParser extends ParserAlgorithm<ConditionBuilder> {
    /**
     * Parse the next {@link ConditionBuilder} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     * @return the next {@link ConditionBuilder} node from the token stream
     */
    @Override
    public @NotNull ConditionBuilder parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        // handle builtin condition methods
        if (peek().is(TokenType.IDENTIFIER) && at(cursor() + 1).is(TokenType.LPAREN))
            return new ConditionMethodCall(parser.nextMethodCall());

        // handle negate operator
        if (peek().is(TokenType.OPERATOR, "!")) {
            get();
            ConditionBuilder condition = parser.nextCondition();
            condition.invert();
            return condition;
        }

        // handle logical group
        if (peek().is(TokenType.LPAREN)) {
            get(TokenType.LPAREN);
            ConditionBuilder condition = parser.nextCondition();
            get(TokenType.RPAREN);
            return condition;
        }

        // handle variable comparators
        if (peek().is(TokenType.IDENTIFIER) && isComparator(at(cursor() + 1))) {
            Token lhs = get(TokenType.IDENTIFIER);

            Token operator = peek();
            Operator comparator = parser.nextOperator();

            if (!comparator.comparable()) {
                context.errorPrinter().print(
                    Notification.error(Errno.EXPECTED_COMPARATOR_OPERATOR, "expected comparator operator")
                        .error(
                            "expected comparator operator, but found `" + operator.value() + "`",
                            operator
                        )
                        .note("use comparator operators", "== != < <= > >=")
                );
            }

            Value rhs = parser.nextValue();
            return new StatComparator(lhs, comparator, rhs);
        }

        // handle placeholder comparators
        if (
            peek().is(TokenType.IDENTIFIER) && at(cursor() + 1).is(TokenType.COLON) &&
            at(cursor() + 2).is(TokenType.COLON)
        ) {
            Value lhs = parser.nextBuiltinValue();
            if (!(lhs instanceof PlaceholderValue))
                throw new IllegalArgumentException("Expected LHS to be a placeholder value");

            Token operator = peek();
            Operator comparator = parser.nextOperator();

            if (!comparator.comparable()) {
                context.errorPrinter().print(
                    Notification.error(Errno.EXPECTED_COMPARATOR_OPERATOR, "expected comparator operator")
                        .error(
                            "expected comparator operator, but found `" + operator.value() + "`",
                            operator
                        )
                        .note("use comparator operators", "== != < <= > >=")
                );
            }

            Value rhs = parser.nextValue();
            return new PlaceholderComparator((PlaceholderValue) lhs, comparator, rhs);
        }

        context.errorPrinter().print(
            Notification.error(Errno.UNKNOWN_CONDITION, "unexpected condition value")
                .error("expected condition, but found: " + peek().value(), peek())
        );
        throw new UnsupportedOperationException("Unexpected condition");
    }

    private boolean isComparator(@NotNull Token token) {
        if (!token.is(TokenType.OPERATOR))
            return false;

        switch (token.value()) {
            case ">":
            case "<":
            case "=":
            case "!":
                return true;
            default:
                return false;
        }
    }
}
