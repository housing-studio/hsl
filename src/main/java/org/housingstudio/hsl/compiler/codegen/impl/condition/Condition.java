package org.housingstudio.hsl.compiler.codegen.impl.condition;

import org.jetbrains.annotations.NotNull;

public interface Condition {
    @NotNull ConditionType type();
    boolean inverted();
}
