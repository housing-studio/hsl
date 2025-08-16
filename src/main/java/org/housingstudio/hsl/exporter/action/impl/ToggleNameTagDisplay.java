package org.housingstudio.hsl.exporter.action.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.action.ActionType;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class ToggleNameTagDisplay implements Action {
    private final ActionType type = ActionType.TOGGLE_NAME_TAG_DISPLAY;

    @SerializedName("display-name-tag")
    private boolean displayNameTag;
}
