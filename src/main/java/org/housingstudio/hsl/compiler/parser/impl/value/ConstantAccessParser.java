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

        // TODO handle method call

        ConstantAccess access = new ConstantAccess(first);

        // TODO handle operator after access

        // handle method call
        // chat("Hello, World!")
        //     ^ the parentheses after an identifier indicates, that a method is being called
        if (peek().is(TokenType.LPAREN)) {
            List<Argument> arguments = parser.nextArgumentList();
            return new MethodCall(first, arguments);
        }

        // handle operation between two expressions
        // stat player var = 100 +
        //                       ^ the operator after a literal indicates, that there are more expressions to be parsed
        //                         the two operands are grouped together by an Operation node
        if (peek().is(TokenType.OPERATOR)) {
            if (!BinaryOperator.isBinaryOperator(peek()))
                return access;

            // parse the target operator of the operation
            Operator operator = parser.nextOperator();

            // TODO handle non-binary operators
            Value rhs = parser.nextValue();

            if (!access.getValueType().matches(rhs.getValueType())) {
                context.errorPrinter().print(
                    Notification.error(Errno.OPERATOR_TYPE_MISMATCH, "operator type mismatch")
                        .error(
                            String.format(
                                "operator type mismatch (lhs: %s, rhs: %s)", access.getValueType().print(),
                                rhs.getValueType().print()
                            ),
                            first
                        )
                        .note("make sure LHS and RHS are the same type")
                );
                throw new UnsupportedOperationException(
                    String.format(
                        "Operator type mismatch (lhs: %s, rhs: %s)",
                        access.getValueType().print(), rhs.getValueType().print()
                    )
                );
            }

            BinaryOperator operation = (BinaryOperator) parser.nextBinaryOperation(access, operator, rhs);
            if (!operation.supported()) {
                context.errorPrinter().print(
                    Notification.error(Errno.UNEXPECTED_OPERAND, "unexpected operand")
                        .error("operator not supported for type: " + access.getValueType(), first)
                );
                throw new UnsupportedOperationException(
                    "Operator not supported for type: " + access.getValueType().print()
                );
            }

            return operation;
        }

        return access;
    }
}
