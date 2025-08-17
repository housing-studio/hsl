package org.housingstudio.hsl.exporter.action.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.action.ActionType;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class PauseExecution implements Action {
    private final ActionType type = ActionType.PAUSE_EXECUTION;

    @SerializedName("ticks-to-wait")
    private int ticksToWait;
}
