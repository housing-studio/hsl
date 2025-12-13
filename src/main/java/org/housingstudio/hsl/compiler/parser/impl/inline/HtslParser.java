package org.housingstudio.hsl.compiler.parser.impl.inline;

import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

public class HtslParser extends ParserAlgorithm<Node> {
    /**
     * Parse the next {@link Node} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     * @return the next {@link Node} node from the token stream
     */
    @Override
    public @NotNull Node parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        get(TokenType.IDENTIFIER, "htsl");
        get(TokenType.LBRACE);

        while (!peek().is(TokenType.RBRACE)) {

        }

        get(TokenType.RBRACE);

        return null;
    }
}
