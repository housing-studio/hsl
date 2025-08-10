package org.hsl.compiler.ast.impl.value.builtin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.std.type.location.Location;
import org.hsl.std.type.location.impl.CustomLocation;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.BUILTIN)
public class LocationValue extends Value {
    private final @NotNull Location location;

    /**
     * Retrieve the type of the held value. This result will be used to inter types for untyped variables.
     *
     * @return the resolved value of the type
     */
    @Override
    public @NotNull Type getValueType() {
        return Type.LOCATION;
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
        return location.asConstantValue();
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return switch (location.type()) {
            case SPAWN, INVOKER, CURRENT -> "Location::" + location.type().format();
            case CUSTOM -> {
                CustomLocation loc = (CustomLocation) location;
                yield "Location::Custom(%s, %s, %s)".formatted(loc.x().print(), loc.y().print(), loc.z().print());
            }
        };
    }
}
