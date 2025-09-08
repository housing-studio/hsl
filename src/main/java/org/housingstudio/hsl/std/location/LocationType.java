package org.housingstudio.hsl.std.location;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum LocationType {
    SPAWN("Spawn", 0),
    INVOKER("Invoker", 1),
    CURRENT("Current", 2),
    CUSTOM("Custom", 3);

    private final @NotNull String format;
    private final int offset;
}
