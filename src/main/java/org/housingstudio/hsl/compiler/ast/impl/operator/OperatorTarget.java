package org.housingstudio.hsl.compiler.ast.impl.operator;

/**
 * Represents an enumeration of operation types in the Abstract Syntax Tree.
 * <p>
 * <ul>
 *     <li>
 *         {@link #BINARY} - a binary operation between two values, for example {@code a + b}
 *     </li>
 *     <li>
 *         {@link #PREFIX_UNARY} - a unary operation that is applied to the left-hand side of the value,
 *         for example {@code -a}
 *     </li>
 *     <li>
 *         {@link #POSTFIX_UNARY} - a unary operation that is applied to the right-hand side of the value,
 *         for example {@code a++}
 *     </li>
 *     <li>
 *         {@link #TERNARY} - a ternary operation that is applied to the left-hand side of the value,
 *         for example {@code a ? b : c}
 *     </li>
 * </ul>
 */
public enum OperatorTarget {
    /**
     * `BINARY` represents a binary operation between two values, for example {@code a + b}.
     */
    BINARY,

    /**
     * `PREFIX_UNARY` represents a unary operation that is applied to the left-hand side of the value,
     * for example {@code -a}.
     */
    PREFIX_UNARY,

    /**
     * `POSTFIX_UNARY` represents a unary operation that is applied to the right-hand side of the value,
     * for example {@code a++}.
     */
    POSTFIX_UNARY,

    /**
     * `TERNARY` represents a ternary operation that is applied to the left-hand side of the value,
     * for example {@code a ? b : c}.
     */
    TERNARY
}
