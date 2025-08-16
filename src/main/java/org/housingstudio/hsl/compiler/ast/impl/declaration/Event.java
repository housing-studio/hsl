package org.housingstudio.hsl.compiler.ast.impl.declaration;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.builder.ActionListBuilder;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.debug.Format;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.generic.EventType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.EVENT)
public class Event extends Node implements Printable, ActionListBuilder {
    private final @NotNull EventType type;
    private final @NotNull Scope scope;

    @Override
    public @NotNull List<Action> buildActionList() {
        return scope.buildActionList();
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        StringBuilder builder = new StringBuilder(Format.RED + "event " + Format.BLUE + type.format());
        builder.append(Format.CYAN).append("()").append(Format.LIGHT_GRAY).append(" {").append('\n');

        for (Node statement : scope.statements()) {
            if (statement instanceof Printable printable)
                builder.append('\t').append(Format.WHITE).append(printable.print()).append('\n');
            else
                builder.append('\t').append(Format.WHITE).append(statement.getClass().getSimpleName()).append('\n');
        }

        builder.append(Format.LIGHT_GRAY).append('}');
        return builder.toString();
    }
}
