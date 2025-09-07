package org.housingstudio.hsl.compiler.parser.impl.value;

import org.housingstudio.hsl.compiler.ast.impl.value.InterpolatedString;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserAlgorithm;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenTransformer;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.housingstudio.hsl.compiler.token.Tokenizer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InterpolationParser extends ParserAlgorithm<Value> {
    /**
     * Parse the next {@link Value} node from the token stream.
     *
     * @param parser the AST node parser
     * @param context the token parser context
     * @return the next {@link Value} node from the token stream
     */
    @Override
    public @NotNull Value parse(@NotNull AstParser parser, @NotNull ParserContext context) {
        get(TokenType.OPERATOR, "$");
        Token value = get(TokenType.STRING);

        String content = value.value();

        List<Object> parts = new ArrayList<>();
        StringBuilder currentText = new StringBuilder();

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            // $"Hello {{world}}"
            //         ^^ double braces indicate, that the opening braces should be escaped
            if (c == '{' && i + 1 < content.length() && content.charAt(i + 1) == '{') {
                currentText.append('{');
                i++;
            }

            // $"foo {{bar}} baz"
            //            ^^ double braces indicate, that the closing braces should be escaped
            else if (c == '}' && i + 1 < content.length() && content.charAt(i + 1) == '}') {
                currentText.append('}');
                i++;
            }

            // $"you have total {points} points"
            //                  ^ single brace indicates expression start
            else if (c == '{') {
                // $"Total coins: {coins}."
                //   ^^^^^^^^^^^^^ flush captured text before interpolated expression
                if (currentText.length() > 0) {
                    parts.add(currentText.toString());
                    currentText.setLength(0);
                }

                int exprStart = i + 1;
                int braceCount = 1;
                i = exprStart;

                // parse each char of the expression until it is closed with another brace,
                // while ensuring that nested braces are accounted foré
                // $"Welcome, {player}"
                while (i < content.length() && braceCount > 0) {
                    char ch = content.charAt(i);
                    if (ch == '{') braceCount++;
                    else if (ch == '}') braceCount--;
                    i++;
                }

                // check if the opening and closing brace count does not match
                // $"{foo}}"
                //   ^
                //   expected one corresponding closing brace
                //       ^^ got two instead
                if (braceCount != 0)
                    throw new RuntimeException("Unbalanced braces in interpolated string");

                // capture the expression within braces and create a sub-parser for it
                // $"{1 + 2}"
                //    ^^^^^ the captured expression
                int exprEnd = i - 1; // exclusive
                String innerSource = content.substring(exprStart, exprEnd).trim();

                // create a parser for the captured value within the interpolated string
                AstParser subParser = createSubParser(context, innerSource);

                Value capturedValue = subParser.nextValue();
                parts.add(capturedValue);

                // value capturing overshoots by one index for some reason, shift to left once
                i--;
            }

            // handle trailing text
            else
                currentText.append(c);
        }

        // flush trailing text
        if (currentText.length() > 0)
            parts.add(currentText.toString());

        return new InterpolatedString(value, parts);
    }

    private @NotNull AstParser createSubParser(@NotNull ParserContext context, @NotNull String content) {
        Tokenizer tokenizer = new Tokenizer(context.file(), content);
        List<Token> tokens = new ArrayList<>();
        Token token;

        do {
            Token next = tokenizer.next();
            // value parsers cannot deal with EOF, as it is unexpected
            if (next.is(TokenType.EOF))
                break;

            tokens.add(token = next);
            if (token.is(TokenType.UNEXPECTED))
                throw new RuntimeException(token.value());
        } while (token.hasNext());

        // manipulate expression to have a semicolon at the end, in order for the value to terminate parsing
        tokens.add(Token.of(TokenType.SEMICOLON, "auto"));
        tokens = new TokenTransformer(tokens).transform();

        ParserContext subContext = new ParserContext(tokens, context.file(), content);
        return new AstParser(subContext);
    }
}
