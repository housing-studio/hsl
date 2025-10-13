package org.housingstudio.hsl.compiler.optimization;

import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an AST optimization strategy.
 */
public interface OptimizationStrategy {
    /**
     * Apply this optimization strategy to a scope.
     *
     * @param scope the scope to optimize
     * @return the number of optimizations applied
     */
    int optimize(@NotNull Scope scope);
}
