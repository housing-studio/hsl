package org.housingstudio.hsl.compiler.parser.impl.action;

import org.housingstudio.hsl.compiler.ast.impl.value.Argument;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.ast.impl.value.MethodCall;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MethodCallParser extends ParserAlgorithm<Value> {
    /**
     * Parse the next {@link Value} node from the token stream.
     *
     * @param parser  the AST node parser
     * @param context the token parser context
     * @return the next {@link Value} node from the token stream
     */
    @Override
    public @NotNull Value parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        Token method = get(TokenType.IDENTIFIER);
        List<Argument> arguments = parser.nextArgumentList();

        return new MethodCall(method, arguments);
    }
}
