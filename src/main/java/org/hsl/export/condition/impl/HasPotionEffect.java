package org.hsl.export.condition.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.export.condition.Condition;
import org.hsl.export.condition.ConditionType;
import org.hsl.std.type.Effect;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class HasPotionEffect extends Condition {
    private final ConditionType type = ConditionType.HAS_POTION_EFFECT;

    private @NotNull Effect effect;
}
