package org.housingstudio.hsl.compiler.optimization;

import org.housingstudio.hsl.compiler.ast.Game;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.codegen.hierarchy.NodeVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an optimization engine that runs various optimization strategies on the AST to improve performance
 * by altering/removing redundant operations.
 */
public class Optimizer {
    private final List<OptimizationStrategy> strategies = new ArrayList<>();
    private int totalOptimizations = 0;

    public Optimizer() {
        strategies.add(new DeadAssignmentEliminator());
        strategies.add(new ArrayStoreIndexConversion());
    }

    /**
     * Optimize a game AST by applying all optimization strategies.
     *
     * @param game the game AST to optimize
     * @return number of optimizations applied
     */
    public int optimize(@NotNull Game game) {
        totalOptimizations = 0;

        // visit all nodes and optimize scopes
        NodeVisitor.visit(this::optimizeNode);

        return totalOptimizations;
    }

    /**
     * Optimize a single node if it's a scope.
     */
    private void optimizeNode(@NotNull Node node) {
        if (node.nodeType() == NodeType.SCOPE && node instanceof Scope)
            optimizeScope((Scope) node);
    }

    /**
     * Apply all optimization strategies to a scope.
     */
    private void optimizeScope(@NotNull Scope scope) {
        for (OptimizationStrategy strategy : strategies) {
            int optimizations = strategy.optimize(scope);
            totalOptimizations += optimizations;
        }
    }

    /**
     * Add a custom optimization strategy.
     *
     * @param strategy the optimization strategy to add
     */
    public void addStrategy(@NotNull OptimizationStrategy strategy) {
        strategies.add(strategy);
    }
}
