package org.housingstudio.hsl.exporter.action.impl;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.action.ActionType;
import org.housingstudio.hsl.exporter.adapter.LocationAdapter;
import org.housingstudio.hsl.importer.interaction.InteractionTarget;
import org.housingstudio.hsl.importer.interaction.InteractionType;
import org.housingstudio.hsl.importer.interaction.defaults.DefaultBoolean;
import org.housingstudio.hsl.importer.interaction.defaults.Required;
import org.housingstudio.hsl.type.location.Location;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class TeleportPlayer implements Action {
    private final ActionType type = ActionType.TELEPORT_PLAYER;

    @JsonAdapter(LocationAdapter.class)
    @InteractionTarget(type = InteractionType.LOCATION, offset = 0)
    @Required
    private @NotNull Location location;

    @SerializedName("prevent-teleport-inside-blocks")
    @InteractionTarget(type = InteractionType.TOGGLE, offset = 1)
    @DefaultBoolean(false)
    private boolean preventTeleportInsideBlocks;
}
