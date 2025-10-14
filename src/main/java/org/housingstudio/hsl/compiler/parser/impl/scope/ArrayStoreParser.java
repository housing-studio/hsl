package org.housingstudio.hsl.compiler.parser.impl.scope;

import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.statement.ArrayStore;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

public class ArrayStoreParser extends ParserAlgorithm<Node> {
    /**
     * Parse the next {@link Node} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     *
     * @return the next {@link Node} node from the token stream
     */
    @Override
    public @NotNull Node parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        Token name = get(TokenType.IDENTIFIER);

        get(TokenType.LBRACKET);
        Value index = parser.nextValue();
        get(TokenType.RBRACKET);

        get(TokenType.OPERATOR, "=");

        Value value = parser.nextValue();

        return new ArrayStore(name, index, value);
    }
}
