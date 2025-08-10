package org.hsl.compiler.ast.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.impl.declaration.Constant;
import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.CONSTANT_ACCESS)
public class ConstantAccess extends Value {
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

    public @NotNull Value load() {
        Constant constant = game.constants().get(name.value());
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
