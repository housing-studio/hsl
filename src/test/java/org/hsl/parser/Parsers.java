package org.hsl.parser;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.parser.AstParser;
import org.housingstudio.hsl.compiler.parser.ParserContext;
import org.housingstudio.hsl.compiler.token.Token;
import org.hsl.tokenizer.Tokenizers;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@UtilityClass
public class Parsers {
    public @NotNull AstParser of(@NotNull String source) {
        List<Token> tokens = Tokenizers.tokenizeSource(source);
        ParserContext context = new ParserContext(tokens, source);
        Node.context(context);

        return new AstParser(context);
    }
}
