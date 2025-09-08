package org.housingstudio.hsl.compiler.parser.impl.action;

import org.housingstudio.hsl.compiler.ast.impl.value.Argument;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.parser.impl.value.MacroCall;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MacroCallParser extends ParserAlgorithm<MacroCall> {
    /**
     * Parse the next {@link MacroCall} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     *
     * @return the next {@link MacroCall} node from the token stream
     */
    @Override
    public @NotNull MacroCall parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        Token macro = get(TokenType.IDENTIFIER);
        get(TokenType.OPERATOR, "!");
        List<Argument> arguments = parser.nextArgumentList();

        return new MacroCall(macro, arguments);
    }
}
