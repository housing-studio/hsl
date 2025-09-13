package org.housingstudio.hsl.compiler.parser.impl.declaration;

import org.housingstudio.hsl.compiler.ast.impl.declaration.Method;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.type.BaseType;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.ast.impl.value.Annotation;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
 * The method `foo` is defined with the body of a statement {@code chat("Hello, World!")}.
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
        Token name = context.get(TokenType.IDENTIFIER);

        // skip the parameter list
        get(TokenType.LPAREN);
        get(TokenType.RPAREN);

        Type returnType = Types.VOID;
        if (peek().is(TokenType.OPERATOR, "-") && at(cursor() + 1).is(TokenType.OPERATOR, ">")) {
            get();
            get();
            returnType = parser.nextType();
        }

        if (!returnType.matches(Types.VOID)) {
            context.syntaxError(
                at(cursor() - 1), "non-void function return type is currently not supported"
            );
            throw new UnsupportedOperationException("non-void function return type is currently not supported");
        }

        Scope scope = parser.nextScope();
        ArrayList<Annotation> annotations = new ArrayList<>(context.currentAnnotations());
        context.currentAnnotations().clear();

        return new Method(annotations, name, returnType, new ArrayList<>(), scope);
    }
}
