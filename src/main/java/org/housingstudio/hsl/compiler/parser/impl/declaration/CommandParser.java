package org.housingstudio.hsl.compiler.parser.impl.declaration;

import org.housingstudio.hsl.compiler.ast.impl.declaration.CommandNode;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Method;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.value.Annotation;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
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
