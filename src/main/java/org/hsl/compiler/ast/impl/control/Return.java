package org.hsl.compiler.ast.impl.control;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.Node;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.builder.ActionBuilder;
import org.hsl.compiler.debug.Printable;
import org.hsl.export.action.Action;
import org.hsl.export.action.impl.Exit;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.RETURN)
public class Return extends Node implements ActionBuilder, Printable {
    @Override
    public @NotNull Action buildAction() {
        return new Exit();
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
