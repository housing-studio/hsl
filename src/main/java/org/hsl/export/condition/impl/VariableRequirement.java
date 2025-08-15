package org.hsl.export.condition.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.export.condition.Condition;
import org.hsl.export.condition.ConditionType;
import org.hsl.std.type.Comparator;
import org.hsl.std.type.Namespace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
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
