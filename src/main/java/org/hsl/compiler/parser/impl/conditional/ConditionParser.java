package org.hsl.compiler.parser.impl.conditional;

import org.hsl.compiler.ast.builder.ConditionBuilder;
import org.hsl.compiler.parser.AstParser;
import org.hsl.compiler.parser.ParserAlgorithm;
import org.hsl.compiler.parser.ParserContext;
import org.hsl.compiler.token.TokenType;
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

        context.syntaxError(peek(), "Unexpected condition");
        throw new UnsupportedOperationException("Unexpected condition");
    }
}
