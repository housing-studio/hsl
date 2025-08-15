package org.hsl.export.condition;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Accessors(fluent = true)
@Getter
@Setter
public abstract class Condition {
    private boolean inverted;

    public abstract @NotNull ConditionType type();
}
