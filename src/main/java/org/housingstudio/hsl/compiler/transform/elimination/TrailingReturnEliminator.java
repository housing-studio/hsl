package org.housingstudio.hsl.compiler.transform.elimination;

import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.control.Return;
import org.housingstudio.hsl.compiler.ast.impl.control.ReturnValue;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.ErrorPrinter;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.transform.ScopeVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a return statement eliminator from unwanted places.
 * <p>
 * Value returning works by storing the return value to a special stat reserved for the method. Then an exit action is
 * placed afterward.
 * <p>
 * Normal void-returns simply translate to an exit action.
 * <p>
 * However Hypixel Runtime only allows exit actions to be specified within conditional scopes, so we need to make sure
 * not to have exit actions on top level scopes.
 * <p>
 * This results in that we cannot have return statements midway in methods on top level scope, except when it is the
 * method's end, whereas we can just simply remove the action.
 */
public class TrailingReturnEliminator implements ScopeVisitor {
    /**
     * Apply this optimization strategy to a scope.
     *
     * @param scope the scope to optimize
     * @return the number of optimizations applied
     */
    @Override
    public int visit(@NotNull Scope scope) {
        List<Node> body = scope.statements();
        if (body.isEmpty())
            return 0;

        for (int i = 0; i < body.size(); i++) {
            Node node = body.get(i);

            // ignore non-return nodes
            boolean isReturn = node instanceof Return || node instanceof ReturnValue;
            if (!isReturn)
                continue;

            // check if the return statement was put before the end of a scope
            boolean isTrailing = i == body.size() - 1;
            if (!isTrailing) {
                ErrorPrinter.getInstance().print(
                    Notification
                        .error(Errno.NON_TERMINAL_RETURN, "unexpected non-terminal returns")
                        .error("return statement must be declared last in a scope", node.tokens().get(0))
                );
                continue;
            }

            // mark return statements "trailing" if they are on the top level scope, as they are not allowed by Hypixel
            if (!scope.isTopLevelScope())
                continue;

            if (node instanceof Return)
                ((Return) node).trailing(true);
            else
                ((ReturnValue) node).trailing(true);
            return 1;
        }

        return 0;
    }
}
