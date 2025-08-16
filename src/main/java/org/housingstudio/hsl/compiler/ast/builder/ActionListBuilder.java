package org.housingstudio.hsl.compiler.ast.builder;

import org.housingstudio.hsl.export.action.Action;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ActionListBuilder extends Builder {
    @NotNull List<Action> buildActionList();
}
