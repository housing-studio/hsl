package org.hsl.compiler.parser.impl.action;

import org.hsl.compiler.ast.impl.value.Argument;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.parser.AstParser;
import org.hsl.compiler.parser.ParserAlgorithm;
import org.hsl.compiler.parser.ParserContext;
import org.hsl.compiler.parser.impl.value.MethodCall;
import org.hsl.compiler.token.Token;
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
        Token method = get();
        List<Argument> arguments = parser.nextArgumentList();

        return switch (method.value()) {
            default -> new MethodCall(method, arguments);
        };
    }
}
