package org.housingstudio.hsl.compiler.ast.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.ast.impl.type.BaseType;
import org.housingstudio.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.STAT_ACCESS)
public class StatAccess extends Value {
    private final @NotNull Token name;

    @Children
    private final @NotNull Variable variable;

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull BaseType getValueType() {
        return variable.type();
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
        String prefix;
        switch (variable.namespace()) {
            case PLAYER:
                prefix = "stat";
                break;
            case TEAM:
                prefix = "teamstat";
                break;
            case GLOBAL:
                prefix = "globalstat";
                break;
            default:
                throw new IllegalStateException("Unexpected variable namespace: " + variable.namespace());
        }
        return "%%" + prefix + "_" + name.value() + "%%";
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return "load " + name.value();
    }
}
