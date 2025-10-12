package org.housingstudio.hsl.compiler.parser.impl.value;

import org.housingstudio.hsl.compiler.ast.impl.operator.BinaryOperator;
import org.housingstudio.hsl.compiler.ast.impl.operator.Operator;
import org.housingstudio.hsl.compiler.ast.impl.value.Group;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

public class GroupParser extends ParserAlgorithm<Value> {
    /**
     * Parse the next {@link Value} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     *
     * @return the next {@link Value} node from the token stream
     */
    @Override
    public @NotNull Value parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        get(TokenType.LPAREN);
        Value content = parser.nextValue();
        get(TokenType.RPAREN);
        Group group = new Group(content);

        // handle operation between two expressions
        // stat player var = (1 + 2) + 3
        //                           ^ the operator after a group indicates, that there are more expressions to be parsed
        //                             the two operands are grouped together by an Operation node
        if (peek().is(TokenType.OPERATOR)) {
            if (!BinaryOperator.isBinaryOperator(peek()))
                return group;

            // parse the target operator of the operation
            Token operatorToken = peek(TokenType.OPERATOR);
            Operator operator = parser.nextOperator();

            // TODO handle non-binary operators
            Value rhs = parser.nextValue();

            return parser.nextBinaryOperation(group, operator, operatorToken, rhs);
        }

        return group;
    }
}
