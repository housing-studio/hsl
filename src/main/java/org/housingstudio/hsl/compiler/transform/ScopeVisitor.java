package org.housingstudio.hsl.compiler.transform;

import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.jetbrains.annotations.NotNull;

/**
 * Represents compiler pass over AST strategy.
 */
public interface ScopeVisitor extends CompilerPass {
    /**
     * Apply this strategy to a scope.
     *
     * @param scope the scope to transform
     * @return the number of transformations applied
     */
    int visit(@NotNull Scope scope);
}
