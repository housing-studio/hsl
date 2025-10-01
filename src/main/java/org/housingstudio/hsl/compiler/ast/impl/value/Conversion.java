package org.housingstudio.hsl.compiler.ast.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.type.BaseType;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;

@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.GROUP)
@RequiredArgsConstructor
public class Conversion extends Value {
    private final @NotNull Type explicitType;
    private final @NotNull Value value;

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        return explicitType;
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
        verifyTargets();

        if (explicitType.matches(Types.STRING)) {
            return value.asConstantValue();
        }

        else if (explicitType.matches(Types.FLOAT)) {
            if (value.getValueType().matches(Types.INT))
                return String.valueOf((float) Integer.parseInt(value.asConstantValue()));
        }

        else if (explicitType.matches(Types.INT)) {
            if (value.getValueType().matches(Types.FLOAT))
                return String.valueOf((int) Float.parseFloat(value.asConstantValue()));
        }

        context.errorPrinter().print(
            Notification.error(Errno.ILLEGAL_TYPE_CONVERSION, "illegal type conversion", this)
                .error(
                    "cannot convert type " + value.getValueType().print() + " to " + explicitType.print(),
                    explicitType.tokens()
                )
        );
        throw new UnsupportedOperationException(
            "Cannot covert type " + value.getValueType().print() + " to " + explicitType.print()
        );
    }

    @Override
    public @NotNull Value load() {
        verifyTargets();

        ConstantLiteral oldLiteral = (ConstantLiteral) value.load();
        if (explicitType.matches(Types.STRING))
            return value;

        else if (explicitType.matches(Types.FLOAT)) {
            if (value.getValueType().matches(Types.INT)) {
                String newValue = String.valueOf((float) Integer.parseInt(value.asConstantValue()));
                return new ConstantLiteral(new Token(TokenType.FLOAT, newValue, oldLiteral.token().meta()));
            }
        }

        else if (explicitType.matches(Types.INT)) {
            if (value.getValueType().matches(Types.FLOAT)) {
                String newValue = String.valueOf((int) Float.parseFloat(value.asConstantValue()));
                return new ConstantLiteral(new Token(TokenType.INT, newValue, oldLiteral.token().meta()));
            }
        }

        context.errorPrinter().print(
            Notification.error(Errno.ILLEGAL_TYPE_CONVERSION, "illegal type conversion", this)
                .error(
                    "cannot convert type " + value.getValueType().print() + " to " + explicitType.print(),
                    explicitType.tokens()
                )
        );
        throw new UnsupportedOperationException(
            "Cannot covert type " + value.getValueType().print() + " to " + explicitType.print()
        );
    }


    private void verifyTargets() {
        if (!value.isConstant()) {
            context.errorPrinter().print(
                Notification.error(Errno.ILLEGAL_TYPE_CONVERSION, "illegal type conversion", this)
                    .error(
                        "cannot convert value, because its value may not be known at compile-time",
                        explicitType.tokens()
                    )
                    .note("consider declaring a constant for the value", "const FOO = 1234")
            );
        }

        if (!explicitType.base().primitive()) {
            context.errorPrinter().print(
                Notification.error(Errno.ILLEGAL_TYPE_CONVERSION, "illegal type conversion", this)
                    .error(
                        "cannot convert to non-primitive type",
                        explicitType.tokens()
                    )
                    .note("use a primitive type to convert to", "string(1234)")
            );
        }

        if (explicitType.base() == BaseType.VOID || explicitType.base() == BaseType.NIL) {
            context.errorPrinter().print(
                Notification.error(Errno.ILLEGAL_TYPE_CONVERSION, "illegal type conversion", this)
                    .error(
                        "this special type cannot be converted to",
                        explicitType.tokens()
                    )
                    .note("use another primitive type to convert to", "string(1234)")
            );
        }

        if (!value.getValueType().base().primitive()) {
            context.errorPrinter().print(
                Notification.error(Errno.ILLEGAL_TYPE_CONVERSION, "illegal type conversion", this)
                    .error(
                        "cannot convert from non-primitive type",
                        value.getValueType().tokens()
                    )
                    .note("use a primitive type to convert from", "string(1234)")
            );
        }

        if (value.getValueType().base() == BaseType.VOID || value.getValueType().base() == BaseType.NIL) {
            context.errorPrinter().print(
                Notification.error(Errno.ILLEGAL_TYPE_CONVERSION, "illegal type conversion", this)
                    .error(
                        "this special type cannot be converted from",
                        explicitType.tokens()
                    )
                    .note("use another primitive type to convert from", "string(1234)")
            );
        }
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return explicitType.print() + "(" + value.print() + ")";
    }
}
