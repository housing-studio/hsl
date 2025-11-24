package org.housingstudio.hsl.compiler.optimization.warning;

import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.control.Return;
import org.housingstudio.hsl.compiler.ast.impl.control.ReturnValue;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.error.ErrorPrinter;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.error.Warning;
import org.housingstudio.hsl.compiler.optimization.OptimizationStrategy;
import org.housingstudio.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DeadCodeDetector implements OptimizationStrategy {
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

        boolean returnCalled = false;
        for (Node statement : statements) {
            if (statement instanceof Return || statement instanceof ReturnValue) {
                returnCalled = true;
                continue;
            }

            if (statement instanceof Scope)
                optimize((Scope) statement);

            if (!returnCalled)
                continue;

            List<Token> tokens = statement.tokens();
            if (tokens.isEmpty())
                continue;

            ErrorPrinter.getInstance().print(
                Notification.warning(Warning.DEAD_CODE, "unreachable code", statement)
                    .error("unreachable code", tokens.get(0))
            );
        }

        return 0;
    }
}
