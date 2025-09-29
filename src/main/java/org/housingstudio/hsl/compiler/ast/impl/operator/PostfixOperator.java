package org.housingstudio.hsl.compiler.ast.impl.operator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.ast.impl.scope.Statement;
import org.housingstudio.hsl.compiler.codegen.builder.ActionBuilder;
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
@NodeInfo(type = NodeType.POSTFIX_UNARY_OPERATOR)
public class PostfixOperator extends Statement implements Printable, ActionBuilder {
    private final @NotNull Token operand;
    private final @NotNull Operator operator;

    @Override
    public @NotNull Action buildAction() {
        Variable variable = resolveName(operand.value());
        if (variable == null) {
            context.errorPrinter().print(
                Notification.error(Errno.UNKNOWN_VARIABLE, "unknown variable", this)
                    .error("cannot find variable in this scope", operand)
                    .note("did you misspell the name, or forgot to declare the variable?")
            );
            throw new UnsupportedOperationException("Cannot find variable: " + operand.value());
        }

        Mode mode;
        if (operator == Operator.INCREMENT)
            mode = Mode.INCREMENT;
        else if (operator == Operator.DECREMENT)
            mode = Mode.DECREMENT;
        else
            throw new UnsupportedOperationException("Unexpected postfix operator: " + operator);

        return new ChangeVariable(
            variable.namespace(), variable.name(), mode, "1", false
        );
    }

    /**
     * Returns a string representation of the implementing class.
     *
     * @return the class debug information
     */
    @Override
    public @NotNull String print() {
        return operand.print() + operator.value();
    }
}
