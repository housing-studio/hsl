package org.housingstudio.hsl.compiler.optimization.conversion;

import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.control.ReturnValue;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.optimization.OptimizationStrategy;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReturnValueInliner implements OptimizationStrategy {
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

        int steps = 0;
        for (int i = 1; i < statements.size(); i++) {
            Node previousStmt = statements.get(i - 1);
            Node currentStmt = statements.get(i);

            if (!(currentStmt instanceof ReturnValue))
                continue;
            ReturnValue returnValue = (ReturnValue) currentStmt;

            if (!(previousStmt instanceof Variable))
                continue;
            Variable variable = (Variable) previousStmt;

            Value value = variable.load();
            if (value == null || !value.isConstant())
                continue;

            returnValue.value(value);
            steps++;
        }

        return steps;
    }
}
