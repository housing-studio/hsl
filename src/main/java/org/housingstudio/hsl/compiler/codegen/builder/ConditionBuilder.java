package org.housingstudio.hsl.compiler.codegen.builder;

import org.housingstudio.hsl.compiler.codegen.impl.condition.Condition;
import org.jetbrains.annotations.NotNull;

public interface ConditionBuilder extends Builder {
    @NotNull Condition buildCondition();
    void invert();
}
