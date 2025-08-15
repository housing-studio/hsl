package org.hsl.compiler.ast.impl.control;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.Node;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.builder.ActionBuilder;
import org.hsl.compiler.ast.builder.ConditionBuilder;
import org.hsl.compiler.ast.impl.scope.Scope;
import org.hsl.compiler.debug.Printable;
import org.hsl.export.action.Action;
import org.hsl.export.action.impl.Conditional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.CONDITIONAL)
public class ConditionalNode extends Node implements ActionBuilder, Printable {
    private final @NotNull List<ConditionBuilder> conditions;

    private final boolean matchAnyCases;

    private final @NotNull Scope ifScope;
    private final @Nullable Scope elseScope;

    @Override
    public @NotNull Action buildAction() {
        return new Conditional(
            conditions.stream().map(ConditionBuilder::buildCondition).toList(),
            matchAnyCases,
            ifScope.buildActionList(),
            elseScope != null ? elseScope.buildActionList() : new ArrayList<>()
        );
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        String operator = matchAnyCases ? "||" : "&&";

        StringBuilder builder = new StringBuilder();
        builder.append("if (");

        Iterator<ConditionBuilder> iterator = conditions.iterator();
        while (iterator.hasNext()) {
            ConditionBuilder condition = iterator.next();
            if (condition instanceof Printable printable)
                builder.append(printable.print());
            else
                builder.append(condition.getClass().getSimpleName());
            if (iterator.hasNext())
                builder.append(" ").append(operator).append(" ");
        }

        builder.append(") ");
        builder.append(ifScope.print());
        if (elseScope != null) {
            builder.append("else ");
            builder.append(elseScope.print());
        }
        return builder.toString();
    }
}
