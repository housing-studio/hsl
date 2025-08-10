package org.hsl.compiler.ast.impl.scope;

import org.hsl.compiler.ast.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents an interface that describes, that the implementing class is responsible for managing scopes.
 */
public abstract class ScopeContainer extends Node {
    /**
     * Retrieve the parent scope of this scope.
     * <p>
     * This method will return {@code null}, only if {@code this} scope is the root scope.
     *
     * @return the parent scope of this scope, or {@code null} if {@code this} scope is the root scope
     */
    public abstract @Nullable ScopeContainer getParentScope();

    /**
     * Retrieve the list of child scopes of this scope.
     * <p>
     * If {@code this} scope has no child scopes, this method will return an empty list.
     *
     * @return the list of child scopes of this scope
     */
    public abstract @NotNull List<@NotNull ScopeContainer> getChildrenScopes();
}
