package org.housingstudio.hsl.compiler.token;

import lombok.RequiredArgsConstructor;
import org.housingstudio.hsl.compiler.debug.Format;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.regex.Pattern;

/**
 * Represents a class that parses the content of a source file into a list of tokens.
 *
 * @author AdvancedAntiSkid
 */
@RequiredArgsConstructor
public class Tokenizer {
    private static final Pattern DURATION_PATTERN = Pattern.compile(
        "^(?:(\\d+(?:_\\d+)*h))?" +                // hours
            "(?:(\\d+(?:_\\d+)*m))?" +                 // minutes
            "(?:(\\d+(?:_\\d+)*s))?" +                 // seconds
            "(?:(\\d+(?:_\\d+)*ms))?" +                // milliseconds
            "(?:(\\d+(?:_\\d+)*(?:us|µs)))?" +         // microseconds
            "(?:(\\d+(?:_\\d+)*ns))?$"                 // nanoseconds
    );

    /**
     * The maximum length of a displayable line of code in a syntax error.
     */
    public static final int MAX_ERROR_LINE_LENGTH = 30;

    /**
     * The source file that is being parsed.
     */
    private final @NotNull File file;

    /**
     * The content of the source file that is being parsed.
     */
    private final @NotNull String data;

    /**
     * The current cursor position in the source file.
     */
    private int cursor;

    /**
     * The index of the current character in the line that is being parsed.
     */
    private int lineIndex;

    /**
     * The current line number in the source file.
     */
    private int lineNumber = 1;

    /**
     * The index of the first character in the line of the token being parsed.
     */
    private int tokenLineIndex;

    /**
     * The line number of the token being parsed.
     */
    private int tokenLineNumber = 1;

    /**
     * The index of the first character in the token being parsed.
     */
    private int tokenBeginIndex;

    /**
     * Parse the next token from the source file.
     *
     * @return the next token in the source file
     */
    public @NotNull Token next() {
        // ignore all whitespaces from the content
        while (isWhitespace(peek())) {
            // even though, the compiler ignores whitespaces, we still need to keep track of the line index
            // and emit new line tokens for the auto semicolon insertion system
            if (get() != '\n')
                continue;

            // reset the line index
            lineIndex = 0;
            lineNumber++;
            // make a new line token to be replaced later to semicolons
            return makeToken(TokenType.NEW_LINE);
        }

        // handle one line comments
        if (peek() == '/' && at(cursor + 1) == '/') {
            while (peek() != '\n') {
                get();
            }
            lineIndex++;
        }

        // handle multiline comments
        else if (peek() == '/' && at(cursor + 1) == '*') {
            // skip the comment prefix
            skip(2);

            while (true) {
                if (peek() == '*' && at(cursor + 1) == '/') {
                    // skip the comment suffix
                    skip(2);
                    break;
                }
                get();
            }
        }

        // epic workaround for ignoring comments
        // ignore again all whitespaces from the content
        while (isWhitespace(peek())) {
            // handle new line
            if (get() == '\n') {
                // reset the line index
                lineIndex = 0;
                lineNumber++;
                // make a new line token to be replaced later to semicolons
                return makeToken(TokenType.NEW_LINE);
            }
        }

        // handle end of file
        if (peek() == '\0')
            return makeToken(TokenType.EOF);

        // update the token metadata
        tokenBeginIndex = cursor;
        tokenLineNumber = lineNumber;
        tokenLineIndex = lineIndex;

        // handle identifier-like tokens
        if (isIdentifierStart(peek()))
            return nextIdentifier();
        else if (isAnnotationStart(peek()))
            return nextAnnotation();

        // handle mathematical and logical operators
        else if (isOperator(peek()))
            return nextOperator();

        // handle separator tokens such as brackets, braces, and commas
        else if (isSeparator(peek()))
            return nextSeparator();

        // handle literal tokens
        else if (isNumberStart(peek()))
            return nextNumber();
        else if (isStringStart(peek()))
            return nextString();

        syntaxError(Errno.UNEXPECTED_CHARACTER, "unexpected character: `" + peek() + "`");
        return makeToken(TokenType.UNEXPECTED);
    }

