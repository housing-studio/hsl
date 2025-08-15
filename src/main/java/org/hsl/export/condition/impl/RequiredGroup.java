package org.hsl.export.condition.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.export.condition.Condition;
import org.hsl.export.condition.ConditionType;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class RequiredGroup implements Condition {
    private final ConditionType type = ConditionType.REQUIRED_GROUP;

    private boolean inverted;

    @SerializedName("required-group")
    private @NotNull String requiredGroup;

    @SerializedName("require-higher-groups")
    private boolean requireHigherGroups;
}
