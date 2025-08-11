package org.hsl.export.action.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.export.action.Action;
import org.hsl.export.action.ActionType;
import org.hsl.std.type.Effect;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class ApplyPotionEffect implements Action {
    private final ActionType type = ActionType.APPLY_POTION_EFFECT;

    private @NotNull Effect effect;
    private int duration;
    private int level;

    @SerializedName("override-existing-effects")
    private boolean overrideExistingEffects;

    @SerializedName("show-potion-icon")
    private boolean showPotionIcon;
}
