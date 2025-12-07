package org.housingstudio.hsl.compiler.ast.impl.operator;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.value.ConstantLiteral;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.PREFIX_UNARY_OPERATOR)
public class PrefixOperator extends Value {
    private final @NotNull Operator operator;

    @Children
    private final @NotNull Value operand;

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        return operand.getValueType();
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
        return load().asConstantValue();
    }

    private @NotNull Value negate(@NotNull Value operand) {
        if (operand instanceof ConstantLiteral)
            return buildStaticNegation(operand);

        return buildDynamicNegation(operand);
    }

    private @NotNull Value buildDynamicNegation(@NotNull Value operand) {
        return new BinaryOperator(
            operand,
            Operator.MULTIPLY, Token.of(TokenType.OPERATOR, "*"),
            ConstantLiteral.ofInt(-1)
        );
    }

    private @NotNull Value buildStaticNegation(@NotNull Value operand) {
        if (!operand.getValueType().numeric()) {
            context.errorPrinter().print(
                Notification.error(Errno.UNEXPECTED_OPERAND, "unexpected prefix operand")
                    .error("non-numeric values are not supported", context.peek()) // TODO resolve correct token
            );
        }

        Token token = ((ConstantLiteral) operand).token();
        Token negated = new Token(token.type(), "-" + token.value(), token.meta());
        return new ConstantLiteral(negated);
    }

    @Override
    public @NotNull Value load() {
        Value value = operand.load();
        switch (operator) {
            case NEGATE_OR_SUBTRACT:
                value = negate(value);
                break;
            default:
                context.errorPrinter().print(
                    Notification.error(Errno.NOT_IMPLEMENTED_FEATURE, "not implemented prefix operator")
                        .error("this prefix operator is not implemented yet", context.peek()) // TODO resolve correct token
                );
                throw new IllegalStateException("Prefix operator " + operator.value() + " is not implemented yet");
        }
        return value;
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return operator.value() + operand.print();
    }
}
