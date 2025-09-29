package org.housingstudio.hsl.compiler.parser.impl.operator;

import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.operator.Operator;
import org.housingstudio.hsl.compiler.ast.impl.operator.PostfixOperator;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

public class PostfixOperatorParser extends ParserAlgorithm<Node> {
    /**
     * Parse the next {@link Node} node from the token stream.
     *
     * @param parser  the AST node parser
     * @param context the token parser context
     * @return the next {@link Node} node from the token stream
     */
    @Override
    public @NotNull Node parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        Token identifier = get(TokenType.IDENTIFIER);

        Token token = peek(TokenType.OPERATOR);
        Operator operator = parser.nextOperator();

        if (!operator.postfix()) {
            context.errorPrinter().print(
                Notification.error(Errno.INVALID_POSTFIX_OPERATOR, "unexpected postfix operator")
                    .error("expected postfix operator, but found", token)
                    .note("use `++` and `--` instead")
            );
            throw new IllegalStateException("Unexpected postfix operator: " + operator);
        }

        return new PostfixOperator(identifier, operator);
    }
}
