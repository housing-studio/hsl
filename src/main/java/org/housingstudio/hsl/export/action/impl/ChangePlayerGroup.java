package org.housingstudio.hsl.export.action.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.export.action.Action;
import org.housingstudio.hsl.export.action.ActionType;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class ChangePlayerGroup implements Action {
    private final ActionType type = ActionType.CHANGE_PLAYER_GROUP;

    private @NotNull String group;

    @SerializedName("demotion-protection")
    private boolean demotionProtection;
}
