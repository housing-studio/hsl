package org.housingstudio.hsl.compiler.parser.impl.operator;

import org.housingstudio.hsl.compiler.ast.impl.operator.BinaryOperator;
import org.housingstudio.hsl.compiler.ast.impl.operator.Operator;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a utility class that makes binary operator nodes and updates the operation tree to correct the order of
 * operations according to the precedence of the operators.
 * <p>
 * The user defines a set of arithmetic, logical, and comparison operators in the source code, and they may not
 * explicitly declare the desired order of these operations. The parser will take care of the operation order by
 * implicitly modifying the operation tree in the AST.
 *
 * @see Operator
 * @see BinaryOperator
 */
public class BinaryOperatorTree {
    /**
     * Create a binary operation node with the specified {@param lhs} (left-hand side) value, {@param operator},
     * and {@param rhs} (right-hand) side value.
     * <p>
     * The operation tree will be updated to correct the order of operations according to the precedence of the
     * operators.
     * <p>
     * For example, the code {@code 1 + 2 * 3} will resolve to {@code (1 + 2) * 3}, according to the tree parsing.
     * However, the transformer will convert this operation to {@code 1 + (2 * 3)}.
     *
     * @param lhs the left-hand side value of the operation
     * @param operator the operator that will be applied to the left-hand side and right-hand side values
     * @param rhs the right-hand side value of the operation
     * @return a new binary operation node
     */
    public static @NotNull Value makeBinaryOperator(
        @NotNull Value lhs, @NotNull Operator operator, @NotNull Value rhs
    ) {
        return updateOperationTree(new BinaryOperator(lhs, operator, rhs));
    }

    /**
     * Update the operation tree to correct the order of operations according to the precedence of the operators.
     * <p>
     * For example, the code {@code 1 + 2 * 3} will resolve to {@code (1 + 2) * 3}, according to the tree parsing.
     * However, the transformer will convert this operation to {@code 1 + (2 * 3)}.
     * <p>
     * The order of the operations will be determined by their precedence defined in
     * {@link Operator#precedence()} and the operation tree be transformed by the parser.
     *
     * @param node the root node of the operation tree
     * @return the updated operation tree
     */
    public static @NotNull Value updateOperationTree(@NotNull Value node) {
        // return if the node itself, if it is not a binary operation
        if (!(node instanceof BinaryOperator operation))
            return node;

        // recursively correct the order of child operands of the binary operation
        operation.lhs(updateOperationTree(operation.lhs()));
        operation.rhs(updateOperationTree(operation.rhs()));

        // check if the current operator has lower precedence than the operator of its right child
        if (operation.rhs() instanceof BinaryOperator rhs && hasPrecedence(operation.operator(), rhs.operator())) {
            // rotate the operation order to the right
            operation.rhs(rhs.lhs());
            rhs.lhs(operation);
            return rhs;
        }

        // check if the current operator has lower or equal precedence than the
        // operator of its left child, and the left child is also an operation
        if (
            operation.lhs() instanceof BinaryOperator lhs && hasPrecedence(operation.operator(), lhs.operator()) &&
            operation.operator().associativity() == 0
        ) {
            // rotate the operation order to the left
            operation.lhs(lhs.rhs());
            lhs.rhs(operation);
            return lhs;
        }

        // the current order is correct, so return the node as it is
        return node;
    }

    /**
     * Indicate, whether the first operator has a precedence priority over the second operator.
     *
     * @param first first operator to check
     * @param second second operator to check
     *
     * @return {@code true} if the first operator has higher precedence than the second one, {@code false} otherwise
     */
    private static boolean hasPrecedence(@NotNull Operator first, @NotNull Operator second) {
        return first.precedence() > second.precedence() ||
            (first.precedence() == second.precedence() && first.associativity() == 0);
    }
}
