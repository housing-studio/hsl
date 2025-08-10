package org.hsl.std.type.location.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.std.type.location.Location;
import org.hsl.std.type.location.LocationType;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class LocationCustom implements Location {
    private final LocationType type = LocationType.CURRENT;

    private final double x, y, z;
}
