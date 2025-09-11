package org.housingstudio.hsl.compiler.ast.impl.control;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.codegen.builder.ActionBuilder;
import org.housingstudio.hsl.compiler.codegen.builder.ConditionBuilder;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.action.impl.Conditional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
            conditions.stream().map(ConditionBuilder::buildCondition).collect(Collectors.toList()),
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
            if (condition instanceof Printable)
                builder.append(((Printable) condition).print());
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
