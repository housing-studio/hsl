package org.housingstudio.hsl.exporter.condition.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.condition.Condition;
import org.housingstudio.hsl.exporter.condition.ConditionType;
import org.housingstudio.hsl.type.Comparator;
import org.housingstudio.hsl.type.Namespace;
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
