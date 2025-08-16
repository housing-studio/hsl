package org.housingstudio.hsl.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum GameMode {
    ADVENTURE("Adventure"),
    SURVIVAL("Survival"),
    CREATIVE("Creative");

    private final @NotNull String format;
}
