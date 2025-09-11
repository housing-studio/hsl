package org.housingstudio.hsl.compiler.codegen.builder;

import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.jetbrains.annotations.NotNull;

public interface ActionBuilder extends Builder {
    @NotNull Action buildAction();
}
