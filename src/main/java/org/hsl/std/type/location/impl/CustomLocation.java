package org.hsl.std.type.location.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.std.type.location.Location;
import org.hsl.std.type.location.LocationType;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class CustomLocation implements Location {
    private final LocationType type = LocationType.CUSTOM;

    private final @NotNull Value x, y, z;

    /**
     * Get the constant string representation of the value.
     * <p>
     * Housing variables are handled as string by default, this format is the input for housing variables.
     *
     * @return the final string value
     */
    @Override
    public @NotNull String asConstantValue() {
        return type.format() + " " + x.asConstantValue() + " " + y.asConstantValue() + " " + z.asConstantValue();
    }
}
