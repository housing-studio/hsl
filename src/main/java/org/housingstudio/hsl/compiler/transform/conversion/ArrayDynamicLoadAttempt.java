package org.housingstudio.hsl.compiler.transform.conversion;

import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.transform.ScopeVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ArrayDynamicLoadAttempt implements ScopeVisitor {
    /**
     * Apply this optimization strategy to a scope.
     *
     * @param scope the scope to optimize
     * @return the number of optimizations applied
     */
    @Override
    public int visit(@NotNull Scope scope) {
        List<Node> statements = scope.statements();

        return 0;
    }
}
