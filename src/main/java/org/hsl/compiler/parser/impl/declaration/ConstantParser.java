package org.hsl.compiler.parser.impl.declaration;

import org.hsl.compiler.ast.impl.declaration.ConstantDeclare;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.parser.AstParser;
import org.hsl.compiler.parser.ParserAlgorithm;
import org.hsl.compiler.parser.ParserContext;
import org.hsl.compiler.token.Token;
import org.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

public class ConstantParser extends ParserAlgorithm<ConstantDeclare> {
    /**
     * Parse the next {@link ConstantDeclare} node from the token stream.
     *
     * @param parser  the AST node parser
     * @param context the token parser context
     * @return the next {@link ConstantDeclare} node from the token stream
     */
    @Override
    public @NotNull ConstantDeclare parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        // skip the `const` specifier
        get(TokenType.EXPRESSION, "const");

        // parse the constant name
        Token name = get(TokenType.IDENTIFIER);

        // skip the assignment sign
        get(TokenType.OPERATOR, "=");

        // parse the constant value
        Value value = parser.nextValue();

        // skip the semicolon after the declaration
        if (peek().is(TokenType.SEMICOLON))
            get();

        if (!context.currentAnnotations().isEmpty()) {
            context.syntaxError(context.currentAnnotations().getFirst().name(), "Annotations not allowed for constants");
            throw new UnsupportedOperationException("Annotations not allowed for constants");
        }

        return new ConstantDeclare(name, value);
    }
}
