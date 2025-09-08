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
import org.housingstudio.hsl.importer.interaction.defaults.Required;
import org.housingstudio.hsl.std.Effect;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class ApplyPotionEffect implements Action {
    private final ActionType type = ActionType.APPLY_POTION_EFFECT;

    @InteractionTarget(type = InteractionType.EFFECT, offset = 0)
    @Required
    private @NotNull Effect effect;

    @InteractionTarget(type = InteractionType.CHAT, offset = 1)
    @DefaultInt(60)
    private int duration;

    @InteractionTarget(type = InteractionType.CHAT, offset = 2)
    @DefaultInt(1)
    private int level;

    @InteractionTarget(type = InteractionType.TOGGLE, offset = 3)
    @DefaultBoolean(false)
    @SerializedName("override-existing-effects")
    private boolean overrideExistingEffects;

    @InteractionTarget(type = InteractionType.TOGGLE, offset = 4)
    @DefaultBoolean(true)
    @SerializedName("show-potion-icon")
    private boolean showPotionIcon;
}
