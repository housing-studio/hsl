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
        // handle local variable declaration
        if (peek().is(TokenType.EXPRESSION, "stat"))
            return parser.nextLocalDeclaration();

        // handle variable assignment
        // foo = 123
        //     ^ the `=` symbol after an identifier indicates, that a variable is assigned
        // ^^^ the identifier must be followed by a `=`
        //       ^ the `=` symbol must not follow another `=`, as that would be a comparing binary operator
        if (
            peek().is(TokenType.IDENTIFIER) && at(cursor() + 1).is(TokenType.OPERATOR, "=") &&
            !at(cursor() + 2).is(TokenType.OPERATOR, "=")
        )
            return parser.nextLocalAssignment();

        context.syntaxError(peek(), "Invalid statement");
        throw new UnsupportedOperationException("Not implemented statement: " + peek());
    }
}
