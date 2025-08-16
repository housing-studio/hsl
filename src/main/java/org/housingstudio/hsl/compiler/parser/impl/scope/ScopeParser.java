package org.housingstudio.hsl.compiler.parser.impl.scope;

import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the parser algorithm for parsing a {@link Scope} node from the token stream.
 * <p>
 * A scope is a block of statements that can be executed in a specific context.
 * <p>
 * For example:
 * <pre>
 *     fn foo() {
 *         stat player value: int = 42;
 *     }
 * </pre>
 * The scope of the function `foo` contains the statement `stat player value: int = 42;`.
 * <p>
 * The scope is defined by the `{` and `}` characters.
 * <p>
 * The scope can contain multiple statements, that are executed in the order they are defined.
 * <p>
 * The scope can also contain other scopes, that are executed in the order they are defined.
 *
 * @see Scope
 */
public class ScopeParser extends ParserAlgorithm<Scope> {
    /**
     * Parse the next {@link Scope} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     * @return the next {@link Scope} node from the token stream
     */
    @Override
    public @NotNull Scope parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        // handle scope beginning
        // fn foo() {
        //          ^ the `{` char indicates, that the parser should enter a new scope
        get(TokenType.LBRACE);

        // parse the list of statements in the scope
        List<Node> statements = new ArrayList<>();
        while (!peek().is(TokenType.RBRACE)) {
            // parse the next statement of the scope
            Node statement = parser.nextStatement();

            // ignore unexpected auto-inserted semicolon
            if (peek().is(TokenType.SEMICOLON, "auto")) {
                get();
                //continue;
            }

            // break parsing, if the statement is an error or EOF
            if (!statement.hasNext())
                break;

            // register the statement for the scope
            statements.add(statement);
        }

        // handle scope ending
        // fn foo() { ... }
        //                ^ the `}` char indicates, that the parser should exit the current scope
        get(TokenType.RBRACE);

        // skip the semicolon after the scope
        // fn foo() { ... };
        //                 ^ the (auto-inserted) semicolon does not mean anything here, we just have to remove it
        //                   because it will be an unexpected token in the next parsing step
        if (peek().is(TokenType.SEMICOLON))
            get();

        return new Scope(statements);
    }
}
