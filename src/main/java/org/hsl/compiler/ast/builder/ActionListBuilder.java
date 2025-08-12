package org.hsl.compiler.ast.builder;

import org.hsl.export.action.Action;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ActionListBuilder extends Builder {
    @NotNull List<Action> buildActionList();
}
