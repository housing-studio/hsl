package org.housingstudio.hsl.compiler.ast.impl.operator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.ast.impl.scope.Statement;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.codegen.builder.ActionBuilder;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.action.impl.ChangeVariable;
import org.housingstudio.hsl.compiler.debug.Printable;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.std.Mode;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.ASSIGNMENT_OPERATOR)
public class AssignmentOperator extends Statement implements Printable, ActionBuilder {
    private final @NotNull Token lhs;
    private final @NotNull Operator operator;

    @Children
    private final @NotNull Value rhs;

    private Variable variable;

    /**
     * Initialize node logic before the nodes are visited and the code is generated.
     */
    @Override
    public void init() {
        variable = resolveName(lhs.value());
        if (variable == null) {
            context.errorPrinter().print(
                Notification.error(Errno.UNKNOWN_VARIABLE, "unknown variable " + lhs.value(), this)
                    .error("cannot find variable in this scope", lhs)
                    .note("did you misspell the name, or forgot to declare the variable?")
            );
            throw new UnsupportedOperationException("Cannot find variable: " + lhs.value());
        }
    }

    @Override
    public @NotNull Action buildAction() {
        Mode mode;
        if (operator == Operator.ADD_EQUAL)
            mode = Mode.INCREMENT;
        else if (operator == Operator.SUBTRACT_EQUAL)
            mode = Mode.DECREMENT;
        else if (operator == Operator.MULTIPLY_EQUAL)
            mode = Mode.MULTIPLY;
        else if (operator == Operator.DIVIDE_EQUAL)
            mode = Mode.DIVIDE;
        else
            throw new UnsupportedOperationException("Unexpected assignment operator: " + operator);

        if (!rhs.isConstant()) {
            context.errorPrinter().print(
                Notification.error(Errno.EXPECTED_CONSTANT_VALUE, "cannot assign to non-constant value", this)
                    .error(
                        "cannot assign to this stat, because the assigned value may not be known at compile-time",
                        lhs // TODO use rhs.tokens()
                    )
                    .note("consider assigning a constant value", "foo = 1234")
            );
            throw new UnsupportedOperationException("Cannot assign to non-constant value: " + lhs.value());
        }

        return new ChangeVariable(
            variable.namespace(), variable.name(), mode, rhs.asConstantValue(), false
        );
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return lhs.print() + operator.value() + rhs.print();
    }
}
