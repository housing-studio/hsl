package org.housingstudio.hsl.exporter.condition.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.condition.Condition;
import org.housingstudio.hsl.exporter.condition.ConditionType;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class PlayerFlying implements Condition {
    private final ConditionType type = ConditionType.PLAYER_FLYING;

    private boolean inverted;
}
