package org.housingstudio.hsl.compiler.transform.conversion;

import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.local.LocalAssign;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.statement.ArrayStore;
import org.housingstudio.hsl.compiler.transform.ScopeVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents an optimization strategy that tries to convert dynamic array store instructions to static
 * array store instructions by checking if the previous instruction assigns to the dynamic index.
 */
public class ArrayStoreIndexConversion implements ScopeVisitor {
    // TODO this would not work until array store's lowering would be moved here
    //  basically the AST parse flow already converts dynamic stores to if statements, so this
    //  condition will never be true.

    /**
     * Apply this optimization strategy to a scope.
     *
     * @param scope the scope to optimize
     * @return the number of optimizations applied
     */
    @Override
    public int visit(@NotNull Scope scope) {
        List<Node> statements = scope.statements();
        if (statements.isEmpty())
            return 0;

        int steps = 0;
        for (int i = 1; i < statements.size(); i++) {
            Node previousStmt = statements.get(i - 1);
            Node currentStmt = statements.get(i);

            if (!isDynamicArrayStore(currentStmt))
                continue;

            ArrayStore store = (ArrayStore) currentStmt;

            if (!(previousStmt instanceof LocalAssign))
                continue;

            LocalAssign assign = (LocalAssign) previousStmt;
            if (!assign.value().isConstant())
                continue;

            if (!store.name().equals(assign.name()))
                continue;

            store.index(assign.value());
            steps++;
        }

        return steps;
    }

    private boolean isDynamicArrayStore(@NotNull Node node) {
        if (!(node instanceof ArrayStore))
            return false;

        ArrayStore arrayStore = (ArrayStore) node;
        return !arrayStore.index().isConstant();
    }
}
