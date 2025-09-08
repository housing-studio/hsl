package org.housingstudio.hsl.compiler.parser.impl.conditional;

import org.housingstudio.hsl.compiler.ast.builder.ConditionBuilder;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Errno;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

public class ConditionParser extends ParserAlgorithm<ConditionBuilder> {
    /**
     * Parse the next {@link ConditionBuilder} node from the token stream.
     *
     * @param parser  the AST node parser
     * @param context the token parser context
     * @return the next {@link ConditionBuilder} node from the token stream
     */
    @Override
    public @NotNull ConditionBuilder parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        if (peek().is(TokenType.IDENTIFIER) && at(cursor() + 1).is(TokenType.LPAREN))
            return new ConditionMethodCall(parser.nextMethodCall());

        if (peek().is(TokenType.OPERATOR, "!")) {
            get();
            ConditionBuilder condition = parser.nextCondition();
            condition.invert();
            return condition;
        }

        context.error(
            Errno.UNKNOWN_CONDITION,
            "unexpected condition value",
            peek(),
            "expected condition, but found: " + peek().value()
        );
        throw new UnsupportedOperationException("Unexpected condition");
    }
}
