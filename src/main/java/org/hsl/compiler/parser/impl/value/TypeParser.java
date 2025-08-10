package org.hsl.compiler.parser.impl.value;

import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.parser.AstParser;
import org.hsl.compiler.parser.ParserAlgorithm;
import org.hsl.compiler.parser.ParserContext;
import org.hsl.compiler.token.Token;
import org.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

public class TypeParser extends ParserAlgorithm<Type> {
    /**
     * Parse the next {@link Type} node from the token stream.
     *
     * @param parser  the AST node parser
     * @param context the token parser context
     * @return the next {@link Type} node from the token stream
     */
    @Override
    public @NotNull Type parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        Token type = get(TokenType.TYPE);
        return switch (type.value()) {
            case "int" -> Type.INT;
            case "float" -> Type.FLOAT;
            case "string" -> Type.STRING;
            case "bool" -> Type.BOOL;
            case "any" -> Type.ANY;
            default -> {
                context.syntaxError(type, "Invalid type name");
                throw new UnsupportedOperationException("Invalid type name: " + type.value());
            }
        };
    }
}
