package org.hsl.compiler.ast.builder;

import org.hsl.export.action.Action;
import org.jetbrains.annotations.NotNull;

public interface ActionBuilder extends Builder {
    @NotNull Action buildAction();
}
