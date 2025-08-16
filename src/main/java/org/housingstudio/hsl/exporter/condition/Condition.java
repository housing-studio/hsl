package org.housingstudio.hsl.exporter.condition;

import org.jetbrains.annotations.NotNull;

public interface Condition {
    @NotNull ConditionType type();
    boolean inverted();
}
