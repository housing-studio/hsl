package org.housingstudio.hsl.compiler.ast.impl.statement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.ast.impl.type.ArrayType;
import org.housingstudio.hsl.compiler.ast.impl.type.BaseType;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.ARRAY_LOAD)
public class ArrayLoad extends Value {
    private final @NotNull Token name;

    @Children
    private final @NotNull Value index;

    private Variable variable;

    @Override
    public void init() {
        resolveVariable();
    }

    private @NotNull Variable resolveVariable() {
        if (variable != null)
            return variable;

        variable = resolveName(name.value());
        if (variable == null) {
            context.errorPrinter().print(
                Notification.error(Errno.UNKNOWN_VARIABLE, "unknown variable: " + name.value(), this)
                    .error("cannot find variable in this scope", name)
                    .note("did you misspell the name, or forgot to declare the variable?")
            );
            throw new UnsupportedOperationException("Cannot find variable: " + name.value());
        }

        if (variable.type().base() != BaseType.ARRAY) {
            context.errorPrinter().print(
                Notification.error(Errno.UNEXPECTED_TYPE, "expected array type for: " + name.value(), this)
                    .error("underlying type must be array, but found: " + variable.type().print(), name)
            );
            throw new IllegalStateException("Expected array type, but found " + variable.type().print());
        }

        return variable;
    }

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        BaseType elementType = ((ArrayType) resolveVariable().type()).elementType();
        return Types.fromBase(elementType);
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
        if (index.isConstant())
            return buildConstantLookup();

        context.errorPrinter().print(
            Notification.error(Errno.CANNOT_LOAD_ARRAY_DYNAMICALLY, "cannot load array dynamically: " + name.value(), this)
                .error("cannot load array dynamically", name)
                .note("the compiler could not figure out how to convert this instruction to static load")
        );
        throw new UnsupportedOperationException("Cannot load array dynamically: " + name.value());
    }

    private @NotNull String buildConstantLookup() {
        if (!index.getValueType().matches(Types.INT))
            throw new IllegalStateException("Expected int");

        int index = Integer.parseInt(index().asConstantValue());
        if (index < 0)
            throw new IllegalStateException("Expected non-negative");

        ArrayType arrayType = (ArrayType) variable.type();
        int capacity = Integer.parseInt(arrayType.capacity().asConstantValue());
        if (index >= capacity)
            throw new IllegalStateException("Index out of bounds");

        return "%%stat_" + variable.name() + "_" + index + "%%";
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return name.value() + "[" + index.print() + "]";
    }
}
