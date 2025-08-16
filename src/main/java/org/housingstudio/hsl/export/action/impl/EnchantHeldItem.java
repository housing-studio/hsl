package org.housingstudio.hsl.export.action.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.export.action.Action;
import org.housingstudio.hsl.export.action.ActionType;
import org.housingstudio.hsl.type.Enchant;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class EnchantHeldItem implements Action {
    private final ActionType type = ActionType.ENCHANT_HELD_ITEM;

    private @NotNull Enchant enchant;
    private int level;
}
