package org.housingstudio.hsl.compiler.codegen.impl.condition.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.codegen.impl.condition.Condition;
import org.housingstudio.hsl.compiler.codegen.impl.condition.ConditionType;
import org.housingstudio.hsl.compiler.codegen.impl.htsl.HTSL;
import org.housingstudio.hsl.compiler.codegen.impl.htsl.HtslInvocation;
import org.housingstudio.hsl.std.Comparator;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class PlayerHunger implements Condition {
    private final ConditionType type = ConditionType.PLAYER_HUNGER;

    private boolean inverted;

    private @NotNull Comparator comparator;

    @SerializedName("compare-value")
    private @NotNull String compareValue;

    /**
     * Retrieve the HTSL representation of this housing condition.
     *
     * @return the htsl code that represents this condition
     */
    @Override
    public @NotNull HtslInvocation asHTSL() {
        return HTSL.Condition.HUNGER.invoke()
            .setComparator("comparator", comparator)
            .set("compare_value", compareValue);
    }
}
