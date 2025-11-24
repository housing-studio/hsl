package org.housingstudio.hsl.compiler.optimization.elimination;

import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.control.Return;
import org.housingstudio.hsl.compiler.ast.impl.control.ReturnValue;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.optimization.OptimizationStrategy;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TrailingReturnEliminator implements OptimizationStrategy {
    /**
     * Apply this optimization strategy to a scope.
     *
     * @param scope the scope to optimize
     * @return the number of optimizations applied
     */
    @Override
    public int optimize(@NotNull Scope scope) {
        List<Node> statements = scope.statements();
        if (statements.isEmpty())
            return 0;

        Node node = statements.get(statements.size() - 1);
        if (node instanceof Return) {
            ((Return) node).trailing(true);
            return 1;
        } else if (node instanceof ReturnValue) {
            ((ReturnValue) node).trailing(true);
            return 1;
        }

        return 0;
    }
}
