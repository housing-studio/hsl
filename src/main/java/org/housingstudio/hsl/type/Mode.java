package org.housingstudio.hsl.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum Mode {
    SET("Set", 0),
    INCREMENT("Increment", 1),
    DECREMENT("Decrement", 2),
    MULTIPLY("Multiply", 3),
    DIVIDE("Divide", 4);

    private final @NotNull String format;
    private final int offset;
}
