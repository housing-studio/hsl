package org.hsl.export.action.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.hsl.export.action.Action;
import org.hsl.export.action.ActionType;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class ClearAllPotionEffects implements Action {
    private final ActionType type = ActionType.CLEAR_ALL_POTION_EFFECTS;
}
