package org.housingstudio.hsl.compiler.transform;

import org.housingstudio.hsl.compiler.ast.Game;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.codegen.hierarchy.NodeVisitor;
import org.housingstudio.hsl.compiler.transform.conversion.ArrayStoreIndexConversion;
import org.housingstudio.hsl.compiler.transform.conversion.ReturnValueInliner;
import org.housingstudio.hsl.compiler.transform.elimination.DeadAssignmentEliminator;
import org.housingstudio.hsl.compiler.transform.elimination.TrailingReturnEliminator;
import org.housingstudio.hsl.compiler.transform.lowering.MethodCallExpansion;
import org.housingstudio.hsl.compiler.transform.lowering.OperatorLowering;
import org.housingstudio.hsl.compiler.transform.warning.DeadCodeDetector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an optimization engine that runs various optimization strategies on the AST to improve performance
 * by altering/removing redundant operations.
 */
public class TransformerEngine {
    private final List<ScopeVisitor> scopeVisitors = new ArrayList<>();
    private int totalTransforms = 0;

    public TransformerEngine() {
        //addStrategy(new DeadAssignmentEliminator());
        addStrategy(new ArrayStoreIndexConversion());
        addStrategy(new ReturnValueInliner());
        addStrategy(new TrailingReturnEliminator());
        addStrategy(new DeadCodeDetector());
        addStrategy(new MethodCallExpansion());
        addStrategy(new OperatorLowering());
    }

    /**
     * Optimize a game AST by applying all optimization strategies.
     *
     * @param game the game AST to optimize
     * @return number of optimizations applied
     */
    public int process(@NotNull Game game) {
        totalTransforms = 0;

        // visit all nodes and optimize scopes
        NodeVisitor.visit(this::transformNode);

        return totalTransforms;
    }

    /**
     * Optimize a single node if it's a scope.
     */
    private void transformNode(@NotNull Node node) {
        if (node.nodeType() == NodeType.SCOPE && node instanceof Scope)
            transformScope((Scope) node);
    }

    /**
     * Apply all optimization strategies to a scope.
     */
    private void transformScope(@NotNull Scope scope) {
        for (ScopeVisitor strategy : scopeVisitors) {
            int optimizations = strategy.visit(scope);
            totalTransforms += optimizations;
        }
    }

    /**
     * Add a custom optimization strategy.
     *
     * @param strategy the optimization strategy to add
     */
    public void addStrategy(@NotNull CompilerPass strategy) {
        if (strategy instanceof ScopeVisitor)
            scopeVisitors.add((ScopeVisitor) strategy);
        else
            throw new IllegalArgumentException("Unrecognized compiler pass: " + strategy.getClass().getName());
    }
}
