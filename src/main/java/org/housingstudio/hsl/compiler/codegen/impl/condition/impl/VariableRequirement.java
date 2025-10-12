package org.housingstudio.hsl.compiler.codegen.impl.condition.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.codegen.impl.condition.Condition;
import org.housingstudio.hsl.compiler.codegen.impl.condition.ConditionType;
import org.housingstudio.hsl.compiler.codegen.impl.htsl.HTSL;
import org.housingstudio.hsl.compiler.codegen.impl.htsl.HtslCommand;
import org.housingstudio.hsl.compiler.codegen.impl.htsl.HtslInvocation;
import org.housingstudio.hsl.std.Comparator;
import org.housingstudio.hsl.std.Namespace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class VariableRequirement implements Condition {
    private final ConditionType type = ConditionType.VARIABLE_REQUIREMENT;

    private boolean inverted;

    private @NotNull Namespace namespace;
    private @NotNull String variable;

    private @NotNull Comparator comparator;

    @SerializedName("compare-value")
    private @NotNull String value;

    @SerializedName("fallback-value")
    private @Nullable String fallback;

    /**
     * Retrieve the HTSL representation of this housing condition.
     *
     * @return the htsl code that represents this condition
     */
    @Override
    public @NotNull HtslInvocation asHTSL() {
        HtslCommand base;
        if (fallback != null && !fallback.isEmpty()) {
            switch (namespace) {
                case GLOBAL:
                    base = HTSL.Condition.GLOBAL_VAR;
                    break;
                case TEAM:
                    base = HTSL.Condition.TEAM_VAR;
                    break;
                case PLAYER:
                    base = HTSL.Condition.VAR;
                    break;
                default:
                    throw new IllegalStateException("Unexpected namespace: " + namespace);
            }
            return base.invoke()
                .set("variable", variable)
                .setComparator("comparator", comparator)
                .set("compare_value", value)
                .set("fallback_value", fallback);
        } else {
            switch (namespace) {
                case GLOBAL:
                    base = HTSL.Condition.GLOBAL_STAT;
                    break;
                case TEAM:
                    base = HTSL.Condition.TEAM_STAT;
                    break;
                case PLAYER:
                    base = HTSL.Condition.STAT;
                    break;
                default:
                    throw new IllegalStateException("Unexpected namespace: " + namespace);
            }
            return base.invoke()
                .set("variable", variable)
                .setComparator("comparator", comparator)
                .set("compare_value", value);
        }
    }
}
