package org.housingstudio.hsl.compiler.ast.impl.control;

import lombok.RequiredArgsConstructor;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.codegen.builder.ActionBuilder;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@NodeInfo(type = NodeType.BUILTIN)
public class StaticActionBuilder extends Node implements ActionBuilder {
    private final @NotNull Action action;

    @Override
    public @NotNull Action buildAction() {
        return action;
    }
}
