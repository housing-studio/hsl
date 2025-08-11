package org.hsl.compiler.ast.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.debug.Printable;
import org.hsl.compiler.token.Token;
import org.hsl.compiler.token.TokenType;
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
    public @NotNull Type getValueType() {
        return switch (token.type()) {
            case STRING -> Type.STRING;
            case INT, DURATION, HEXADECIMAL, BINARY -> Type.INT;
            case FLOAT -> Type.FLOAT;
            case BOOL -> Type.BOOL;
            default -> throw new IllegalStateException("Cannot resolve type of token: " + token);
        };
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

    public long durationToTicks(String input) {
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
}
