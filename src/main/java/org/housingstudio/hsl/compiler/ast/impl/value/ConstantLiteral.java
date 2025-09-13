package org.housingstudio.hsl.compiler.ast.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.type.BaseType;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a value node in the Abstract Syntax Tree, that holds a constant value.
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.LITERAL)
@ToString
public class ConstantLiteral extends Value implements Printable {
    private static final Pattern DURATION_SEGMENT = Pattern.compile("(\\d+(?:_\\d+)*)(ns|us|µs|ms|s|m|h)");

    /**
     * The held constant value of the literal.
     */
    private final @NotNull Token token;

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull BaseType getValueType() {
        switch (token.type()) {
            case STRING:
                return BaseType.STRING;
            case INT:
            case DURATION:
            case HEXADECIMAL:
            case BINARY:
                return BaseType.INT;
            case FLOAT:
                return BaseType.FLOAT;
            case BOOL:
                return BaseType.BOOL;
            default:
                throw new IllegalStateException("Cannot resolve type of token: " + token);
        }
    }

    /**
     * Get the constant string representation of the value.
     * <p>
     * Housing variables are handled as string by default, this format is the input for housing variables.
     *
     * @return the final string value
     */
    @Override
    public @NotNull String asConstantValue() {
        String value = token.value();
        if (token.type() == TokenType.DURATION)
            value = Long.toString(durationToTicks(value));
        else if (token.type() == TokenType.HEXADECIMAL)
            value = Long.toString(hexStringToInt(value));
        else if (token.type() == TokenType.BINARY)
            value = Long.toString(binaryStringToInt(value));
        return value;
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        String str = token.value();
        if (token.type() == TokenType.STRING)
            str = "\"" + str + "\"";
        return str;
    }

    public static long durationToTicks(String input) {
        if (input == null || input.isEmpty())
            throw new IllegalArgumentException("Duration string cannot be null or empty");

        Matcher matcher = DURATION_SEGMENT.matcher(input);
        long totalNanos = 0;
        int matchedLength = 0;

        while (matcher.find()) {
            matchedLength += matcher.group(0).length();
            long value = Long.parseLong(matcher.group(1).replace("_", ""));
            String unit = matcher.group(2);

            switch (unit) {
                case "h":  totalNanos += value * 3_600_000_000_000L; break; // 1h = 3600s
                case "m":  totalNanos += value * 60_000_000_000L; break;    // 1m = 60s
                case "s":  totalNanos += value * 1_000_000_000L; break;     // 1s
                case "ms": totalNanos += value * 1_000_000L; break;
                case "us":
                case "µs": totalNanos += value * 1_000L; break;
                case "ns": totalNanos += value; break;
            }
        }

        // Ensure entire string matched
        if (matchedLength != input.length())
            throw new IllegalArgumentException("Invalid duration format: " + input);

        // Convert nanoseconds → seconds → ticks
        return totalNanos / 50_000_000L; // 1 tick = 50ms = 50,000,000 ns
    }

    public static int hexStringToInt(String hex) {
        if (hex == null)
            throw new IllegalArgumentException("Input cannot be null");

        hex = hex.trim();
        if (hex.startsWith("0x") || hex.startsWith("0X"))
            hex = hex.substring(2); // remove the "0x" prefix

        return (int) Long.parseLong(hex, 16); // use long to avoid overflow, cast back to int
    }

    public static int binaryStringToInt(String bin) {
        if (bin == null)
            throw new IllegalArgumentException("Input cannot be null");

        bin = bin.trim();
        if (bin.startsWith("0b") || bin.startsWith("0B"))
            bin = bin.substring(2); // remove "0b" prefix

        return (int) Long.parseLong(bin, 2); // parse as base 2
    }

    public static @NotNull ConstantLiteral ofInt(int value) {
        return new ConstantLiteral(Token.of(TokenType.INT, String.valueOf(value)));
    }

    public static @NotNull ConstantLiteral ofBool(boolean value) {
        return new ConstantLiteral(Token.of(TokenType.BOOL, String.valueOf(value)));
    }

    public static @NotNull ConstantLiteral ofString(String value) {
        return new ConstantLiteral(Token.of(TokenType.STRING, String.valueOf(value)));
    }

    public static @NotNull ConstantLiteral ofFloat(float value) {
        return new ConstantLiteral(Token.of(TokenType.FLOAT, String.valueOf(value)));
    }
}
