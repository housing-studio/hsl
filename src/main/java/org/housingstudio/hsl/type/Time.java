package org.housingstudio.hsl.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum Time {
    WORLD("World", 0),
    SUNRISE("Sunrise", 1),
    NOON("Noon", 2),
    SUNSET("Sunset", 3),
    MIDNIGHT("Midnight", 4);

    private final @NotNull String format;
    private final int offset;
}
