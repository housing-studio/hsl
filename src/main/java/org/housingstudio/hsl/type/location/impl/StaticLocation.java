package org.housingstudio.hsl.type.location.impl;

import org.housingstudio.hsl.type.location.Location;
import org.housingstudio.hsl.type.location.LocationType;
import org.jetbrains.annotations.NotNull;

public record StaticLocation(@NotNull LocationType type) implements Location {
    /**
     * Get the constant string representation of the value.
     * <p>
     * Housing variables are handled as string by default, this format is the input for housing variables.
     *
     * @return the final string value
     */
    @Override
    public @NotNull String asConstantValue() {
        return type.format();
    }
}
