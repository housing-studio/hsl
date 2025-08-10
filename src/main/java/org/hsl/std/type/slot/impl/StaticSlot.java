package org.hsl.std.type.slot.impl;

import org.hsl.std.type.slot.Slot;
import org.hsl.std.type.slot.SlotType;
import org.jetbrains.annotations.NotNull;

public record StaticSlot(@NotNull SlotType type) implements Slot {
}
