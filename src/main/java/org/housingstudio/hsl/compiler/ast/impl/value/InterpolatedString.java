package org.housingstudio.hsl.compiler.ast.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.hierarchy.Children;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.INTERPOLATED_STRING)
public class InterpolatedString extends Value {
    /**
     * The raw string token value of the interpolation.
     */
    private final @NotNull Token value;

    /**
     * The list of parsed raw strings and value nodes.
     */
    @Children
    private final @NotNull List<Object> parts;

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        return Type.STRING;
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
        StringBuilder builder = new StringBuilder();
        for (Object part : parts) {
            if (part instanceof String)
                builder.append((String) part);
            else if (part instanceof Value)
                builder.append(((Value) part).load().asConstantValue());
            else
                throw new IllegalStateException("Unexpected interpolation part: " + part);
        }
        return builder.toString();
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        StringBuilder builder = new StringBuilder("$").append('"');
        for (Object part : parts) {
            if (part instanceof Printable)
                builder.append(((Printable) part).print());
            else
                builder.append(part);
        }
        return builder.append('"').toString();
    }
}
