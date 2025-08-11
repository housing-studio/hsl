package org.hsl.export.action.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.export.action.Action;
import org.hsl.export.action.ActionType;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class ToggleNameTagDisplay implements Action {
    private final ActionType type = ActionType.TOGGLE_NAME_TAG_DISPLAY;

    @SerializedName("display-name-tag")
    private boolean displayNameTag;
}
