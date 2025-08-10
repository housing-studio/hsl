package org.hsl.compiler.parser.impl.declaration;

import org.hsl.compiler.ast.impl.declaration.Method;
import org.hsl.compiler.ast.impl.scope.Scope;
import org.hsl.compiler.parser.AstParser;
import org.hsl.compiler.parser.ParserAlgorithm;
import org.hsl.compiler.parser.ParserContext;
import org.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the parser algorithm for parsing a {@link Method} node from the token stream.
 * <p>
 * A method is a function that is defined in a specific context.
 * <p>
 * For example:
 * <pre>
 *     fn foo() {
 *         chat("Hello, World!")
 *     }
 * </pre>
 * The method `foo` is defined with the body of the method contains the statement {@code chat("Hello, World!")}.
 * <p>
 * The method is defined by the return type, the name of the method, the parameter list, and the body of the method.
 * <p>
 * The method can have multiple return types, that are placed in between parenthesis.
 *
 * @see Method
 */
public class MethodParser extends ParserAlgorithm<Method> {
    /**
     * Parse the next {@link Method} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     * @return the next {@link Method} node from the token stream
     */
    @Override
    public @NotNull Method parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        // skip the method declaration specifier
        get(TokenType.EXPRESSION, "fn");

        // parse the method name
        String name = context.get(TokenType.IDENTIFIER).value();

        // skip the parameter list
        get(TokenType.LPAREN);
        get(TokenType.RPAREN);

        Scope scope = parser.nextScope();

        return new Method(name, scope);
    }
}
