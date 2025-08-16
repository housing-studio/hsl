package org.housingstudio.hsl.type.location;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum LocationType {
    SPAWN("Spawn"),
    INVOKER("Invoker"),
    CURRENT("Current"),
    CUSTOM("Custom");

    private final @NotNull String format;
}
