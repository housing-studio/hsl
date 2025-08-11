package org.hsl.export.action.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.export.action.Action;
import org.hsl.export.action.ActionType;
import org.hsl.std.type.location.Location;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class TeleportPlayer implements Action {
    private final ActionType type = ActionType.TELEPORT_PLAYER;

    private @NotNull Location location;

    @SerializedName("prevent-teleport-inside-blocks")
    private boolean preventTeleportInsideBlocks;
}
