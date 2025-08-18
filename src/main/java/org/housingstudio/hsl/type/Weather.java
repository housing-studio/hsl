package org.housingstudio.hsl.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum Weather {
    SUNNY("Sunny", 0),
    RAINING("Raining", 1);

    private final @NotNull String format;
    private final int offset;
}
