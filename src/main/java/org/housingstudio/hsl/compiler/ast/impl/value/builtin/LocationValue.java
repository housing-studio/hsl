package org.housingstudio.hsl.compiler.ast.impl.value.builtin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.std.location.Location;
import org.housingstudio.hsl.std.location.impl.PosLocation;
import org.housingstudio.hsl.std.location.impl.PosLookLocation;
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
        return Types.LOCATION;
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
        switch (location.type()) {
            case SPAWN:
            case INVOKER:
            case CURRENT:
                return "Location::" + location.type().format();
            case POSITION:
                PosLocation pos = (PosLocation) location;
                return String.format("Location::Pos(%s, %s, %s)", pos.x().print(), pos.y().print(), pos.z().print());
            case POSITION_LOOK:
                PosLookLocation posLook = (PosLookLocation) location;
                return String.format(
                    "Location::PosLook(%s, %s, %s, %s, %s)",
                    posLook.x().print(), posLook.y().print(), posLook.z().print(),
                    posLook.yaw(), posLook.pitch()
                );
            default:
                throw new UnsupportedOperationException("Unexpected location type: " + location.type());
        }
    }
}
