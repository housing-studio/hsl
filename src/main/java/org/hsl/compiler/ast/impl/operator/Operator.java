package org.hsl.compiler.ast.impl.operator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Represents an enumeration of defined operators in the Abstract Syntax Tree.
 * <p>
 * The priority of the operator is defined by {@link #precedence()}, which will then
 * be used to transform the operation tree according to the precedence of each operator.
 * <p>
 * For example, the code {@code 1 + 2 * 3} will resolve to {@code (1 + 2) * 3}, according to the tree parsing.
 * However, the transformer will convert this operation to {@code 1 + (2 * 3)}, as {@code *} has a higher precedence
 * than {@code +}.
 */
@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public enum Operator {
    AND("&&", 0, 0),
    OR("||", 0, 0),

    NEGATE_OR_SUBTRACT("-", 0, 0),
    NOT("!", 0, 0),
    QUESTION("?", 0, 0),

    ADD("+", 1, 0),
    ADD_EQUAL("+=", 1, 0),
    INCREMENT("++", 1, 0),

    SUBTRACT_EQUAL("-=", 1, 0),
    DECREMENT("--", 1, 0),

    MULTIPLY("*", 2, 0),
    MULTIPLY_EQUAL("*=", 2, 0),

    DIVIDE("/", 2, 0),
    DIVIDE_EQUAL("/=", 2, 0),

    REMAINDER("%", 2, 0),
    REMAINDER_EQUAL("%=", 2, 0),

    POWER("^", 3, 1),
    POWER_EQUAL("^=", 3, 1),

    EQUAL("==", 4, 0),
    NOT_EQUAL("!=", 4, 0),
    GREATER_THAN(">", 4, 0),
    GREATER_OR_EQUAL(">=", 4, 0),
    LESS_THAN("<", 4, 0),
    LESS_OR_EQUAL("<=", 4, 0),

    DOT(".", 5, 0),

    SLICE(":", 6, 0),
    LAMBDA("::", 6, 0),

    ARROW("->", 7, 0),

    ASSIGN("=", 8, 0),

    UNKNOWN("<unk>", -1, -1);

    /**
     * The name of the operator.
     */
    private final @NotNull String value;

    /**
     * The priority of the operator in the operation tree. The higher this value is,
     * the sooner the operation will be executed.
     */
    private final int precedence;

    /**
     * The associativity of the operator. This value is used to determine the order of execution
     * when two operators have the same precedence.
     * <p>
     * The value {@code 0} will resolve in the order of left-to-right, and the value {@code 1} will be right-to-left.
     */
    private final int associativity;

    /**
     * Find the wrapper for the given operator value.
     * @param value raw operator value
     * @return operator wrapper
     */
    public static @NotNull Operator of(@NotNull String value) {
        return Arrays.stream(values())
            .filter(operator -> operator.value.equals(value))
            .findFirst()
            .orElse(UNKNOWN);
    }
}
