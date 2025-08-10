package org.hsl.compiler.parser.impl.declaration;

import org.hsl.compiler.ast.impl.declaration.Constant;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.parser.AstParser;
import org.hsl.compiler.parser.ParserAlgorithm;
import org.hsl.compiler.parser.ParserContext;
import org.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

public class ConstantParser extends ParserAlgorithm<Constant> {
    /**
     * Parse the next {@link Constant} node from the token stream.
     *
     * @param parser  the AST node parser
     * @param context the token parser context
     * @return the next {@link Constant} node from the token stream
     */
    @Override
    public @NotNull Constant parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        // skip the `const` specifier
        get(TokenType.EXPRESSION, "const");

        // parse the constant name
        String name = get(TokenType.IDENTIFIER).value();

        // skip the assignment sign
        get(TokenType.OPERATOR, "=");

        // parse the constant value
        Value value = parser.nextValue();

        // skip the semicolon after the declaration
        if (peek().is(TokenType.SEMICOLON))
            get();

        return new Constant(name, value);
    }
}
