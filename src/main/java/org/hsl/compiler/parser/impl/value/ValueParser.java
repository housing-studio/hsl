package org.hsl.compiler.parser.impl.value;

import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.parser.AstParser;
import org.hsl.compiler.parser.ParserAlgorithm;
import org.hsl.compiler.parser.ParserContext;
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

        /*
        // handle variable access
        // let name = otherName
        //            ^^^^^^^^^ the identifier token indicates, that a value is expected
        // let value = outer.inner
        //                  ^ identifiers may be chained to access nested values
        // foo()
        //    ^^ function calls have a similar signature, except there are parentheses at the end
        else if (peek().is(TokenType.IDENTIFIER))
            return parser.nextAccess();
         */

        context.syntaxError(peek(), "Invalid value");
        throw new UnsupportedOperationException("Unsupported value type: " + peek());
    }
}
