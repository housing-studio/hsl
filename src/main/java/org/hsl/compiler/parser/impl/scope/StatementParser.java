package org.hsl.compiler.parser.impl.scope;

import org.hsl.compiler.ast.Node;
import org.hsl.compiler.ast.impl.scope.Statement;
import org.hsl.compiler.parser.AstParser;
import org.hsl.compiler.parser.ParserAlgorithm;
import org.hsl.compiler.parser.ParserContext;
import org.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a parser algorithm that parses a statement node from the token stream.
 *
 * @see Statement
 */
public class StatementParser extends ParserAlgorithm<Node> {
    /**
     * Parse the next {@link Statement} node from the token stream.
     *
     * @param parser  the AST node parser
     * @param context the token parser context
     * @return the next {@link Statement} node from the token stream
     */
    @Override
    public @NotNull Node parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        if (peek().is(TokenType.EXPRESSION, "stat"))
            return parser.nextLocalDeclaration();

        context.syntaxError(peek(), "Invalid statement");
        throw new UnsupportedOperationException("Not implemented statement: " + peek());
    }
}
