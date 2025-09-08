package org.housingstudio.hsl.exporter.condition.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.condition.Condition;
import org.housingstudio.hsl.exporter.condition.ConditionType;
import org.housingstudio.hsl.std.Effect;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class HasPotionEffect implements Condition {
    private final ConditionType type = ConditionType.HAS_POTION_EFFECT;

    private boolean inverted;
    private @NotNull Effect effect;
}
