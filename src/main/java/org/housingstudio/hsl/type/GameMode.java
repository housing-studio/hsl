package org.housingstudio.hsl.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum GameMode {
    ADVENTURE("Adventure", 0),
    SURVIVAL("Survival", 1),
    CREATIVE("Creative", 2);

    private final @NotNull String format;
    private final int offset;
}
