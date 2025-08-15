package org.hsl.export.action.impl;

import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.export.action.Action;
import org.hsl.export.action.ActionType;
import org.hsl.export.adapter.LocationAdapter;
import org.hsl.std.type.location.Location;
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
