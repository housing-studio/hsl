package org.housingstudio.hsl.exporter.condition.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.condition.Condition;
import org.housingstudio.hsl.exporter.condition.ConditionType;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class PlayerSneaking implements Condition {
    private final ConditionType type = ConditionType.PLAYER_SNEAKING;

    private boolean inverted;
}
