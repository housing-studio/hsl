package org.hsl.std.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum Namespace {
    PLAYER("Player"),
    TEAM("Team"),
    GLOBAL("Global");

    private final @NotNull String format;
}
