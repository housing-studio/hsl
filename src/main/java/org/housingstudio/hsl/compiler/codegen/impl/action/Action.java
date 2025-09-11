package org.housingstudio.hsl.compiler.codegen.impl.action;

import org.jetbrains.annotations.NotNull;

public interface Action {
    @NotNull ActionType type();
}
