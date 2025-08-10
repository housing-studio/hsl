package org.hsl.parser;

import lombok.experimental.UtilityClass;
import org.hsl.compiler.parser.AstParser;
import org.hsl.compiler.parser.ParserContext;
import org.hsl.compiler.token.Token;
import org.hsl.tokenizer.Tokenizers;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@UtilityClass
public class Parsers {
    public @NotNull AstParser of(@NotNull String source) {
        List<Token> tokens = Tokenizers.tokenizeSource(source);
        ParserContext context = new ParserContext(tokens, source);

        return new AstParser(context);
    }
}
