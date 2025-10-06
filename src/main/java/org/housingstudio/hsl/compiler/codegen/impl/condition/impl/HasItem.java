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
import org.housingstudio.hsl.compiler.codegen.impl.htsl.HtslInvocation;
import org.housingstudio.hsl.std.ComparatorAmount;
import org.housingstudio.hsl.std.ComparatorTarget;
import org.housingstudio.hsl.std.ItemComparator;
import org.housingstudio.hsl.std.Material;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class HasItem implements Condition {
    private final ConditionType type = ConditionType.HAS_ITEM;

    private boolean inverted;

    private @NotNull Material material;

    @SerializedName("what-to-check")
    private @NotNull ItemComparator whatToCheck;

    @SerializedName("where-to-check")
    private @NotNull ComparatorTarget whereToCheck;

    @SerializedName("required-amount")
    private @NotNull ComparatorAmount requiredAmount;

    /**
     * Retrieve the HTSL representation of this housing condition.
     *
     * @return the htsl code that represents this condition
     */
    @Override
    public @NotNull HtslInvocation asHTSL() {
        return HTSL.Condition.HAS_ITEM.invoke()
            .set("item", material)
            .set("what_to_check", whatToCheck)
            .set("where_to_check", whereToCheck)
            .set("required_amount", requiredAmount);
    }
}
