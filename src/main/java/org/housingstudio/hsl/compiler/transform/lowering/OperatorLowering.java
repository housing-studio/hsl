package org.housingstudio.hsl.compiler.transform.lowering;

import lombok.RequiredArgsConstructor;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.control.ReturnValue;
import org.housingstudio.hsl.compiler.ast.impl.local.LocalAssign;
import org.housingstudio.hsl.compiler.ast.impl.local.LocalDeclareAssign;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.ast.impl.operator.AssignmentOperator;
import org.housingstudio.hsl.compiler.ast.impl.operator.BinaryOperator;
import org.housingstudio.hsl.compiler.ast.impl.operator.Operator;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.ast.impl.value.ConstantAccess;
import org.housingstudio.hsl.compiler.ast.impl.value.Group;
import org.housingstudio.hsl.compiler.ast.impl.value.StatAccess;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.housingstudio.hsl.compiler.transform.ScopeVisitor;
import org.housingstudio.hsl.std.Namespace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Lowers binary operators into assignment and update operations.
 * <p>
 * This transform converts expressions like {@code res = a + b + c} into a sequence of
 * assignments and updates: {@code res = a; res += b; res += c}.
 * <p>
 * Key behaviors:
 * <ul>
 *   <li>Preserves AST precedence and associativity</li>
 *   <li>Does not perform constant folding (relies on {@link BinaryOperator}'s constant folding)</li>
 *   <li>Creates temporary variables only when necessary for non-atomic values</li>
 *   <li>Avoids clobbering the target variable when the RHS reads it</li>
 *   <li>Recursively lowers nested binary operators to minimize temporary variables</li>
 * </ul>
 */
public class OperatorLowering implements ScopeVisitor {
    @Override
    public int visit(@NotNull Scope scope) {
        List<Node> original = scope.statements();
        List<Node> lowered = new ArrayList<>();
        int transforms = 0;

        for (Node node : original) {
            if (node instanceof LocalAssign) {
                boolean changed = lowerAssign((LocalAssign) node, lowered);
                if (changed)
                    transforms++;
            } else if (node instanceof LocalDeclareAssign) {
                boolean changed = lowerDeclareAssign((LocalDeclareAssign) node, lowered);
                if (changed)
                    transforms++;
            } else if (node instanceof ReturnValue) {
                boolean changed = lowerReturnValue((ReturnValue) node, lowered);
                if (changed)
                    transforms++;
            } else
                lowered.add(node);
        }

        // replace scope statements with lowered ones
        scope.statements(lowered);
        return transforms;
    }

    /**
     * Lowers a single assignment statement.
     *
     * @param assign the assignment to lower
     * @param out the list to append lowered statements to
     *
     * @return {@code true} if the assignment was transformed, {@code false} if left unchanged
     */
    private boolean lowerAssign(@NotNull LocalAssign assign, @NotNull List<Node> out) {
        return lowerAssignment(unwrap(assign.value()), assign.variable(), assign, out);
    }

    /**
     * Lowers a declaration with assignment statement.
     *
     * @param assign the declaration with assignment to lower
     * @param out the list to append lowered statements to
     *
     * @return {@code true} if the assignment was transformed, {@code false} if left unchanged
     */
    private boolean lowerDeclareAssign(@NotNull LocalDeclareAssign assign, @NotNull List<Node> out) {
        return lowerAssignment(unwrap(assign.value()), assign, assign, out);
    }

    /**
     * Lowers the value returned by a {@link ReturnValue} node.
     * <p>
     * Any complex expression in the return statement is evaluated into temporary
     * variables before the {@code return} executes, leaving the {@link ReturnValue}
     * itself holding only an atomic value.
     *
     * @param returnValue the return statement to lower
     * @param out the list to append lowered statements to
     *
     * @return {@code true} if the return statement was transformed, {@code false} otherwise
     */
    private boolean lowerReturnValue(@NotNull ReturnValue returnValue, @NotNull List<Node> out) {
        Value value = unwrap(returnValue.value());
        returnValue.value(value);

        // nothing to do for atomic values
        if (isAtomic(value)) {
            out.add(returnValue);
            return false;
        }

        // compute the value into temporaries and replace the return expression with a simple load
        Value simple = ensureSimple(value, Namespace.PLAYER, out);
        returnValue.value(simple);
        out.add(returnValue);
        return true;
    }

    /**
     * Unified lowering logic for assignment statements.
     * <p>
     * This method handles lowering for any assignment-like node by working with the value
     * and target variable directly. The original node is passed to preserve type information
     * for creating the appropriate assignment node type.
     *
     * @param rhs the right-hand side value to lower
     * @param target the target variable for the assignment
     * @param originalNode the original assignment node (for type preservation)
     * @param out the list to append lowered statements to
     *
     * @return {@code true} if the assignment was transformed, {@code false} if left unchanged
     */
    private boolean lowerAssignment(
        @NotNull Value rhs, @NotNull Variable target, @NotNull Node originalNode, @NotNull List<Node> out
    ) {
        rhs = unwrap(rhs);
        // if RHS is already atomic (constant or variable access), no lowering needed
        if (isAtomic(rhs)) {
            out.add(originalNode);
            return false;
        }

        // if RHS is a binary operator, use specialized lowering strategy
        if (rhs instanceof BinaryOperator) {
            lowerBinaryTop(target, (BinaryOperator) rhs, out);
            return true;
        }

        // for other complex values, compute into a temporary variable first
        Value simple = ensureSimple(rhs, target.namespace(), out);
        out.add(makeInitialAssign(target, originalNode, simple));
        return true;
    }

    /**
     * Lowers a binary operator at the top level of an assignment.
     * <p>
     * Handles both simple cases (both operands are atomic) and complex cases
     * (nested binary operators) by recursively lowering into the target variable.
     * <p>
     * If the RHS reads the target variable, the entire expression is computed into
     * a temporary variable first to avoid clobbering the target.
     *
     * @param target the target variable for the assignment
     * @param bin the binary operator to lower
     * @param out the list to append lowered statements to
     */
    private void lowerBinaryTop(@NotNull Variable target, @NotNull BinaryOperator bin, @NotNull List<Node> out) {
        Value left = unwrap(bin.lhs());
        Value right = unwrap(bin.rhs());
        bin.lhs(left);
        bin.rhs(right);
        Operator op = bin.operator();

        // check if the RHS reads the target variable - if so, we need to use a temp
        boolean rhsReadsTarget = readsVariable(bin, target);

        // fast path: both operands are atomic and don't read the target
        // this allows direct assignment without temporary variables
        if (
            isAtomic(left) && isAtomic(right) && !rhsReadsTarget && !readsVariable(left, target)
        ) {
            out.add(makeInitialAssign(target, null, left));
            out.add(makeUpdate(target, mapOperator(op), right));
            return;
        }

        // if RHS reads the target, compute entire expression into a temp first
        if (rhsReadsTarget) {
            Variable tmp = newTempVar(bin.getValueType(), target.namespace());
            lowerBinaryIntoTemp(tmp, bin, out);
            out.add(makeInitialAssign(target, null, makeLoad(tmp)));
            return;
        }

        // general case: handle nested binary operators recursively
        if (left instanceof BinaryOperator) {
            // recursively lower nested binary operators into the target to avoid redundant temps
            lowerBinaryIntoTemp(target, (BinaryOperator) left, out);
        } else {
            // evaluate left operand into a simple form (atomic or temporary variable)
            Value leftSimple = ensureSimple(left, target.namespace(), out);
            out.add(makeInitialAssign(target, null, leftSimple));
        }

        // evaluate right operand and update target
        Value rightSimple = ensureSimple(right, target.namespace(), out);
        out.add(makeUpdate(target, mapOperator(op), rightSimple));
    }

    /**
     * Ensure a value is "simple".
     * <p>
     * Creates load operations for complex cases but preserves stat access and constants.
     *
     * @param value the value to transform
     * @param namespace the namespace to use for temporary variables (should match the target variable's namespace)
     * @param out the list to append lowered statements to
     *
     * @return the transformed "simple" value
     */
    private @NotNull Value ensureSimple(
        @NotNull Value value, @NotNull Namespace namespace, @NotNull List<Node> out
    ) {
        value = unwrap(value);

        // treat StatAccess, ConstantAccess, and constant as atomic/simple
        if (isAtomic(value))
            return value;

        // if it's a binary op, compute subtree into a single temp
        if (value instanceof BinaryOperator) {
            Variable tmp = newTempVar(value.getValueType(), namespace);
            lowerBinaryIntoTemp(tmp, (BinaryOperator) value, out);
            return makeLoad(tmp);
        }

        // fallback: other complex values (calls, indexing, etc.) -> compute into temp
        Variable tmp = newTempVar(value.getValueType(), namespace);
        out.add(makeInitialAssign(tmp, null, value));
        return makeLoad(tmp);
    }

    /**
     * Lower a nested binary operator into a temporary variable.
     * <p>
     * Generates code of the form:
     * <pre>
     *   tmp = left_simple
     *   tmp op= right_simple
     * </pre>
     * <p>
     * Recursively handles nested binary operators to avoid creating redundant
     * temporary variables. For example, {@code (a + b) + c} is lowered directly
     * into the target temp without creating intermediate temps.
     *
     * @param temp the temporary variable to lower into
     * @param bin the binary operator to lower
     * @param out the list to append lowered statements to
     */
    private void lowerBinaryIntoTemp(Variable temp, BinaryOperator bin, List<Node> out) {
        Value left = unwrap(bin.lhs());
        Value right = unwrap(bin.rhs());
        bin.lhs(left);
        bin.rhs(right);
        Operator op = bin.operator();

        // handle nested binary operators recursively to avoid redundant temps
        if (left instanceof BinaryOperator)
            lowerBinaryIntoTemp(temp, (BinaryOperator) left, out);
        else {
            // evaluate left operand and assign to temp
            Value leftSimple = ensureSimple(left, temp.namespace(), out);
            out.add(makeInitialAssign(temp, null, leftSimple));
        }

        // evaluate right operand and update temp
        Value rightSimple = ensureSimple(right, temp.namespace(), out);
        out.add(makeUpdate(temp, mapOperator(op), rightSimple));
    }

    /**
     * Check if a value is atomic (can be used directly without temporary variables).
     * <p>
     * Atomic values include:
     * <ul>
     *   <li>Compile-time constants</li>
     *   <li>Variable accesses ({@code StatAccess})</li>
     *   <li>Variable/constant accesses ({@code ConstantAccess})</li>
     * </ul>
     *
     * @param value the value to check
     * @return {@code true} if the value is atomic, {@code false} otherwise
     */
    private boolean isAtomic(@NotNull Value value) {
        Value unwrapped = unwrap(value);
        return unwrapped.isConstant() || unwrapped instanceof StatAccess || unwrapped instanceof ConstantAccess;
    }

    /**
     * Recursively checks whether a value reads a variable with the same name as the target.
     * <p>
     * This prevents clobbering the target variable while it is still being read in the right-hand side of an
     * assignment.
     *
     * @param value the value to check
     * @param target the target variable to check against
     *
     * @return {@code true} if the value reads the target variable, {@code false} otherwise
     */
    private boolean readsVariable(@NotNull Value value, @NotNull Variable target) {
        value = unwrap(value);
        if (value instanceof StatAccess) {
            StatAccess sa = (StatAccess) value;
            return sa.variable().name().equals(target.name());
        }

        if (value instanceof ConstantAccess) {
            ConstantAccess ca = (ConstantAccess) value;
            return ca.name().value().equals(target.name());
        }

        if (value instanceof BinaryOperator) {
            BinaryOperator b = (BinaryOperator) value;
            return readsVariable(b.lhs(), target) || readsVariable(b.rhs(), target);
        }

        // other value types (function calls, indexing, etc.) could contain reads
        // add cases here as needed
        return false;
    }

    /**
     * Creates an assignment update operation (e.g., {@code +=}, {@code -=}).
     */
    private @NotNull AssignmentOperator makeUpdate(
        @NotNull Variable target, @NotNull Operator op, @NotNull Value value
    ) {
        AssignmentOperator update = new AssignmentOperator(
            Token.of(TokenType.IDENTIFIER, target.name()),
            op,
            value
        );
        update.variable(target);
        return update;
    }

    /**
     * Creates the initial assignment node for a target variable.
     * <p>
     * Uses the original node type to determine what kind of assignment to create.
     * If the original node is a {@code LocalDeclareAssign}, creates a new declaration with assignment.
     * Otherwise, creates a regular {@code LocalAssign}.
     * <p>
     * Falls back to checking the target type if originalNode is null (e.g., for temporary variables).
     *
     * @param target the target variable for the assignment
     * @param originalNode the original assignment node (used to determine the node type to create)
     * @param value the value to assign
     *
     * @return the appropriate assignment node
     */
    private @NotNull Node makeInitialAssign(
        @NotNull Variable target, @Nullable Node originalNode, @NotNull Value value
    ) {
        value = unwrap(value);
        // use originalNode to determine the type of assignment to create
        if (originalNode instanceof LocalDeclareAssign) {
            LocalDeclareAssign original = (LocalDeclareAssign) originalNode;
            return new LocalDeclareAssign(
                original.namespace(),
                Token.of(TokenType.IDENTIFIER, original.name()),
                original.alias(),
                original.team(),
                original.explicitType(),
                value
            );
        }

        // fallback: if originalNode is null or not a LocalDeclareAssign, check target type
        // (this handles cases like temporary variables where we don't have an original node)
        if (target instanceof LocalDeclareAssign) {
            LocalDeclareAssign declareAssign = (LocalDeclareAssign) target;
            return new LocalDeclareAssign(
                declareAssign.namespace(),
                Token.of(TokenType.IDENTIFIER, declareAssign.name()),
                declareAssign.alias(),
                declareAssign.team(),
                declareAssign.explicitType(),
                value
            );
        }

        // otherwise, create a regular assignment
        LocalAssign assign = new LocalAssign(
            Token.of(TokenType.IDENTIFIER, target.name()),
            Token.of(TokenType.OPERATOR, "="),
            value
        );
        assign.variable(target);
        return assign;
    }

    /**
     * Creates a variable load expression.
     * <p>
     * The returned {@code StatAccess} is considered atomic and can be used
     * directly in assignments without creating additional temporary variables.
     */
    private @NotNull StatAccess makeLoad(@NotNull Variable variable) {
        return new StatAccess(Token.of(TokenType.IDENTIFIER, variable.name()), variable);
    }

    /**
     * Creates a new temporary variable with the given type and namespace.
     *
     * @param type the type of the temporary variable
     * @param namespace the namespace for the temporary variable (should match the target variable's namespace)
     * @return a new temporary variable
     */
    private @NotNull Variable newTempVar(@NotNull Type type, @NotNull Namespace namespace) {
        return new TempVar(type, namespace);
    }

    /**
     * Maps a binary operator to its corresponding assignment operator.
     * <p>
     * For example, {@code +} maps to {@code +=}, {@code -} maps to {@code -=}, etc.
     *
     * @param source the binary operator to map
     * @return the corresponding assignment operator
     *
     * @throws IllegalArgumentException if the operator cannot be mapped
     */
    private @NotNull Operator mapOperator(@NotNull Operator source) {
        switch (source) {
            case ADD:
                return Operator.ADD_EQUAL;
            case NEGATE_OR_SUBTRACT:
                return Operator.SUBTRACT_EQUAL;
            case MULTIPLY:
                return Operator.MULTIPLY_EQUAL;
            case DIVIDE:
                return Operator.DIVIDE_EQUAL;
            default:
                throw new IllegalArgumentException("Cannot map operator: " + source);
        }
    }

    /**
     * Recursively unwrap {@link Group} nodes to access their underlying value.
     *
     * @param value the value to unwrap
     * @return the innermost non-group value
     */
    private @NotNull Value unwrap(@NotNull Value value) {
        Value current = value;
        while (current instanceof Group)
            current = current.load();
        return current;
    }

    /**
     * Represents a generated temporary variable implementation for use during operator lowering.
     * <p>
     * Temporary variables are automatically numbered and prefixed with {@code $temp:} to avoid conflicts with
     * user-defined variables.
     */
    @RequiredArgsConstructor
    private static class TempVar implements Variable {
        private static int counter = 0;
        private final int id = counter++;
        private final Type type;
        private final Namespace namespace;

        @Override
        public @NotNull Namespace namespace() {
            return namespace;
        }

        @Override
        public @NotNull String name() {
            return "$temp:" + id;
        }

        @Override
        public @NotNull Type type() {
            return type;
        }
    }
}
