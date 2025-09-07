package org.housingstudio.hsl.compiler.parser.impl.value;

import org.housingstudio.hsl.compiler.ast.impl.value.Value;
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
     * @param parser  the AST node parser
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


        // handle variable access
        // let name = otherName
        //            ^^^^^^^^^ the identifier token indicates, that a value is expected
        // foo()
        //    ^^ function calls have a similar signature, except there are parentheses at the end
        else if (peek().is(TokenType.IDENTIFIER))
            return parser.nextConstantAccess();

        context.syntaxError(peek(), "Invalid value");
        throw new UnsupportedOperationException("Unsupported value type: " + peek());
    }
}
