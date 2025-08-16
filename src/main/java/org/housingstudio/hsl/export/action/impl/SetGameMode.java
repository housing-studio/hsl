package org.housingstudio.hsl.export.action.impl;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.export.action.Action;
import org.housingstudio.hsl.export.action.ActionType;
import org.housingstudio.hsl.type.GameMode;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class SetGameMode implements Action {
    private final ActionType type = ActionType.PLAY_SOUND;

    @SerializedName("game-mode")
    private @NotNull GameMode gameMode;
}
