package org.housingstudio.hsl.compiler.ast.impl.control;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.scope.ScopeContainer;
import org.housingstudio.hsl.compiler.codegen.builder.ActionBuilder;
import org.housingstudio.hsl.compiler.codegen.builder.ConditionBuilder;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.action.impl.Conditional;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.CONDITIONAL)
public class ConditionalNode extends ScopeContainer implements ActionBuilder, Printable {
    @Children
    private final @NotNull List<ConditionBuilder> conditions;

    private final boolean matchAnyCases;

    @Children
    private final @NotNull Scope ifScope;

    @Children
    private final @Nullable Scope elseScope;

    @Override
    public void init() {
        if (!(parent() instanceof Scope))
            return;

        Scope scope = (Scope) parent();
        if (scope.parent() != null && !scope.parent().isTopLevelNode()) {
            context.errorPrinter().print(
                Notification.error(Errno.ILLEGAL_NESTED_CONDITIONAL, "unexpected nested conditional")
                    .error("conditionals cannot have nested conditionals", context.peek())
            );
        }
    }

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

    /**
     * Retrieve the parent scope of this scope.
     * <p>
     * This method will return {@code null}, only if {@code this} scope is the root scope.
     *
     * @return the parent scope of this scope, or {@code null} if {@code this} scope is the root scope
     */
    @Override
    public @Nullable ScopeContainer getParentScope() {
        return (ScopeContainer) parent();
    }

    /**
     * Retrieve the list of child scopes of this scope.
     * <p>
     * If {@code this} scope has no child scopes, this method will return an empty list.
     *
     * @return the list of child scopes of this scope
     */
    @Override
    public @NotNull List<ScopeContainer> getChildrenScopes() {
        return Arrays.asList(ifScope, elseScope);
    }
}
