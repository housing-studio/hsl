package org.housingstudio.hsl.std.location.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.std.location.Location;
import org.housingstudio.hsl.std.location.LocationType;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class PosLookLocation implements Location {
    private final LocationType type = LocationType.POSITION;

    private final @NotNull Value x, y, z, yaw, pitch;

    /**
     * Get the constant string representation of the value.
     * <p>
     * Housing variables are handled as string by default, this format is the input for housing variables.
     *
     * @return the final string value
     */
    @Override
    public @NotNull String asConstantValue() {
        return type.format() + " " +
            x.asConstantValue() + " " + y.asConstantValue() + " " + z.asConstantValue() + " " +
            yaw.asConstantValue() + " " + pitch.asConstantValue();
    }

    @Override
    public String toString() {
        return String.format(
            "PosLookLocation(x=%s, y=%s, z=%s, yaw=%s, pitch=%s)",
            x.asConstantValue(), y.asConstantValue(), z.asConstantValue(),
            yaw.asConstantValue(), pitch.asConstantValue()
        );
    }
}
