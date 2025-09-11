package org.housingstudio.hsl.compiler.codegen.builder;

import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ActionListBuilder extends Builder {
    @NotNull List<Action> buildActionList();
}
