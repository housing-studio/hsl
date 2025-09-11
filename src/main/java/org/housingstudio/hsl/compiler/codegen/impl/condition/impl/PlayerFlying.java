package org.housingstudio.hsl.compiler.codegen.impl.condition.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.codegen.impl.condition.Condition;
import org.housingstudio.hsl.compiler.codegen.impl.condition.ConditionType;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class PlayerFlying implements Condition {
    private final ConditionType type = ConditionType.PLAYER_FLYING;

    private boolean inverted;
}
