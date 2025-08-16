package org.housingstudio.hsl.type.slot.impl;

import org.housingstudio.hsl.type.slot.Slot;
import org.housingstudio.hsl.type.slot.SlotType;
import org.jetbrains.annotations.NotNull;

public record StaticSlot(@NotNull SlotType type) implements Slot {
    /**
     * Get the constant string representation of the value.
     * <p>
     * Housing variables are handled as string by default, this format is the input for housing variables.
     *
     * @return the final string value
     */
    @Override
    public @NotNull String asConstantValue() {
        return type.format();
    }
}
