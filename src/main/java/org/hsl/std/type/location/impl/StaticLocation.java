package org.hsl.std.type.location.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.std.type.location.Location;
import org.hsl.std.type.location.LocationType;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class StaticLocation implements Location {
    private final @NotNull LocationType type;
}
