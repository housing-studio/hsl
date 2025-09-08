package org.housingstudio.hsl.std.slot.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.std.slot.Slot;
import org.housingstudio.hsl.std.slot.SlotType;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class CustomSlot implements Slot {
    private final SlotType type = SlotType.CUSTOM;

    private final @NotNull Value rawSlot;

    /**
     * Get the constant string representation of the value.
     * <p>
     * Housing variables are handled as string by default, this format is the input for housing variables.
     *
     * @return the final string value
     */
    @Override
    public @NotNull String asConstantValue() {
        return type.format() + " " + rawSlot.asConstantValue();
    }
}
