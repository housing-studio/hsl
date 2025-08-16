package org.housingstudio.hsl.export.action.impl;

import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.export.action.Action;
import org.housingstudio.hsl.export.action.ActionType;
import org.housingstudio.hsl.export.adapter.LocationAdapter;
import org.housingstudio.hsl.type.location.Location;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class SetCompassTarget implements Action {
    private final ActionType type = ActionType.SET_COMPASS_TARGET;

    @JsonAdapter(LocationAdapter.class)
    private @Nullable Location location;
}
