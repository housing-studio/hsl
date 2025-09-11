package org.housingstudio.hsl.compiler.codegen.impl.action.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.action.ActionType;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class ParkourCheckpoint implements Action {
    private final ActionType type = ActionType.PARKOUR_CHECKPOINT;
}
