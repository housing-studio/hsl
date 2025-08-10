package org.hsl.std.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum Mode {
    SET("Set"),
    INCREMENT("Increment"),
    DECREMENT("Decrement"),
    MULTIPLY("Multiply"),
    DIVIDE("Divide");

    private final @NotNull String format;
}
