package org.hsl.export.condition.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.hsl.export.condition.Condition;
import org.hsl.export.condition.ConditionType;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class PlayerFlying extends Condition {
    private final ConditionType type = ConditionType.PLAYER_FLYING;
}
