package org.housingstudio.hsl.compiler.parser.impl.scope;

import org.housingstudio.hsl.compiler.ast.impl.statement.ArrayLoad;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

public class ArrayLoadParser extends ParserAlgorithm<Value> {
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
        Token name = get(TokenType.IDENTIFIER);

        get(TokenType.LBRACKET);
        Value index = parser.nextValue();
        get(TokenType.RBRACKET);

        return new ArrayLoad(name, index);
    }
}
