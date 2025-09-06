package org.housingstudio.hsl.compiler.ast.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.declaration.ConstantDeclare;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.CONSTANT_ACCESS)
public class ConstantAccess extends Value {
    private final Stack<Value> accessStack = new Stack<>();
    private final @NotNull Token name;

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        return load().getValueType();
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
        Value value = load();
        if (accessStack.contains(value)) {
            context.syntaxError(name, "Circular initialization flow");
            throw new IllegalStateException("Circular initialization flow: " + name.value());
        }

        accessStack.push(value);

        try {
            return value.asConstantValue();
        } finally {
            accessStack.pop();
        }
    }

    @Override
    public @NotNull Value load() {
        ConstantDeclare constant = game.constants().get(name.value());
        if (constant == null) {
            context.syntaxError(name, "Constant not found");
            throw new UnsupportedOperationException("Cannot find constant: " + name.value());
        }

        return constant.value();
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
