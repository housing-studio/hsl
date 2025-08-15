package org.hsl.export.condition.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.export.condition.Condition;
import org.hsl.export.condition.ConditionType;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class WithinRegion implements Condition {
    private final ConditionType type = ConditionType.WITHIN_REGION;

    private boolean inverted;

    private @NotNull String region;
}
