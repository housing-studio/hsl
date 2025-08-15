package org.hsl.compiler.parser.impl.scope;

import org.hsl.compiler.ast.Node;
import org.hsl.compiler.ast.impl.scope.Statement;
import org.hsl.compiler.ast.impl.value.Argument;
import org.hsl.compiler.parser.AstParser;
import org.hsl.compiler.parser.ParserAlgorithm;
import org.hsl.compiler.parser.ParserContext;
import org.hsl.compiler.parser.impl.value.MethodCall;
import org.hsl.compiler.token.Token;
import org.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
        //      ^ the `=` symbol must not follow another `=`, as that would be a comparing binary operator
        // ^^^ the identifier must be followed by a `=`
        if (
            peek().is(TokenType.IDENTIFIER) && at(cursor() + 1).is(TokenType.OPERATOR, "=") &&
            !at(cursor() + 2).is(TokenType.OPERATOR, "=")
        )
            return parser.nextLocalAssignment();

        // handle method call
        // chat("Hello, World!")
        //     ^ the parentheses after an identifier indicates, that a method is being called
        if (peek().is(TokenType.IDENTIFIER) && at(cursor() + 1).is(TokenType.LPAREN)) {
            Token method = get();
            List<Argument> arguments = parser.nextArgumentList();
            return new MethodCall(method, arguments);
        }

        // handle conditional
        if (peek().is(TokenType.EXPRESSION, "if"))
            return parser.nextConditional();

        context.syntaxError(peek(), "Invalid statement");
        throw new UnsupportedOperationException("Not implemented statement: " + peek());
    }
}