    /**
     * Parse the next identifier-like token from the source file.
     *
     * @return the next identifier-like token in the source file
     */
    private @NotNull Token nextIdentifier() {
        // get the full identifier
        int begin = cursor;

        // move the cursor until it reaches the end of the identifier
        while (isIdentifierPart(peek()))
            get();

        // resolve the full content of the identifier
        String token = range(begin, cursor);

        // adjust the token type for reserved keywords (which are technically considered identifiers)
        TokenType type = TokenType.IDENTIFIER;
        if (isExpression(token))
            type = TokenType.EXPRESSION;
        else if (isType(token))
            type = TokenType.TYPE;
        else if (isBoolean(token))
            type = TokenType.BOOL;
        else if (isInfo(token))
            type = TokenType.INFO;
        else if (isNull(token))
            type = TokenType.NIL;

        return makeToken(type, token);
    }

    /**
     * Parse the next annotation token from the source file.
     *
     * @return the next annotation token in the source file
     */
    public @NotNull Token nextAnnotation() {
        // skip the '@' symbol
        // for example: `@Link("library.dll")`
        //           ^ this symbol is skipped
        skip(1);

        // parse the name of the annotation name
        Token token = nextIdentifier();
        if (!token.is(TokenType.IDENTIFIER))
            return token;

        return makeToken(TokenType.ANNOTATION, token.value());
    }

    /**
     * Parse the next operator token from the source file.
     *
     * @return the next operator token in the source file
     */
    private @NotNull Token nextOperator() {
        // operators are parsed separately, char-by-char
        // let the AST handle grouping them together
        // for example: `!=` will be parsed as `!` and `=`
        return makeToken(TokenType.OPERATOR, String.valueOf(get()));
    }

    /**
     * Parse the next separator token from the source file.
     *
     * @return the next separator token in the source file
     */
    private @NotNull Token nextSeparator() {
        char c = get();
        // resolve the token types for the separator char
        TokenType type;
        switch (c) {
            case ';':
                type = TokenType.SEMICOLON;
                break;
            case ':':
                type = TokenType.COLON;
                break;
            case ',':
                type = TokenType.COMMA;
                break;
            case '{':
                type = TokenType.LBRACE;
                break;
            case '}':
                type = TokenType.RBRACE;
                break;
            case '(':
                type = TokenType.LPAREN;
                break;
            case ')':
                type = TokenType.RPAREN;
                break;
            case '[':
                type = TokenType.LBRACKET;
                break;
            case ']':
                type = TokenType.RBRACKET;
                break;
            default:
                type = TokenType.UNEXPECTED; // should never reach this, as the separator check is done beforehand
        };
        return makeToken(type, String.valueOf(c));
    }

    /**
     * Parse the next number token from the source file.
     *
     * @return the next number token in the source file
     */
    private @NotNull Token nextNumber() {
        // get the beginning of the number content
        int begin = cursor;

        // handle hexadecimal number format
        // check if the number starts with '0x'
        // for example: `0x1A`, `0x1AF`, `0x1AFD`
        //               ^^ this prefix is checked here
        if (peek() == '0' && at(cursor + 1) == 'x') {
            // skip the '0x' prefix
            skip(2);

            // move the cursor until it reaches the end of the number
            while (isHexValue(peek()))
                get();

            // make the hexadecimal number token
            String value = range(begin, cursor);
            return makeToken(TokenType.HEXADECIMAL, value);
        }

        // handle binary number format
        // check if the number starts with '0b'
        // for example: `0b1010`, `0b1010F`, `0b1010D`
        //               ^^ this prefix is checked here
        else if (peek() == '0' && at(cursor + 1) == 'b') {
            // skip the '0b' prefix
            skip(2);

            // move the cursor until it reaches the end of the number
            while (isBinary(peek()))
                get();

            // make the hexadecimal number token
            String value = range(begin, cursor);
            return makeToken(TokenType.BINARY, value);
        }

        // will adjust this state later, when processing the content of the number
        // initially true, because we are unaware, if the number has any `.` symbols
        // if it does not, then it is an integer-like number
        boolean integer = true;

        // will adjust this later, if the content contains duration specifiers such as `h` or `m`
        boolean duration = false;

        // handle regular number format
        // loop until we reach the end of the number content
        // for example: `123.4D`
        //               ^^^^^^ will loop 6 times
        while (isNumberContent(peek())) {
            // handle floating point number
            // for example: `1.5`, `1.5F`, `1.5D`
            //                ^ this character is checked here
            if (peek() == '.') {
                // check if the floating-point number contains multiple dot symbols
                if (!integer) {
                    tokenLineIndex += cursor - begin;
                    syntaxError(
                        Errno.MULTIPLE_DECIMAL_POINTS,
                        "floating point number cannot have multiple dot symbols"
                    );
                    return makeToken(TokenType.UNEXPECTED);
                }
                integer = false;

                // check if duration value contains decimal points
                if (duration) {
                    syntaxError(
                        Errno.CANNOT_HAVE_DECIMAL_POINT,
                        "duration number cannot have decimal points"
                    );
                    return makeToken(TokenType.UNEXPECTED);
                }
            }

            else if (isDuration(peek())) {
                duration = true;
            }

            // move to the next number part
            skip(1);
        }

        // TODO validate that the number does not end with '.'
        // TODO validate that multiple underscores do not follow each other
        // TODO handle numbers starting with '.'

        // get the value of the number
        String value = range(begin, cursor);

        if (duration) {
            if (value.isEmpty() || !DURATION_PATTERN.matcher(value).matches()) {
                syntaxError(Errno.INVALID_DURATION_VALUE, "invalid duration value");
                return makeToken(TokenType.UNEXPECTED);
            }

            return makeToken(TokenType.DURATION, value);
        }

        return makeToken(integer ? TokenType.INT : TokenType.FLOAT, value);
    }

