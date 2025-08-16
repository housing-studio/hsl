package org.housingstudio.hsl.export.condition.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.export.condition.Condition;
import org.housingstudio.hsl.export.condition.ConditionType;
import org.housingstudio.hsl.type.GameMode;
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
