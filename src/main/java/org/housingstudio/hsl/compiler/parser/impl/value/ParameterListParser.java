package org.housingstudio.hsl.compiler.parser.impl.value;

import org.housingstudio.hsl.compiler.ast.impl.declaration.Parameter;
import org.housingstudio.hsl.compiler.ast.impl.type.BaseType;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ParameterListParser extends ParserAlgorithm<List<Parameter>> {
    /**
     * Parse the next {@link List} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     *
     * @return the next {@link List} node from the token stream
     */
    @Override
    public @NotNull List<Parameter> parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        get(TokenType.LPAREN);

        List<Parameter> parameters = new ArrayList<>();
        while (!peek().is(TokenType.RPAREN)) {
            Token name = get(TokenType.IDENTIFIER);
            get(TokenType.COLON);
            BaseType type = parser.nextType();

            Value defaultValue = null;
            if (peek().is(TokenType.OPERATOR, "=")) {
                get();
                defaultValue = parser.nextValue();
            }

            parameters.add(new Parameter(name, type, defaultValue));

            // handle more parameters
            // foo(a: int, b: int)
            //           ^ the comma indicates, that there are more arguments to be parsed
            if (peek().is(TokenType.COMMA))
                get(TokenType.COMMA);

            // parameter list is terminated, exit the loop
            else
                break;
        }

        get(TokenType.RPAREN);
        return parameters;
    }
}