    /**
     * Parse the next string token from the source file.
     *
     * @return the next string token in the source file
     */
    private @NotNull Token nextString() {
        return nextStringOrChar();
    }

    /**
     * Parse the next string token from the source file.
     *
     * @return the next string token in the source file
     */
    private @NotNull Token nextStringOrChar() {
        // will append the string content later
        StringBuilder content = new StringBuilder();

        // skip the quotation mark
        skip(1);

        // the indication, whether then next character should be treated as an escape character,
        // when the current character is a backslash
        boolean escapeNext = false;

        // loop until the string literal is terminated or the end of file has been reached
        while (has(cursor)) {
            // handle escaped characters, when the previous character was a backslash
            if (escapeNext) {
                switch (peek()) {
                    case 'n':
                        content.append('\n');
                        break;
                    case 'r':
                        content.append('\r');
                        break;
                    case 't':
                        content.append('\t');
                        break;
                    case '\\':
                        content.append('\\');
                        break;
                    // TODO handle \\u character code
                    default:
                        // validate the escape sequence
                        if (peek() == '"')
                            content.append(peek());
                        else {
                            tokenLineIndex += content.length() + 1;
                            syntaxError(
                                Errno.INVALID_ESCAPE_SEQUENCE, "invalid escape sequence: `\\" + peek() +
                                "`"
                            );
                        }
                }
                escapeNext = false;
            }

            // handle escaping the next character
            // for example: `"Hello, \"World\""`
            //                       ^ backslash detected, escape the next character
            else if (peek() == '\\')
                escapeNext = true;

            // handle the ending of the string literal
            // for example: `"Hello, World"` or `'a'`
            //               ^            ^      ^ ^ opening and closing characters must be identical
            else if (peek() == '"') {
                // skip the end of the string
                skip(1);
                return makeToken(TokenType.STRING, content.toString());
            }

            // handle string literal content
            else
                content.append(peek());

            // move to the next string character
            skip(1);
        }

        // TODO: note that this is NOT a multi-line string, so don't treat it like that
        //  stop string parsing when encountering a new line and emit an error
        //  multiline comments should be similar to Java's `"""` syntax

        // handle unexpected end of a string literal
        syntaxError(
            Errno.MISSING_STRING_TERMINATOR,
            "missing trailing `\"` symbol to terminate the string literal"
        );
        return makeToken(TokenType.UNEXPECTED);
    }

    /**
     * Retrieve the string value from the source content within the specified range.
     *
     * @param begin the content range start index
     * @param end the content range finish index
     */
    private @NotNull String range(int begin, int end) {
        return data.substring(begin, end);
    }

    /**
     * Retrieve the character at the current cursor position without moving the cursor.
     *
     * @return the character at the current cursor position
     */
    private char peek() {
        return at(cursor);
    }

    /**
     * Retrieve the character at the current cursor position and move the cursor to the next character.
     *
     * @return the character at the current cursor position
     */
    private char get() {
        lineIndex++;
        return at(cursor++);
    }

