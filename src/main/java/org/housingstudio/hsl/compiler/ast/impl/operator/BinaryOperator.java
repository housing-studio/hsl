package org.housingstudio.hsl.compiler.ast.impl.operator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.ast.impl.type.BaseType;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a binary operation between two values in the Abstract Syntax Tree.
 * <p>
 * The order of the operations will be determined by their precedence defined in
 * {@link Operator#precedence()} and the operation tree be transformed by the parser.
 * <p>
 * FOr example, the code {@code 1 + 2 * 3} will resolve to {@code (1 + 2) * 3}, according to the tree parsing.
 * However, the transformer will convert this operation to {@code 1 + (2 * 3)}.
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.BINARY_OPERATOR)
public class BinaryOperator extends Value {
    /**
     * The left-hand side value of the operation.
     */
    @Setter
    @Children
    private @NotNull Value lhs;

    /**
     * The operator that will be applied to the left-hand side and right-hand side values.
     */
    private final @NotNull Operator operator;

    private final @NotNull Token operatorToken;

    /**
     * The right-hand side value of the operation.
     */
    @Setter
    @Children
    private @NotNull Value rhs;

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        return lhs.getValueType();
    }

    @Override
    public boolean isConstant() {
        return lhs.isConstant() && rhs.isConstant();
    }

    @Override
    public void init() {
        if (!lhs.getValueType().matches(rhs.getValueType())) {
            context.errorPrinter().print(
                Notification.error(Errno.OPERATOR_TYPE_MISMATCH, "operator type mismatch")
                    .error(
                        String.format(
                            "operator type mismatch (lhs: %s, rhs: %s)", lhs.getValueType().print(),
                            rhs.getValueType().print()
                        ),
                        operatorToken
                    )
                    .note("make sure LHS and RHS are the same type")
            );
            throw new UnsupportedOperationException(
                String.format(
                    "Operator type mismatch (lhs: %s, rhs: %s)",
                    lhs.getValueType().print(), rhs.getValueType().print()
                )
            );
        }

        if (!supported()) {
            context.errorPrinter().print(
                Notification.error(Errno.UNEXPECTED_OPERAND, "unexpected operand")
                    .error("operator not supported for type: " + lhs.getValueType().print(), operatorToken)
            );
            throw new UnsupportedOperationException(
                "Operator not supported for type: " + lhs.getValueType().print()
            );
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
        switch (operator) {
            case ADD:
                if (lhs.getValueType().matches(Types.INT)) {
                    int lhs = Integer.parseInt(this.lhs.asConstantValue());
                    int rhs = Integer.parseInt(this.rhs.asConstantValue());
                    return String.valueOf(lhs + rhs);
                }

                else if (lhs.getValueType().matches(Types.FLOAT)) {
                    float lhs = Float.parseFloat(this.lhs.asConstantValue());
                    float rhs = Float.parseFloat(this.rhs.asConstantValue());
                    return String.valueOf(lhs + rhs);
                }

                else if (lhs.getValueType().matches(Types.STRING)) {
                    String lhs = this.lhs.asConstantValue();
                    String rhs = this.rhs.asConstantValue();
                    return lhs + rhs;
                }

            case NEGATE_OR_SUBTRACT:
                if (lhs.getValueType().matches(Types.INT)) {
                    int lhs = Integer.parseInt(this.lhs.asConstantValue());
                    int rhs = Integer.parseInt(this.rhs.asConstantValue());
                    return String.valueOf(lhs - rhs);
                }

                else if (lhs.getValueType().matches(Types.FLOAT)) {
                    float lhs = Float.parseFloat(this.lhs.asConstantValue());
                    float rhs = Float.parseFloat(this.rhs.asConstantValue());
                    return String.valueOf(lhs - rhs);
                }

            case MULTIPLY:
                if (lhs.getValueType().matches(Types.INT)) {
                    int lhs = Integer.parseInt(this.lhs.asConstantValue());
                    int rhs = Integer.parseInt(this.rhs.asConstantValue());
                    return String.valueOf(lhs * rhs);
                }

                else if (lhs.getValueType().matches(Types.FLOAT)) {
                    float lhs = Float.parseFloat(this.lhs.asConstantValue());
                    float rhs = Float.parseFloat(this.rhs.asConstantValue());
                    return String.valueOf(lhs * rhs);
                }

            case DIVIDE:
                if (lhs.getValueType().matches(Types.INT)) {
                    int lhs = Integer.parseInt(this.lhs.asConstantValue());
                    int rhs = Integer.parseInt(this.rhs.asConstantValue());
                    return String.valueOf(lhs / rhs);
                }

                else if (lhs.getValueType().matches(Types.FLOAT)) {
                    float lhs = Float.parseFloat(this.lhs.asConstantValue());
                    float rhs = Float.parseFloat(this.rhs.asConstantValue());
                    return String.valueOf(lhs / rhs);
                }

            case REMAINDER:
                if (lhs.getValueType().matches(Types.INT)) {
                    int lhs = Integer.parseInt(this.lhs.asConstantValue());
                    int rhs = Integer.parseInt(this.rhs.asConstantValue());
                    return String.valueOf(lhs % rhs);
                }

                else if (lhs.getValueType().matches(Types.FLOAT)) {
                    float lhs = Float.parseFloat(this.lhs.asConstantValue());
                    float rhs = Float.parseFloat(this.rhs.asConstantValue());
                    return String.valueOf(lhs % rhs);
                }
        }

        throw new IllegalStateException("Unsupported operator type: " + operator);
    }

    public boolean supported() {
        BaseType type = lhs.getValueType().base();

        // ANY type is considered supported for all operations (will be validated later)
        if (type == BaseType.ANY)
            return true;

        switch (operator) {
            case ADD:
                return type.isNumber() || type == BaseType.STRING;
            case NEGATE_OR_SUBTRACT:
            case DIVIDE:
            case MULTIPLY:
            case REMAINDER:
                return type.isNumber();
            case AND:
            case OR:
                return type == BaseType.BOOL;
            default:
                return false;
        }
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return lhs.print() + " " + operator.value() + " " + rhs.print();
    }

    public static boolean isBinaryOperator(@NotNull Token token) {
        if (!token.is(TokenType.OPERATOR))
            return false;

        switch (token.value()) {
            case "+":
            case "-":
            case "*":
            case "/":
            case "%":
                return true;
            default:
                return false;
        }
    }
}
