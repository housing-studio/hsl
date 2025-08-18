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
import org.housingstudio.hsl.importer.interaction.defaults.Required;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class ChangePlayerGroup implements Action {
    private final ActionType type = ActionType.CHANGE_PLAYER_GROUP;

    @InteractionTarget(type = InteractionType.DYNAMIC_OPTION, offset = 0)
    @Required
    private @NotNull String group;

    @InteractionTarget(type = InteractionType.TOGGLE, offset = 1)
    @DefaultBoolean(true)
    @SerializedName("demotion-protection")
    private boolean demotionProtection;
}
