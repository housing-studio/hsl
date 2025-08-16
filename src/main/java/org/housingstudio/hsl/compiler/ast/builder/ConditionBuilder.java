package org.housingstudio.hsl.compiler.ast.builder;

import org.housingstudio.hsl.exporter.condition.Condition;
import org.jetbrains.annotations.NotNull;

public interface ConditionBuilder extends Builder {
    @NotNull Condition buildCondition();
    void invert();
}
