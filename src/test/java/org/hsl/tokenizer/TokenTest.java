package org.hsl.tokenizer;

import org.hsl.compiler.token.Token;
import org.hsl.compiler.token.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TokenTest {
    @Test
    public void testTokenize() {
        String source =
            """
            fn foo() {
                stat player kills: int
                kills++

                chat("Hello, World!")
            }
            """;

        List<Token> expect = List.of(
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
            """
            fn main() {
                "Hello, World
            }
            """;

        assertThrows(RuntimeException.class, () -> Tokenizers.tokenizeSource(source));
    }
}
