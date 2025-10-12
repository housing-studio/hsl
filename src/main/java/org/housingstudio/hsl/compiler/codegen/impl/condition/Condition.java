package org.housingstudio.hsl.compiler.codegen.impl.condition;

import org.housingstudio.hsl.compiler.codegen.interop.htsl.HtslInvocation;
import org.jetbrains.annotations.NotNull;

public interface Condition {
    @NotNull ConditionType type();
    boolean inverted();

    /**
     * Retrieve the HTSL representation of this housing condition.
     *
     * @return the htsl code that represents this condition
     */
    @NotNull HtslInvocation asHTSL();
}
