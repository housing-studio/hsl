package org.hsl.std.type.location.impl;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.hsl.std.type.location.Location;
import org.hsl.std.type.location.LocationType;

@Accessors(fluent = true)
@Getter
public class LocationCurrent implements Location {
    private final LocationType type = LocationType.CURRENT;
}
