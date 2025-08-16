package org.housingstudio.hsl.export.action.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.export.action.Action;
import org.housingstudio.hsl.export.action.ActionType;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class CancelEvent implements Action {
    private final ActionType type = ActionType.CANCEL_EVENT;
}
