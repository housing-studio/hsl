package org.housingstudio.hsl.exporter.action.impl;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.action.ActionType;
import org.housingstudio.hsl.exporter.adapter.LocationAdapter;
import org.housingstudio.hsl.type.location.Location;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class TeleportPlayer implements Action {
    private final ActionType type = ActionType.TELEPORT_PLAYER;

    @JsonAdapter(LocationAdapter.class)
    private @NotNull Location location;

    @SerializedName("prevent-teleport-inside-blocks")
    private boolean preventTeleportInsideBlocks;
}
