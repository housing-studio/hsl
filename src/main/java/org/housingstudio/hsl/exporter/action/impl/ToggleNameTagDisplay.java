package org.housingstudio.hsl.exporter.action.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.action.ActionType;
import org.housingstudio.hsl.importer.interaction.InteractionTarget;
import org.housingstudio.hsl.importer.interaction.InteractionType;
import org.housingstudio.hsl.importer.interaction.defaults.DefaultBoolean;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class ToggleNameTagDisplay implements Action {
    private final ActionType type = ActionType.TOGGLE_NAME_TAG_DISPLAY;

    @InteractionTarget(type = InteractionType.TOGGLE, offset = 0)
    @DefaultBoolean(true)
    @SerializedName("display-name-tag")
    private boolean displayNameTag;
}
