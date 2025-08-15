package org.hsl.export.condition.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.hsl.export.condition.Condition;
import org.hsl.export.condition.ConditionType;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class RequiredTeam extends Condition {
    private final ConditionType type = ConditionType.REQUIRED_TEAM;

    @SerializedName("required-team")
    private @NotNull String requiredTeam;
}
