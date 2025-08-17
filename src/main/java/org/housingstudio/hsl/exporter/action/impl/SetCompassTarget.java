package org.housingstudio.hsl.exporter.action.impl;

import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.action.ActionType;
import org.housingstudio.hsl.exporter.adapter.LocationAdapter;
import org.housingstudio.hsl.type.location.Location;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class SetCompassTarget implements Action {
    private final ActionType type = ActionType.SET_COMPASS_TARGET;

    @JsonAdapter(LocationAdapter.class)
    private @Nullable Location location;
}
