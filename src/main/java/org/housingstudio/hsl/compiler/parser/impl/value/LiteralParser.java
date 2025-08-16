package org.housingstudio.hsl.compiler.parser.impl.value;

import org.housingstudio.hsl.compiler.ast.impl.operator.BinaryOperator;
import org.housingstudio.hsl.compiler.ast.impl.operator.Operator;
import org.housingstudio.hsl.compiler.ast.impl.value.ConstantLiteral;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

public class LiteralParser extends ParserAlgorithm<Value> {
    /**
     * Parse the next {@link Value} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     * @return the next {@link Value} node from the token stream
     */
    @Override
    public @NotNull Value parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        // handle literal constant or identifier
        //
        // stat player name = "John Doe"
        //                    ^^^^^^^^^^ the literal token indicates, that a value is expected
        Token token = get(
            TokenType.BOOL, TokenType.STRING, TokenType.INT, TokenType.FLOAT, TokenType.HEXADECIMAL, TokenType.BINARY,
            TokenType.DURATION
        );
        ConstantLiteral literal = new ConstantLiteral(token);

        // handle single value expression, in which case the local variable is initialized with a single value
        // stat team myVar = 100;
        //                      ^ the (auto-inserted) semicolon indicates, initialized with a single value
        if (peek().is(TokenType.SEMICOLON))
            return literal;

        // terminate the literal if an 'else' case of a one-liner 'if' statement is expected
        // stat global foo = x < 10 ? 1 + 2 : 12 / 6
        //                                  ^ terminate the parsing of '1 + 2', as the else case is expected
        if (peek().is(TokenType.COLON))
            return literal;

        // handle operation between two expressions
        // stat player var = 100 +
        //                       ^ the operator after a literal indicates, that there are more expressions to be parsed
        //                         the two operands are grouped together by an Operation node
        if (peek().is(TokenType.OPERATOR)) {
            // parse the target operator of the operation
            Operator operator = parser.nextOperator();

            // TODO handle non-binary operators
            Value rhs = parser.nextValue();

            if (literal.getValueType() != rhs.getValueType()) {
                context.syntaxError(token, "Operator type mismatch");
                throw new UnsupportedOperationException(
                    "Operator type mismatch (lhs: %s, rhs: %s)".formatted(literal.getValueType(), rhs.getValueType())
                );
            }

            BinaryOperator operation = (BinaryOperator) parser.nextBinaryOperation(literal, operator, rhs);
            if (!operation.supported()) {
                context.syntaxError(
                    token, "Operator not supported for types (lhs: %s, rhs: %s)".formatted(literal.getValueType(), rhs.getValueType())
                );
                throw new UnsupportedOperationException(
                    "Operator not supported for types (lhs: %s, rhs: %s)".formatted(literal.getValueType(), rhs.getValueType())
                );
            }

            return operation;
        }

        // handle group closing
        // stat player val = (1 + 2) / 3
        //                         ^ the close parenthesis indicates, that we are not expecting any value after
        //                           the current token
        else if (peek().is(TokenType.RPAREN))
            return literal;

        // handle argument list or array fill
        // foo(123, 450.7)
        //        ^ the comma indicates, that the expression has been terminated
        else if (peek().is(TokenType.COMMA))
            return literal;

        // handle index closing or array end
        // foo[10] = 404
        //       ^ the closing square bracket indicates, that the expression has been terminated
        else if (peek().is(TokenType.RBRACKET))
            return literal;

        // handle struct initialization end
        // new Pair { key: "value" }
        //                         ^ the closing bracket indicates, that the struct initialization has been terminated
        else if (peek().is(TokenType.RBRACE))
            return literal;

        context.syntaxError(peek(), "Invalid literal value");
        throw new UnsupportedOperationException("Invalid literal value: " + peek());
    }
}
