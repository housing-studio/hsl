package org.hsl.export.action.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.export.action.Action;
import org.hsl.export.action.ActionType;
import org.hsl.std.type.Lobby;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class SendToLobby implements Action {
    private final ActionType type = ActionType.SEND_TO_LOBBY;

    private @NotNull Lobby lobby;
}
