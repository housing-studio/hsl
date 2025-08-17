package org.hsl.tokenizer;

import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TokenTest {
    @Test
    public void testTokenize() {
        String source =
            "fn foo() {\n" +
            "    stat player kills: int\n" +
            "    kills++\n" +
            "    chat(\"Hello, World!\")\n" +
            "}\n";

        List<Token> expect = Arrays.asList(
            Token.of(TokenType.EXPRESSION, "fn"),
            Token.of(TokenType.IDENTIFIER, "foo"),
            Token.of(TokenType.LPAREN, "("),
            Token.of(TokenType.RPAREN, ")"),
            Token.of(TokenType.LBRACE, "{"),
            Token.of(TokenType.EXPRESSION, "stat"),
            Token.of(TokenType.IDENTIFIER, "player"),
            Token.of(TokenType.IDENTIFIER, "kills"),
            Token.of(TokenType.COLON, ":"),
            Token.of(TokenType.TYPE, "int"),
            Token.of(TokenType.IDENTIFIER, "kills"),
            Token.of(TokenType.OPERATOR, "+"),
            Token.of(TokenType.OPERATOR, "+"),
            Token.of(TokenType.IDENTIFIER, "chat"),
            Token.of(TokenType.LPAREN, "("),
            Token.of(TokenType.STRING, "Hello, World!"),
            Token.of(TokenType.RPAREN, ")"),
            Token.of(TokenType.SEMICOLON, "auto"),
            Token.of(TokenType.RBRACE, "}"),
            Token.of(TokenType.SEMICOLON, "auto"),
            Token.of(TokenType.EOF)
        );

        List<Token> tokens = Tokenizers.tokenizeSource(source);
        assertIterableEquals(expect, tokens);
    }

    @Test
    public void testFail() {
        String source =
            "fn main() {\n" +
            "    \"Hello, World\n" +
            "}\n";

        assertThrows(RuntimeException.class, () -> Tokenizers.tokenizeSource(source));
    }
}
