package org.housingstudio.hsl.compiler.parser.impl.value;

import org.housingstudio.hsl.compiler.ast.impl.type.BaseType;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

public class TypeParser extends ParserAlgorithm<BaseType> {
    /**
     * Parse the next {@link BaseType} node from the token stream.
     *
     * @param parser  the AST node parser
     * @param context the token parser context
     * @return the next {@link BaseType} node from the token stream
     */
    @Override
    public @NotNull BaseType parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        Token type = get(TokenType.TYPE);
        switch (type.value()) {
            case "int":
                return BaseType.INT;
            case "float":
                return BaseType.FLOAT;
            case "string":
                return BaseType.STRING;
            case "bool":
                return BaseType.BOOL;
            case "any":
                return BaseType.ANY;
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
