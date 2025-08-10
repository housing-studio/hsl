package org.hsl.compiler.token;

/**
 * Represents an enumeration of parsable the token types.
 *
 * @author AdvancedAntiSkid
 */
public enum TokenType {
    /**
     * `STRING` represents a literal wrapped in double quotes.
     * Example: {@code "text"}
     */
    STRING,

    /**
     * `BEGIN` represents the opening curly brace.
     * Example: <code>{</code>
     */
    LBRACE,

    /**
     * `END` represents the closing curly brace.
     * Example: <code>}</code>
     */
    RBRACE,

    /**
     * `INT` represents a signed 32-bit integer number literal.
     */
    INT,

    /**
     * `FLOAT` represents a signed 32-bit float number literal.
     */
    FLOAT,

    /**
     * `HEXADECIMAL` represents a hexadecimal number literal.
     * Example: {@code 0xFFFFF}
     */
    HEXADECIMAL,

    /**
     * `BINARY` represents a binary number literal.
     * Example: {@code 0b01101}
     */
    BINARY,

    /**
     * `BOOL` represents a boolean literal.
     * Example: {@code true}
     */
    BOOL,

    /**
     * `SEMICOLON` represents a semicolon character.
     * Example: {@code ;}
     */
    SEMICOLON,

    /**
     * `EXPRESSION` represents any reserved keywords.
     * Example: {@code fn}
     */
    EXPRESSION,

    /**
     * `COLON` represents a colon character.
     * Example: {@code :}
     */
    COLON,

    /**
     * `COMMA` represents a comma character.
     * Example: {@code ,}
     */
    COMMA,

    /**
     * `OPEN` represents an opening parenthesis.
     * Example: {@code (}
     */
    LPAREN,

    /**
     * `CLOSE` represents a closing parenthesis.
     * Example: {@code )}
     */
    RPAREN,

    /**
     * `IDENTIFIER` represents a variable name.
     * Example: {@code abc}
     */
    IDENTIFIER,

    /**
     * `OPERATOR` represents a logical or mathematical operator.
     * Example: {@code +}, {@code !}, {@code &&}
     */
    OPERATOR,

    /**
     * `TYPE` represents a data type keyword.
     * Example: {@code int}
     */
    TYPE,

    /**
     * `START` represents the opening square bracket.
     * Example: {@code [}
     */
    LBRACKET,

    /**
     * `STOP` represents the closing square bracket.
     * Example: {@code ]}
     */
    RBRACKET,

    /**
     * `ANNOTATION` represents an annotation identifier.
     * Example: {@code @Link}
     */
    ANNOTATION,

    /**
     * `LINE_NUMBER` represents a line number identifier.
     * Example: {@code L11}
     */
    LINE_NUMBER,

    /**
     * `NIL` represents a nullptr constant keyword.
     * Example: {@code nil}
     */
    NIL,

    /**
     * `INFO` represents a file information token.
     * Example: {@code file information}
     */
    INFO,

    /**
     * `EOF` represents the end of the content.
     * Example: {@code content finished}
     */
    EOF,

    /**
     * `UNEXPECTED` represents a syntax error.
     * Example: {@code syntax error}
     */
    UNEXPECTED,

    /**
     * `NEW_LINE` represents a temporary new line token.
     * Example: {@code temp new line}
     */
    NEW_LINE,

    /**
     * `NONE` represents a non-existent token.
     * Example: {@code no such token}
     */
    NONE
}
