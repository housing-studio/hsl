package org.housingstudio.hsl.compiler.transform.lowering;

import org.housingstudio.hsl.compiler.ast.Game;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.impl.action.BuiltinActions;
import org.housingstudio.hsl.compiler.ast.impl.control.StaticActionBuilder;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Method;
import org.housingstudio.hsl.compiler.ast.impl.local.LocalAssign;
import org.housingstudio.hsl.compiler.ast.impl.local.LocalDeclareAssign;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.ast.impl.scope.Scope;
import org.housingstudio.hsl.compiler.ast.impl.type.Types;
import org.housingstudio.hsl.compiler.ast.impl.value.MethodCall;
import org.housingstudio.hsl.compiler.ast.impl.value.StatAccess;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.housingstudio.hsl.compiler.transform.ScopeVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a method call expansion that builds the method call chain and replaces the call value with a load
 * operation to the return value.
 * <p>
 * This transform works by:
 * <ul>
 *   <li>Finding MethodCall nodes in statements</li>
 *   <li>Building the action list from the MethodCall (which creates function call instructions)</li>
 *   <li>Replacing the MethodCall value with a StatAccess to the method's reserved return variable</li>
 *   <li>Inserting the function call actions before the statement containing the MethodCall</li>
 * </ul>
 */
public class MethodCallExpansion implements ScopeVisitor {
    /**
     * Apply this strategy to a scope.
     *
     * @param scope the scope to transform
     * @return the number of transformations applied
     */
    @Override
    public int visit(@NotNull Scope scope) {
        List<Node> original = scope.statements();
        List<Node> modified = new ArrayList<>();
        int transforms = 0;

        for (Node node : original) {
            if (node instanceof LocalAssign) {
                boolean changed = expandLocalAssign((LocalAssign) node, modified);
                if (changed)
                    transforms++;
            } else if (node instanceof LocalDeclareAssign) {
                boolean changed = expandLocalDeclareAssign((LocalDeclareAssign) node, modified);
                if (changed)
                    transforms++;
            } else
                modified.add(node);
        }

        scope.statements(modified);
        return transforms;
    }

    /**
     * Expand method calls in a LocalAssign statement.
     *
     * @param assign the assignment to process
     * @param out the list to append transformed statements to
     *
     * @return {@code true} if the assignment was transformed, {@code false} otherwise
     */
    private boolean expandLocalAssign(@NotNull LocalAssign assign, @NotNull List<Node> out) {
        Value value = assign.value();
        List<Action> actions = new ArrayList<>();
        Value expanded = expandCallInstruction(value, actions);

        if (!actions.isEmpty() || expanded != value) {
            // create action nodes from the actions
            for (Action action : actions)
                out.add(new StaticActionBuilder(action));

            // create a new assignment with the expanded value
            LocalAssign newAssign = new LocalAssign(assign.name(), assign.operator(), expanded);
            newAssign.variable(assign.variable());
            out.add(newAssign);
            return true;
        }

        out.add(assign);
        return false;
    }

    /**
     * Expand method calls in a LocalDeclareAssign statement.
     *
     * @param assign the declaration with assignment to process
     * @param out the list to append transformed statements to
     *
     * @return {@code true} if the assignment was transformed, {@code false} otherwise
     */
    private boolean expandLocalDeclareAssign(@NotNull LocalDeclareAssign assign, @NotNull List<Node> out) {
        Value value = assign.value();
        List<Action> actions = new ArrayList<>();
        Value expanded = expandCallInstruction(value, actions);

        if (!actions.isEmpty()) {
            // create action nodes from the actions
            for (Action action : actions)
                out.add(new StaticActionBuilder(action));

            // update the assignment with the expanded value
            assign.value(expanded);
            out.add(assign);
            return true;
        }

        out.add(assign);
        return false;
    }

    /**
     * Recursively expand method calls in a value, collecting actions and replacing MethodCall nodes.
     *
     * @param value the value to process
     * @param actions the list to collect actions into
     *
     * @return the expanded value with MethodCall nodes replaced by StatAccess nodes
     */
    private @NotNull Value expandCallInstruction(@NotNull Value value, @NotNull List<Action> actions) {
        if (value instanceof MethodCall) {
            MethodCall methodCall = (MethodCall) value;
            Method method = resolveMethod(methodCall);

            // if we can't resolve the method, return the original value
            if (method == null)
                return value;

            // only expand if the return type is not void
            // TODO throw exception if void methods are used as expression
            if (method.returnType().matches(Types.VOID))
                return value;

            // get the reserved return variable
            Variable returnVar = method.returnVariable();
            if (returnVar == null)
                return value;

            // build the action list from the method call (creates function call instructions)
            List<Action> methodCallActions = methodCall.buildActionList();
            actions.addAll(methodCallActions);

            // create a load operation for the reserved return variable (similar to OperatorLowering#makeLoad)
            return makeLoad(returnVar);
        }

        // for other value types, recursively process children if they exist
        // this handles nested MethodCalls in expressions
        Value loaded = value.load();
        if (loaded != value)
            return expandCallInstruction(loaded, actions);

        return value;
    }

    /**
     * Resolve the Method being called by a MethodCall.
     *
     * @param methodCall the method call to resolve
     * @return the Method being called, or {@code null} if not found
     */
    private @Nullable Method resolveMethod(@NotNull MethodCall methodCall) {
        // try builtin actions first (same order as MethodCall.getValueType)
        // TODO throw an error here, as builtin actions cannot be used as expressions
        Method method = BuiltinActions.LOOKUP.get(methodCall.name().value());
        if (method != null)
            return method;

        // try to get the method from the game's functions by traversing parent chain
        Node node = methodCall;
        while (node != null) {
            if (node instanceof Game) {
                Game game = (Game) node;
                method = game.functions().get(methodCall.name().value());
                if (method != null)
                    return method;
                break;
            }
            node = node.parent();
        }

        return null;
    }

    /**
     * Creates a variable load expression.
     * <p>
     *
     * @param variable the variable to create a load for
     * @return a StatAccess node for loading the variable
     */
    private @NotNull StatAccess makeLoad(@NotNull Variable variable) {
        return new StatAccess(Token.of(TokenType.IDENTIFIER, variable.name()), variable);
    }
}
