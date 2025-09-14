package org.housingstudio.hsl.compiler.parser.impl.value;

import org.housingstudio.hsl.compiler.ast.impl.value.Argument;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ArgumentListParser extends ParserAlgorithm<List<Argument>> {
    /**
     * Parse the next {@link List<Argument>} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     *
     * @return the next {@link List<Argument>} node from the token stream
     */
    @Override
    public @NotNull List<Argument> parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        get(TokenType.LPAREN);

        List<Argument> arguments = new ArrayList<>();
        boolean namedArgsStarted = false;
        while (!peek().is(TokenType.RPAREN)) {
            // parse the next argument
            // foo(abc, 123)
            //     ^^^ the next argument is expected before the closing parentheses
            Token name = null;
            if (peek().is(TokenType.IDENTIFIER) && at(cursor() + 1).is(TokenType.OPERATOR, "=")) {
                namedArgsStarted = true;
                name = get(TokenType.IDENTIFIER);
                get(TokenType.OPERATOR, "=");
            }

            // handle misuse of named arguments
            else if (namedArgsStarted) {
                context.error(
                    Errno.POSITIONAL_ARGUMENT_AFTER_NAMED_ARGUMENT,
                    "Positional argument after named argument",
                    peek(),
                    "Positional arguments are not allowed after named arguments"
                );
                throw new UnsupportedOperationException("Positional argument after named argument");
            }

            Value value = parser.nextValue();
            arguments.add(new Argument(name, value));

            // handle more arguments
            // foo(1, 2, 3)
            //      ^ the comma indicates, that there are more arguments to be parsed
            if (peek().is(TokenType.COMMA))
                get(TokenType.COMMA);

            // argument list is terminated, exit the loop
            else
                break;
        }

        get(TokenType.RPAREN);

        return arguments;
    }
}
