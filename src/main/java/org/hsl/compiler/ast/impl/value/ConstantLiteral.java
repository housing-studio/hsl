package org.hsl.compiler.ast.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.debug.Printable;
import org.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a value node in the Abstract Syntax Tree, that holds a constant value.
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.LITERAL)
public class ConstantLiteral extends Value implements Printable {
    /**
     * The held constant value of the literal.
     */
    private final @NotNull Token value;

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        return switch (value.type()) {
            case STRING -> Type.STRING;
            case INT -> Type.INT;
            case FLOAT -> Type.FLOAT;
            case BOOL -> Type.BOOL;
            default -> throw new IllegalStateException("Cannot resolve type of token: " + value);
        };
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return value.value();
    }
}
