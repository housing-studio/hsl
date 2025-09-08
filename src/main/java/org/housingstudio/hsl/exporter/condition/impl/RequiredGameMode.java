package org.housingstudio.hsl.exporter.condition.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.condition.Condition;
import org.housingstudio.hsl.exporter.condition.ConditionType;
import org.housingstudio.hsl.std.GameMode;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class RequiredGameMode implements Condition {
    private final ConditionType type = ConditionType.REQUIRED_GAME_MODE;

    private boolean inverted;

    @SerializedName("required-game-mode")
    private @NotNull GameMode requiredGameMode;
}
