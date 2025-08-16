package org.hsl.tokenizer;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenTransformer;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.housingstudio.hsl.compiler.token.Tokenizer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class Tokenizers {
    public List<Token> tokenizeSource(String source) {
        Tokenizer tokenizer = new Tokenizer(new File("example.hsl"), source);
        List<Token> tokens = new ArrayList<>();
        Token token;

        do {
            tokens.add(token = tokenizer.next());
            if (token.is(TokenType.UNEXPECTED))
                throw new RuntimeException(token.value());
        } while (token.hasNext());

        return new TokenTransformer(tokens).transform();
    }
}
