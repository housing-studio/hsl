package org.hsl.export.condition.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.hsl.export.condition.Condition;
import org.hsl.export.condition.ConditionType;
import org.hsl.std.type.Comparator;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class PlayerHunger extends Condition {
    private final ConditionType type = ConditionType.PLAYER_HUNGER;

    private @NotNull Comparator comparator;

    @SerializedName("compare-value")
    private @NotNull String compareValue;
}
