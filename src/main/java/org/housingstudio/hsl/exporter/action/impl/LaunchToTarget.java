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
import org.housingstudio.hsl.importer.interaction.defaults.DefaultInt;
import org.housingstudio.hsl.importer.interaction.defaults.Required;
import org.housingstudio.hsl.std.location.Location;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class LaunchToTarget implements Action {
    private final ActionType type = ActionType.LAUNCH_TO_TARGET;

    @InteractionTarget(type = InteractionType.LOCATION, offset = 0)
    @Required
    @SerializedName("target-location")
    @JsonAdapter(LocationAdapter.class)
    private @Nullable Location location;

    @InteractionTarget(type = InteractionType.ANVIL, offset = 1)
    @DefaultInt(2)
    @SerializedName("launch-strength")
    private int strength;
}
