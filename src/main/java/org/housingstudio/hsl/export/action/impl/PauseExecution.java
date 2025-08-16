package org.housingstudio.hsl.export.action.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.export.action.Action;
import org.housingstudio.hsl.export.action.ActionType;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class PauseExecution implements Action {
    private final ActionType type = ActionType.PAUSE_EXECUTION;

    @SerializedName("ticks-to-wait")
    private int ticksToWait;
}
