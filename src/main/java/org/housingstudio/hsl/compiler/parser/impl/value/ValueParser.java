package org.housingstudio.hsl.compiler.parser.impl.value;

import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a parser algorithm that parses a value node from the token stream.
 *
 * @see Value
 */
public class ValueParser extends ParserAlgorithm<Value> {
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
        // handle literal constant value
        // stat player name = "John Doe"
        //                    ^^^^^^^^^^ the literal token indicates, that a value is expected
        if (peek().isLiteral())
            return parser.nextLiteral();

        else if (peek().is(TokenType.OPERATOR, "-"))
            return parser.nextPrefixOperator();

        // handle explicit type conversion
        else if (
            (
                // either is a builtin type token
                peek().is(TokenType.TYPE) ||
                // or a special identifier that could be a builtin type
                (peek().is(TokenType.IDENTIFIER) && TypeParser.isTypeIdentifier(peek().value()))
            ) && at(cursor() + 1).is(TokenType.LPAREN)
        )
            return parser.nextConversion();

        // handle string interpolation
        // chat($"stat: {var1}")
        //      ^^^^^^^^^^^^^^^ string starting with dollar sign is going to be interpolated
        else if (peek().is(TokenType.OPERATOR, "$"))
            return parser.nextInterpolation();

        // handle builtin value
        // const TEST_LOCATION = Location::Custom(10, 10, 10)
        //                       ^^^^^^^^^^ the identifier follows two colons
        if (
            peek().is(TokenType.IDENTIFIER) && at(cursor() + 1).is(TokenType.COLON) &&
            at(cursor() + 2).is(TokenType.COLON)
        )
            return parser.nextBuiltinValue();

        // handle macro call
        // sum!(10, 20)
        else if (peek().is(TokenType.IDENTIFIER) && at(cursor() + 1).is(TokenType.OPERATOR, "!"))
            return parser.nextMacroCall();

        // handle variable access
        // stat player name = otherName
        //            ^^^^^^^^^ the identifier token indicates, that a value is expected
        // foo()
        //    ^^ function calls have a similar signature, except there are parentheses at the end
        else if (peek().is(TokenType.IDENTIFIER))
            return parser.nextConstantAccess();

        // handle group expression
        // stat player value = (1 + 2) + 3
        //                      ^ the opening paren indicates, that a group assignment start
        else if (peek().is(TokenType.LPAREN))
            return parser.nextGroup();

        context.errorPrinter().print(
            Notification.error(Errno.UNEXPECTED_TOKEN, "unexpected value")
                .error("token " + peek().print() + " is not a value", peek())
        );
        throw new UnsupportedOperationException("Unsupported value type: " + peek());
    }
}
