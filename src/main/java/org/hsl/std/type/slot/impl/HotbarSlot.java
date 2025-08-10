package org.hsl.std.type.slot.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.std.type.slot.Slot;
import org.hsl.std.type.slot.SlotType;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class HotbarSlot implements Slot {
    private final SlotType type = SlotType.HOTBAR;

    private final int inventorySlot;
}
