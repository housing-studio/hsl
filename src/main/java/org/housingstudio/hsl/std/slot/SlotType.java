package org.housingstudio.hsl.std.slot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum SlotType {
    HELMET("Helmet"),
    CHESTPLATE("Chestplate"),
    LEGGINGS("Leggings"),
    BOOTS("Boots"),
    FIRST_AVAILABLE("FirstAvailable"),
    HAND("Hand"),
    CUSTOM("Custom"),
    INVENTORY("Inventory"),
    HOTBAR("Hotbar");

    private final @NotNull String format;
}
