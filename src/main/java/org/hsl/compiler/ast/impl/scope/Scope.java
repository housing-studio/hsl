package org.hsl.compiler.ast.impl.scope;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.Node;
import org.hsl.compiler.ast.NodeInfo;
import org.hsl.compiler.ast.NodeType;
import org.hsl.compiler.ast.builder.ActionBuilder;
import org.hsl.compiler.ast.builder.ActionListBuilder;
import org.hsl.compiler.ast.hierarchy.Children;
import org.hsl.compiler.ast.hierarchy.NodeVisitor;
import org.hsl.compiler.ast.hierarchy.Parent;
import org.hsl.compiler.ast.impl.declaration.Method;
import org.hsl.compiler.ast.impl.local.Variable;
import org.hsl.export.action.Action;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a block in the source code that contains a list of instructions.
 * <p>
 * A scope handles the logic for local variables. When local variables go out of this
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.SCOPE)
public class Scope extends ScopeContainer implements ActionListBuilder {
    /**
     * The list of instructions that are associated with the scope.
     */
    @Children
    private final @NotNull List<Node> statements;

    /**
     * The parent scope container of this scope, that might be a {@link Method} or another {@link Scope}.
     * <p>
     * This field is initially {@code null}, and it is set be the {@link NodeVisitor} after initialization.
     */
    @Parent
    private @Nullable ScopeContainer parent;

    @Override
    public @NotNull List<Action> build() {
        List<Action> actions = new ArrayList<>();
        for (Node statement : statements) {
            if (statement instanceof ActionBuilder builder)
                actions.add(builder.build());
        }
        return actions;
    }

    /**
     * Resolve a local variable or a global constant by its specified name.
     * <p>
     * If a node does not override this logic, by default it will try to resolve the value from the {@link #parent()}
     * node.
     * <p>
     * A {@link Scope} will initially try to resolve the value from itself, and then from the parent scope.
     *
     * @param name the name of the variable or constant to resolve
     * @return the value of the variable or constant, or {@code null} if the name is not found
     */
    @Override
    public @Nullable Variable resolveName(@NotNull String name) {
        // try to resolve the value from this scope
        for (Node statement : statements) {
            if (statement instanceof Variable variable && variable.name().equals(name))
                return variable;
        }
        return parent != null ? parent.resolveName(name) : null;
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
        return parent;
    }

    /**
     * Retrieve the list of child scopes of this scope.
     * <p>
     * If {@code this} scope has no child scopes, this method will return an empty list.
     *
     * @return the list of child scopes of this scope
     */
    @Override
    public @NotNull List<@NotNull ScopeContainer> getChildrenScopes() {
        // purposely checking for `Scope` only and not including anonymous methods,
        // as they work a completely different way
        return statements.stream()
            .filter(node -> node instanceof Scope)
            .map(node -> (ScopeContainer) node)
            .toList();
    }
}
