package org.hsl.compiler.ast.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.ARGUMENT)
public class Argument extends Value {
    private final @Nullable Token name;
    private final @NotNull Value value;

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        return value.getValueType();
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
        return value.asConstantValue();
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return (name != null ? name.value() + " = " : "") + value.print();
    }
}
