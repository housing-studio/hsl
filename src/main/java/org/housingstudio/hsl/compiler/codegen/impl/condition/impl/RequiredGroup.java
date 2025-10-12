package org.housingstudio.hsl.compiler.codegen.impl.condition.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.codegen.impl.condition.Condition;
import org.housingstudio.hsl.compiler.codegen.impl.condition.ConditionType;
import org.housingstudio.hsl.compiler.codegen.interop.htsl.HTSL;
import org.housingstudio.hsl.compiler.codegen.interop.htsl.HtslInvocation;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class RequiredGroup implements Condition {
    private final ConditionType type = ConditionType.REQUIRED_GROUP;

    private boolean inverted;

    @SerializedName("required-group")
    private @NotNull String requiredGroup;

    @SerializedName("require-higher-groups")
    private boolean requireHigherGroups;

    /**
     * Retrieve the HTSL representation of this housing condition.
     *
     * @return the htsl code that represents this condition
     */
    @Override
    public @NotNull HtslInvocation asHTSL() {
        return HTSL.Condition.HAS_GROUP.invoke()
            .setString("required_group", requiredGroup)
            .set("include_higher_groups", requireHigherGroups);
    }
}
