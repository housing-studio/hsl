package org.hsl.compiler.parser.impl.value;

import org.hsl.compiler.ast.impl.value.ConstantAccess;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.parser.AstParser;
import org.hsl.compiler.parser.ParserAlgorithm;
import org.hsl.compiler.parser.ParserContext;
import org.hsl.compiler.token.Token;
import org.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

public class ConstantAccessParser extends ParserAlgorithm<Value> {
    /**
     * Parse the next {@link Value} node from the token stream.
     *
     * @param parser  the AST node parser
     * @param context the token parser context
     * @return the next {@link Value} node from the token stream
     */
    @Override
    public @NotNull Value parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        // handle the first part of the access chain
        // Location::Custom = (POS_X, POS_Y, POS_Z)
        //                     ^^^^^ the identifier token indicates, that a value access is expected
        Token first = get(TokenType.IDENTIFIER);

        // TODO handle method call

        ConstantAccess access = new ConstantAccess(first);

        // TODO handle operator after access

        return access;
    }
}