    /**
     * Move the cursor position with the specified amount.
     *
     * @param amount the cursor move amount
     */
    private void skip(int amount) {
        lineIndex += amount;
        cursor += amount;
    }

    /**
     * Retrieve the character at the specified index in the source content.
     *
     * @param index the index of the character to retrieve
     * @return the character at the specified index
     */
    private char at(int index) {
        return has(index) ? data.charAt(index) : '\0';
    }

    /**
     * Indicate, whether the specified index is within the bounds of the source content size.
     *
     * @param index the index to check
     * @return {@code true} if the index is within the bounds of the source content size
     */
    private boolean has(int index) {
        return index >= 0 && index < data.length();
    }

    /**
     * Retrieve the uppercase format of the specified character.
     *
     * @param c the character to be transformer
     * @return the uppercase representation of the character
     */
    private char upper(char c) {
        return Character.toUpperCase(c);
    }

    /**
     * Retrieve the lowercase format of the specified character.
     *
     * @param c the character to be transformer
     * @return the lowercase representation of the character
     */
    private char lower(char c) {
        return Character.toLowerCase(c);
    }

    /**
     * Indicate, whether the specified character is a whitespace.
     *
     * @param c the character to test
     * @return {@code true} if the character is a whitespace
     */
    private boolean isWhitespace(char c) {
        switch (c) {
            case ' ':
            case '\t':
            case '\r':
            case '\n':
                return true;
            default:
                return false;
        }
    }

    /**
     * Indicate, whether the specified character is the beginning of an identifier.
     *
     * @param c the character to test
     * @return {@code true} if the character is an identifier beginning
     */
    private boolean isIdentifierStart(char c) {
        return Character.isJavaIdentifierStart(c);
    }

    /**
     * Indicate, whether the specified character is the part of an identifier.
     *
     * @param c the character to test
     * @return {@code true} if the character is an identifier part
     */
    private boolean isIdentifierPart(char c) {
        return Character.isJavaIdentifierPart(c);
    }

    /**
     * Indicate, whether the specified character is numeric.
     *
     * @param c the character to test
     * @return {@code true} if the character is numeric
     */
    private boolean isNumberStart(char c) {
        return Character.isDigit(c);
    }

    /**
     * Indicate, whether the specified character is the beginning of a string.
     *
     * @param c the character to test
     * @return {@code true} if the character is a string beginning
     */
    private boolean isStringStart(char c) {
        return c == '"';
    }

    /**
     * Indicate, whether the specified character is the beginning of an annotation.
     *
     * @param c the character to test
     * @return {@code true} if the character is an annotation beginning
     */
    private boolean isAnnotationStart(char c) {
        return c == '@';
    }

    /**
     * Indicate, whether the specified character is a hexadecimal number part.
     *
     * @param c the character to test
     * @return {@code true} if the character is a hexadecimal char
     */
    private boolean isHexValue(char c) {
        c = lower(c);
        switch (c) {
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
                return true;
            default:
                return isNumberStart(c); // fall back to digit check
        }
    }

    /**
     * Indicate, whether the specified character is a binary number part.
     *
     * @param c the character to test
     * @return {@code true} if the character is a binary char
     */
    private boolean isBinary(char c) {
        switch (c) {
            case '0':
            case '1':
                return true;
            default:
                return false;
        }
    }

    /**
     * Indicate, whether the specified character is a time duration part.
     *
     * @param c the character to test
     * @return {@code ture} if the character is a duration char
     */
    private boolean isDuration(char c) {
        switch (c) {
            case 'h':
            case 'm':
            case 's':
            case 'µ':
            case 'n':
                return true;
            default:
                return false;
        }
    }

    /**
     * Indicate, whether the specified character is a content of a number.
     *
     * @param c the character to test
     * @return {@code true} if the character is a number content
     */
    private boolean isNumberContent(char c) {
        switch (c) {
            case '.':
            case '_':
                return true;
            default:
                return isHexValue(c) || isDuration(c);
        }
    }

    /**
     * Indicate, whether the specified character is an operator.
     *
     * @param c the character to test
     * @return {@code true} if the character is an operator
     */
    private boolean isOperator(char c) {
        switch (c) {
            case '.':
            case '=':
            case '+':
            case '-':
            case '*':
            case '/':
            case '<':
            case '>':
            case '?':
            case '!':
            case '^':
            case '&':
            case '~':
            case '$':
            case '|':
            case '%':
                return true;
            default:
                return false;
        }
    }

