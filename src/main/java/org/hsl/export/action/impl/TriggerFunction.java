package org.hsl.export.action.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.export.action.Action;
import org.hsl.export.action.ActionType;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class TriggerFunction implements Action {
    private final ActionType type = ActionType.TRIGGER_FUNCTION;

    private @NotNull String function;

    @SerializedName("trigger-for-all-players")
    private boolean triggerForAllPlayers;
}
