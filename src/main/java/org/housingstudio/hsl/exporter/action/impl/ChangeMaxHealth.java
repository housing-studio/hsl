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
import org.housingstudio.hsl.importer.interaction.defaults.DefaultInt;
import org.housingstudio.hsl.importer.interaction.defaults.DefaultMode;
import org.housingstudio.hsl.std.Mode;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class ChangeMaxHealth implements Action {
    private final ActionType type = ActionType.CHANGE_MAX_HEALTH;

    @InteractionTarget(type = InteractionType.ANVIL, offset = 0)
    @DefaultInt(20)
    @SerializedName("max-health")
    private int maxHealth;

    @InteractionTarget(type = InteractionType.MODE, offset = 1)
    @DefaultMode(Mode.SET)
    private Mode mode;

    @InteractionTarget(type = InteractionType.TOGGLE, offset = 2)
    @DefaultBoolean(true)
    @SerializedName("heal-on-change")
    private boolean healOnChange;
}
