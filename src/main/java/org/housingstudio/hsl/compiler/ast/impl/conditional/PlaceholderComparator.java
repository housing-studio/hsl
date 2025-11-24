package org.housingstudio.hsl.compiler.ast.impl.conditional;

import lombok.RequiredArgsConstructor;
import org.housingstudio.hsl.compiler.ast.Node;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.operator.Operator;
import org.housingstudio.hsl.compiler.ast.impl.placeholder.PlaceholderValue;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.codegen.builder.ConditionBuilder;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.codegen.impl.condition.Condition;
import org.housingstudio.hsl.compiler.codegen.impl.condition.ConditionType;
import org.housingstudio.hsl.compiler.codegen.impl.condition.impl.PlaceholderRequirement;
import org.housingstudio.hsl.compiler.codegen.interop.htsl.HtslInvocation;
import org.housingstudio.hsl.std.Comparator;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@NodeInfo(type = NodeType.COMPARATOR)
public class PlaceholderComparator extends Node implements ConditionBuilder, Condition {
    private final @NotNull PlaceholderValue lhs;
    private final @NotNull Operator comparator;

    @Children
    private final @NotNull Value rhs;
    private boolean inverted;

    @Override
    public @NotNull Condition buildCondition() {
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

        return new PlaceholderRequirement(inverted, lhs.asConstantValue(), comparator, rhs.asConstantValue());
    }

    @Override
    public void invert() {
        inverted = !inverted;
    }

    @Override
    public @NotNull ConditionType type() {
        return ConditionType.PLACEHOLDER_REQUIREMENT;
    }

    @Override
    public boolean inverted() {
        return inverted;
    }

    /**
     * Retrieve the HTSL representation of this housing condition.
     *
     * @return the htsl code that represents this condition
     */
    @Override
    public @NotNull HtslInvocation asHTSL() {
        return buildCondition().asHTSL();
    }
}
