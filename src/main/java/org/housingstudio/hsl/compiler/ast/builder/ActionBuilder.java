package org.housingstudio.hsl.compiler.ast.builder;

import org.housingstudio.hsl.export.action.Action;
import org.jetbrains.annotations.NotNull;

public interface ActionBuilder extends Builder {
    @NotNull Action buildAction();
}
