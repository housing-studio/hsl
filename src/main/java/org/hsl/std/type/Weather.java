package org.hsl.std.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum Weather {
    SUNNY("Sunny"),
    RAINING("Raining");

    private final @NotNull String format;
}
