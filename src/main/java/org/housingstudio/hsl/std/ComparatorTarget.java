package org.housingstudio.hsl.std;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum ComparatorTarget {
    HAND("Hand"),
    ARMOR("Armor"),
    HOTBAR("Hotbar"),
    INVENTORY("Inventory"),
    CURSOR("Cursor"),
    CRAFTING_GRID("CraftingGrid"),
    ANYWHERE("Anywhere");

    private final @NotNull String format;
}
