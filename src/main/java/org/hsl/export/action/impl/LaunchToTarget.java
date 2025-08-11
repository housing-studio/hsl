package org.hsl.export.action.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.export.action.Action;
import org.hsl.export.action.ActionType;
import org.hsl.std.type.location.Location;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class LaunchToTarget implements Action {
    private final ActionType type = ActionType.LAUNCH_TO_TARGET;

    @SerializedName("target-location")
    private @Nullable Location location;

    @SerializedName("launch-strength")
    private int strength;
}
