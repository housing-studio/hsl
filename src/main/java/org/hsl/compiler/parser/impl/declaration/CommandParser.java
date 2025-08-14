package org.hsl.compiler.parser.impl.declaration;

import org.hsl.compiler.ast.impl.declaration.CommandNode;
import org.hsl.compiler.ast.impl.declaration.Method;
import org.hsl.compiler.ast.impl.scope.Scope;
import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.ast.impl.value.Annotation;
import org.hsl.compiler.parser.AstParser;
import org.hsl.compiler.parser.ParserAlgorithm;
import org.hsl.compiler.parser.ParserContext;
import org.hsl.compiler.token.Token;
import org.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommandParser extends ParserAlgorithm<CommandNode> {
    /**
     * Parse the next {@link Method} node from the token stream.
     *
     * @param parser  the AST node parser
     * @param context the token parser context
     * @return the next {@link Method} node from the token stream
     */
    @Override
    public @NotNull CommandNode parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        // skip the command declaration specifier
        get(TokenType.EXPRESSION, "command");


        // parse the method name
        Token name = context.get(TokenType.IDENTIFIER);

        // skip the parameter list
        get(TokenType.LPAREN);
        get(TokenType.RPAREN);

        Scope scope = parser.nextScope();
        ArrayList<Annotation> annotations = new ArrayList<>(context.currentAnnotations());
        context.currentAnnotations().clear();

        return new CommandNode(annotations, name, scope);
    }
}
