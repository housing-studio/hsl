package org.housingstudio.hsl.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum Comparator {
    LESS_THAN("LessThan"),
    LESS_THAN_OR_EQUAL("LessThanOrEqual"),
    EQUAL("Equal"),
    GREATER_THAN("GreaterThan"),
    GREATER_THAN_OR_EQUAL("GreaterThanOrEqual");

    private final @NotNull String format;
}
