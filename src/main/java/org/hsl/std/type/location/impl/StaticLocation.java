package org.hsl.std.type.location.impl;

import org.hsl.std.type.location.Location;
import org.hsl.std.type.location.LocationType;
import org.jetbrains.annotations.NotNull;

public record StaticLocation(@NotNull LocationType type) implements Location {
}
