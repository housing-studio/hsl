package org.housingstudio.hsl.compiler.parser.impl.declaration;

import org.housingstudio.hsl.compiler.ast.impl.declaration.ConstantDeclare;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
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
            Token token = context.currentAnnotations().get(0).name();
            context.error(
                Errno.UNEXPECTED_ANNOTATION_TARGET,
                "unexpected annotation target",
                token,
                "cannot declare annotations for constants"
            );
            throw new UnsupportedOperationException("Annotations not allowed for constants");
        }

        return new ConstantDeclare(name, value);
    }
}
