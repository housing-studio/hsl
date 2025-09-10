package org.housingstudio.hsl.compiler.ast.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.hierarchy.Children;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a single-target value container.
 * <p>
 * Basically a group has no real effect other than ensuring that precedence is applied correctly.
 */
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.GROUP)
@RequiredArgsConstructor
public class Group extends Value {
    @Children
    private final @NotNull Value content;

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        return content.getValueType();
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
        return content.asConstantValue();
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return content.print();
    }
}
