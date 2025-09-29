package org.housingstudio.hsl.compiler.ast.impl.conditional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.ast.impl.operator.Operator;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.codegen.builder.ConditionBuilder;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.codegen.impl.condition.Condition;
import org.housingstudio.hsl.compiler.codegen.impl.condition.ConditionType;
import org.housingstudio.hsl.compiler.codegen.impl.condition.impl.VariableRequirement;
import org.housingstudio.hsl.compiler.error.Errno;
import org.housingstudio.hsl.compiler.error.Notification;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.std.Comparator;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.COMPARATOR)
public class StatComparator extends Node implements ConditionBuilder, Condition {
    private final @NotNull Token lhs;
    private final @NotNull Operator comparator;

    @Children
    private final @NotNull Value rhs;

    private boolean inverted;

    @Override
    public @NotNull Condition buildCondition() {
        Variable variable = resolveName(lhs.value());
        if (variable == null) {
            context.errorPrinter().print(
                Notification.error(Errno.UNKNOWN_VARIABLE, "unknown variable", this)
                    .error("cannot find variable in this scope", lhs)
                    .note("did you misspell the name, or forgot to declare the variable?")
            );
            throw new UnsupportedOperationException("Cannot find variable: " + lhs.value());
        }

        Comparator comparator;
        boolean inverted = this.inverted;
        switch (this.comparator) {
            case LESS_THAN:
                comparator = Comparator.LESS_THAN;
                break;
            case LESS_OR_EQUAL:
                comparator = Comparator.LESS_THAN_OR_EQUAL;
                break;
            case GREATER_THAN:
                comparator = Comparator.GREATER_THAN;
                break;
            case GREATER_OR_EQUAL:
                comparator = Comparator.GREATER_THAN_OR_EQUAL;
                break;
            case EQUAL:
                comparator = Comparator.EQUAL;
                break;
            case NOT_EQUAL:
                comparator = Comparator.EQUAL;
                inverted = !inverted;
                break;
            default:
                throw new IllegalStateException("Unexpected comparator operator: " + this.comparator);
        }

        return new VariableRequirement(
            inverted,
            variable.namespace(),
            variable.name(),
            comparator,
            rhs.asConstantValue(),
            null
        );
    }

    @Override
    public void invert() {
        inverted = !inverted;
    }

    @Override
    public @NotNull ConditionType type() {
        return ConditionType.VARIABLE_REQUIREMENT;
    }
}
