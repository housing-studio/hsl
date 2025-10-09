package org.housingstudio.hsl.compiler.parser.impl.operator;

import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.operator.Operator;
import org.housingstudio.hsl.compiler.ast.impl.operator.PrefixOperator;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

public class PrefixOperatorParser extends ParserAlgorithm<Value> {
    /**
     * Parse the next {@link Node} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     * @return the next {@link Node} node from the token stream
     */
    @Override
    public @NotNull Value parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        Token token = peek(TokenType.OPERATOR);
        Operator operator = parser.nextOperator();

        if (!operator.prefix()) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_POSTFIX_OPERATOR, "unexpected prefix operator")
                    .error("expected prefix operator, but found `" + operator.value() + "`", token)
                    .note("use `++`, `--`, `!` or `-` instead")
            );
            throw new IllegalStateException("Unexpected prefix operator: " + operator);
        }

        return new PrefixOperator(operator, parser.nextValue());
    }
}
