package org.housingstudio.hsl.compiler.parser.impl.value;

import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
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
        switch (type.value()) {
            case "int":
                return Type.INT;
            case "float":
                return Type.FLOAT;
            case "string":
                return Type.STRING;
            case "bool":
                return Type.BOOL;
            case "any":
                return Type.ANY;
            default:
                context.error(
                    Errno.UNEXPECTED_TYPE,
                    "unexpected type value",
                    type,
                    "invalid type name"
                );
                throw new UnsupportedOperationException("Invalid type name: " + type.value());
        }
    }
}
