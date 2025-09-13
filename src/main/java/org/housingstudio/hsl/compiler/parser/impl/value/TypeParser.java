package org.housingstudio.hsl.compiler.parser.impl.value;

import org.housingstudio.hsl.compiler.ast.impl.type.ArrayType;
import org.housingstudio.hsl.compiler.ast.impl.type.BaseType;
import org.housingstudio.hsl.compiler.ast.impl.type.StaticType;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TypeParser extends ParserAlgorithm<Type> {
    /**
     * Parse the next {@link Type} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     *
     * @return the next {@link Type} node from the token stream
     */
    @Override
    public @NotNull Type parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        int dimensions = 0;
        List<Token> dimensionTokens = new ArrayList<>();
        while (peek().is(TokenType.LBRACKET)) {
            dimensionTokens.add(get(TokenType.LBRACKET));
            dimensionTokens.add(get(TokenType.RBRACKET));
            dimensions++;
        }

        Token typeToken = get(TokenType.TYPE);
        BaseType type;
        switch (typeToken.value()) {
            case "int":
                type = BaseType.INT;
                break;
            case "float":
                type = BaseType.FLOAT;
                break;
            case "string":
                type = BaseType.STRING;
                break;
            case "bool":
                type = BaseType.BOOL;
                break;
            case "any":
                type = BaseType.ANY;
                break;
            default:
                context.error(
                    Errno.UNEXPECTED_TYPE, "unexpected type value", typeToken, "invalid type name"
                );
                throw new UnsupportedOperationException("Invalid type name: " + typeToken.value());
        }

        if (dimensions > 0)
            return new ArrayType(type, typeToken, dimensions, dimensionTokens);

        return new StaticType(type, typeToken);
    }
}
