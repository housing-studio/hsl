package org.hsl.compiler.ast.impl.value;

import org.hsl.compiler.ast.Node;
import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.debug.Constant;
import org.hsl.compiler.debug.Printable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an abstract node in the Abstract Syntax Tree that holds a value.
 * <p>
 * The {@link #getValueType()} will dynamically resolve the type of the held value. This is important for
 * interred types, where the type will be resolved later.
 */
public abstract class Value extends Node implements Printable, Constant {
    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    public abstract @NotNull Type getValueType();

    public @NotNull Value load() {
        if (this instanceof ConstantAccess access)
            return access.load();
        return this;
    }
}
