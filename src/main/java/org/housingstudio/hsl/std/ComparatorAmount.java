package org.housingstudio.hsl.std;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum ComparatorAmount {
    ANY("Any"),
    GREATER_OR_EQUAL("GreaterOrEqual");

    private final @NotNull String format;
}
