package org.housingstudio.hsl.compiler.ast.impl.placeholder;

import lombok.RequiredArgsConstructor;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.parser.impl.action.ArgAccess;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@NodeInfo(type = NodeType.PLACEHOLDER)
public class PlaceholderValue extends Value {
    private final @NotNull Placeholder placeholder;
    private final @NotNull ArgAccess args;

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        return placeholder.method().returnType();
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
        String build = placeholder.mapper()
            .apply(args)
            .build();
        String suffix = "";
        if (getValueType().matches(Types.FLOAT))
            suffix = "D";
        else if (getValueType().matches(Types.INT))
            suffix = "L";
        return build + suffix;
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return "placeholder " + placeholder.name();
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
