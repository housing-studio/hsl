package org.housingstudio.hsl.compiler.codegen.impl.action;

import org.housingstudio.hsl.compiler.codegen.interop.htsl.HtslInvocation;
import org.jetbrains.annotations.NotNull;

public interface Action {
    @NotNull ActionType type();

    /**
     * Retrieve the HTSL representation of this housing action.
     *
     * @return the htsl code that represents this action
     */
    @NotNull HtslInvocation asHTSL();
}
