package org.housingstudio.hsl.exporter.action.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.action.ActionType;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class Exit implements Action {
    private final ActionType type = ActionType.EXIT;
}
