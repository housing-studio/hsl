package org.hsl.compiler.parser.impl.local;

import org.hsl.compiler.ast.Node;
import org.hsl.compiler.ast.impl.local.LocalAssign;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.parser.AstParser;
import org.hsl.compiler.parser.ParserAlgorithm;
import org.hsl.compiler.parser.ParserContext;
import org.hsl.compiler.token.Token;
import org.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

public class LocalAssignParser extends ParserAlgorithm<Node> {
    /**
     * Parse the next {@link Node} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     * @return the next {@link Node} node from the token stream
     */
    @Override
    public @NotNull Node parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        // resolve the name of the local variable
        // foo = "Bar"
        // ^^^ an IDENTIFIER token represents the target of the assignation
        Token name = get(TokenType.IDENTIFIER);

        // handle the equals sign
        // val = 123
        //     ^ the `=` symbol indicates, that an assignation is expected
        Token operator = get(TokenType.OPERATOR, "=");

        // parse the value of the local variable
        Value value = parser.nextValue();

        // skip the semicolon after the declaration
        if (peek().is(TokenType.SEMICOLON))
            get();

        return new LocalAssign(name, operator, value);
    }
}
