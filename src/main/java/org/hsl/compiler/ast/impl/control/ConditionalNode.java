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
import org.hsl.export.action.Action;
import org.hsl.export.action.impl.Conditional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.CONDITIONAL)
public class ConditionalNode extends Node implements ActionBuilder {
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
}