    /**
     * Indicate, whether the specified character is a separator.
     *
     * @param c the target character to test
     * @return {@code true} if the character is a separator
     */
    private boolean isSeparator(char c) {
        switch (c) {
            case ';':
            case ':':
            case ',':
            case '{':
            case '}':
            case '(':
            case ')':
            case '[':
            case ']':
                return true;
            default:
                return false;
        }
    }

    /**
     * Indicate, whether the specified token is an expression token.
     *
     * @param token the token to test
     * @return {@code true} if the token is an expression
     */
    private boolean isExpression(@NotNull String token) {
        switch (token) {
            case "fn":
            case "const":
            case "command":
            case "event":
            case "scoreboard":
            case "region":
            case "stat":
            case "if":
            case "else":
            case "return":
                return true;
            default:
                return false;
        }
    }

    /**
     * Indicate, whether the specified token is a type token.
     *
     * @param token the token to test
     * @return {@code true} if the token is a type
     */
    private boolean isType(@NotNull String token) {
        switch (token) {
            case "int":
            case "float":
            case "any":
            case "string":
                return true;
            default:
                return false;
        }
    }

    /**
     * Indicate, whether the specified token is a boolean token.
     *
     * @param token the token to test
     * @return {@code true} if the token is a boolean
     */
    private boolean isBoolean(@NotNull String token) {
        switch (token) {
            case "true":
            case "false":
                return true;
            default:
                return false;
        }
    }

    /**
     * Indicate, whether the specified token is an information token.
     *
     * @param token target token to test
     * @return {@code true} if the token is an information
     */
    private boolean isInfo(@NotNull String token) {
        return false;
    }

    /**
     * Indicate, whether the specified token is a null token.
     *
     * @param token the token to test
     * @return {@code true} if the token is a null
     */
    private boolean isNull(String token) {
        return token.equals("nil");
    }

    /**
     * Create a new token of the specified type and value.
     * <p>
     * This method will implicitly resolve the metadata of the requested token.
     *
     * @param type the type of the token
     * @param value the value of the token
     * @return the new token
     */
    private @NotNull Token makeToken(@NotNull TokenType type, @NotNull String value) {
        Meta meta = new Meta(tokenBeginIndex, cursor, tokenLineIndex, tokenLineNumber);
        return new Token(type, value, meta);
    }

    /**
     * Create a new token of the specified type.
     * <p>
     * This method will implicitly resolve the metadata of the requested token.
     *
     * @param type the type of the token
     * @return the new token
     */
    private @NotNull Token makeToken(@NotNull TokenType type) {
        return makeToken(type, "");
    }

    private @NotNull String repeat(@NotNull String value, int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++)
            builder.append(value);
        return builder.toString();
    }

    /**
     * Display a syntax error in the standard error output with the with some debug information.
     *
     * @param error the token error
     * @param message the short error message
     */
    private void syntaxError(@NotNull Errno error, @NotNull String message) {
        System.err.println(Format.RED + "error[E" + error.code() + "]" + Format.WHITE + ": " + message);
        System.err.println(
            Format.CYAN + " --> " + Format.LIGHT_GRAY + file.getName() + ":" + tokenLineNumber + ":" +
            tokenLineIndex
        );

        int lineSize = String.valueOf(tokenLineNumber).length();

        // display the line number
        System.err.print(Format.CYAN + repeat(" ", lineSize + 1));
        System.err.println(" | ");

        System.err.print(" " + tokenLineNumber + " | ");

        // get the line of the error
        String line = Format.WHITE + data.split("\n")[tokenLineNumber - 1];

        // get the start and end index of the line
        int start = Math.max(0, tokenLineIndex - MAX_ERROR_LINE_LENGTH);
        int end = Math.min(line.length(), tokenLineIndex + MAX_ERROR_LINE_LENGTH);

        // display the line of the error
        System.err.println(Format.LIGHT_GRAY + line.substring(start, end));

        // display the error pointer
        System.err.print(Format.CYAN + repeat(" ", lineSize + 1));
        System.err.println(" | " + repeat(" ", lineSize + (tokenLineIndex - start) - 1) + Format.RED + "^");

        // exit the program with the error code
        //System.exit(error.code());
    }
}
