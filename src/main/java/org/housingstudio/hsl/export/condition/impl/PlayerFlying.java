package org.housingstudio.hsl.export.condition.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.export.condition.Condition;
import org.housingstudio.hsl.export.condition.ConditionType;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class PlayerFlying implements Condition {
    private final ConditionType type = ConditionType.PLAYER_FLYING;

    private boolean inverted;
}
