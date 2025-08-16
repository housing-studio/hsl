package org.housingstudio.hsl.export.condition;

import org.jetbrains.annotations.NotNull;

public interface Condition {
    @NotNull ConditionType type();
    boolean inverted();
}
