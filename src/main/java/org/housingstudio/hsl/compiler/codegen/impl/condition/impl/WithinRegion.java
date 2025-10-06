package org.housingstudio.hsl.compiler.codegen.impl.condition.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.codegen.impl.condition.Condition;
import org.housingstudio.hsl.compiler.codegen.impl.condition.ConditionType;
import org.housingstudio.hsl.compiler.codegen.impl.htsl.HTSL;
import org.housingstudio.hsl.compiler.codegen.impl.htsl.HtslInvocation;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class WithinRegion implements Condition {
    private final ConditionType type = ConditionType.WITHIN_REGION;

    private boolean inverted;

    private @NotNull String region;

    /**
     * Retrieve the HTSL representation of this housing condition.
     *
     * @return the htsl code that represents this condition
     */
    @Override
    public @NotNull HtslInvocation asHTSL() {
        return HTSL.Condition.IN_REGION.invoke()
            .set("region", region);
    }
}
