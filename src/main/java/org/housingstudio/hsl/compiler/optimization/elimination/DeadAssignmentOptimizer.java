package org.housingstudio.hsl.compiler.optimization.elimination;

import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.local.LocalAssign;
import org.housingstudio.hsl.compiler.ast.impl.local.LocalDeclareAssign;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.optimization.OptimizationStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an optimization strategy that removes dead assignments.
 * <p>
 * This optimizer handles consecutive assignments to the same variable:
 * - LOCAL_ASSIGN: Removes the first assignment
 * - LOCAL_DECLARE_ASSIGN: Updates the initial value instead of removing
 * <p>
 * Examples:
 * <p>
 * 1. Consecutive regular assignments:
 * <pre>
 * x = 100  // This gets removed
 * x = 200  // Only this remains
 * </pre>
 *
 * 2. Declaration followed by assignment:
 * <pre>
 * stat player x: int = 10 // Updated to: stat player x: int = 100
 * x = 100                 // This gets removed (redundant after update)
 * </pre>
 *
 * 3. Assignment with something in between (NOT optimized):
 * <pre>
 * x = 100 // This is kept (something in between)
 * chat($"x = {x}")
 * x = 200 // This is kept (something in between)
 * </pre>
 */
public class DeadAssignmentOptimizer implements OptimizationStrategy {
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

        List<Boolean> shouldRemove = new ArrayList<>();

        // initialize removal flags
        for (int i = 0; i < statements.size(); i++)
            shouldRemove.add(false);

        int optimizations = 0;

        // look for consecutive assignments to the same variable
        for (int i = 0; i < statements.size() - 1; i++) {
            Node currentStmt = statements.get(i);
            Node nextStmt = statements.get(i + 1);

            // check if both are assignments
            if (!isAssignment(currentStmt) || !isAssignment(nextStmt))
                continue;

            String currentVar = getVariableName(currentStmt);
            String nextVar = getVariableName(nextStmt);

            // check if both assignments are to the same variable
            if (currentVar == null || !currentVar.equals(nextVar))
                continue;

            // handle reassignment of stat right after declaration and assignment
            if (currentStmt.nodeType() == NodeType.LOCAL_DECLARE_ASSIGN) {
                // update the initial value and remove the following assign
                updateDeclareAssignValue((LocalDeclareAssign) currentStmt, nextStmt);
                shouldRemove.set(i + 1, true); // remove the following assignment
            }

            // remove first assignment if two assignments follow each other
            else
                shouldRemove.set(i, true);

            optimizations++;
        }

        // remove dead assignments
        List<Node> newStatements = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            if (!shouldRemove.get(i))
                newStatements.add(statements.get(i));
        }

        // update the scope if we made changes
        if (newStatements.size() != statements.size())
            scope.statements(newStatements);

        return optimizations;
    }

    /**
     * Check if a node is an assignment.
     */
    private boolean isAssignment(@NotNull Node node) {
        return node.nodeType() == NodeType.LOCAL_ASSIGN
            || node.nodeType() == NodeType.LOCAL_DECLARE_ASSIGN;
    }

    /**
     * Get the variable name from an assignment node.
     */
    private @Nullable String getVariableName(@NotNull Node node) {
        if (node instanceof LocalAssign)
            return ((LocalAssign) node).name().value();
        else if (node instanceof LocalDeclareAssign)
            return ((LocalDeclareAssign) node).name();
        return null;
    }

    /**
     * Update the value of a LOCAL_DECLARE_ASSIGN node to match the next assignment.
     * This is used when we have: stat player x: int = 10; x = 100
     * We update the declaration to: stat player x: int = 100
     */
    private void updateDeclareAssignValue(@NotNull LocalDeclareAssign declareAssign, @NotNull Node nextAssign) {
        if (!(nextAssign instanceof LocalAssign))
            return;

        LocalAssign nextLocalAssign = (LocalAssign) nextAssign;
        declareAssign.value(nextLocalAssign.value());
    }
}
