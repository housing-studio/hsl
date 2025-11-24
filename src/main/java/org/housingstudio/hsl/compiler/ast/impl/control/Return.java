package org.housingstudio.hsl.compiler.ast.impl.control;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.codegen.builder.ActionListBuilder;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.action.impl.Exit;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.RETURN)
public class Return extends Node implements ActionListBuilder, Printable {
    @Setter
    private boolean trailing;

    @Override
    public @NotNull List<Action> buildActionList() {
        if (trailing)
            return Collections.emptyList();
        return Collections.singletonList(new Exit());
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return "return";
    }
}
