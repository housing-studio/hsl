package org.hsl.export.action.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.hsl.export.action.Action;
import org.hsl.export.action.ActionType;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class ResetInventory implements Action {
    private final ActionType type = ActionType.RESET_INVENTORY;
}
