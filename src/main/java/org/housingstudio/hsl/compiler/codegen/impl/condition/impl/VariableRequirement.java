package org.housingstudio.hsl.compiler.codegen.impl.condition.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.codegen.impl.condition.Condition;
import org.housingstudio.hsl.compiler.codegen.impl.condition.ConditionType;
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
}
