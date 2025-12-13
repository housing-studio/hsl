package org.housingstudio.hsl.compiler.parser.impl.value;

import org.housingstudio.hsl.compiler.ast.impl.operator.BinaryOperator;
import org.housingstudio.hsl.compiler.ast.impl.operator.Operator;
import org.housingstudio.hsl.compiler.ast.impl.value.Argument;
import org.housingstudio.hsl.compiler.ast.impl.value.ConstantAccess;
import org.housingstudio.hsl.compiler.ast.impl.value.MethodCall;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConstantAccessParser extends ParserAlgorithm<Value> {
    /**
     * Parse the next {@link Value} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     * @return the next {@link Value} node from the token stream
     */
    @Override
    public @NotNull Value parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        // handle the first part of the access chain
        // Location::Custom = (POS_X, POS_Y, POS_Z)
        //                     ^^^^^ the identifier token indicates, that a value access is expected
        Token first = get(TokenType.IDENTIFIER);

        // handle method call
        // chat("Hello, World!")
        //     ^ the parentheses after an identifier indicates, that a method is being called
        if (peek().is(TokenType.LPAREN)) {
            List<Argument> arguments = parser.nextArgumentList();
            return new MethodCall(first, arguments);
        }

        // constant lookup node is created after deciding whether it is a method call or not
        // because in case it is a method call, then an invalid constant access would be made and tracked in the
        // ast visitor, causing an unknown variable error
        ConstantAccess access = new ConstantAccess(first);

        // handle operation between two expressions
        // stat player var = 100 +
        //                       ^ the operator after a literal indicates, that there are more expressions to be parsed
        //                         the two operands are grouped together by an Operation node
        if (peek().is(TokenType.OPERATOR)) {
            if (!BinaryOperator.isBinaryOperator(peek()))
                return access;

            // parse the target operator of the operation
            Token operatorToken = peek(TokenType.OPERATOR);
            Operator operator = parser.nextOperator();

            // TODO handle non-binary operators
            Value rhs = parser.nextValue();

            return parser.nextBinaryOperation(access, operator, operatorToken, rhs);
        }

        return access;
    }
}
