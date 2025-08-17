package org.housingstudio.hsl.exporter.condition.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.condition.Condition;
import org.housingstudio.hsl.exporter.condition.ConditionType;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class RequiredTeam implements Condition {
    private final ConditionType type = ConditionType.REQUIRED_TEAM;

    private boolean inverted;

    @SerializedName("required-team")
    private @NotNull String requiredTeam;
}
