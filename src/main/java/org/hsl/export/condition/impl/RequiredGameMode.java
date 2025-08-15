package org.hsl.export.condition.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.hsl.export.condition.Condition;
import org.hsl.export.condition.ConditionType;
import org.hsl.std.type.GameMode;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class RequiredGameMode implements Condition {
    private final ConditionType type = ConditionType.REQUIRED_GAME_MODE;

    private boolean inverted;

    @SerializedName("required-game-mode")
    private @NotNull GameMode requiredGameMode;
}
