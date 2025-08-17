package org.housingstudio.hsl.exporter.action.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.action.ActionType;
import org.housingstudio.hsl.type.Lobby;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class SendToLobby implements Action {
    private final ActionType type = ActionType.SEND_TO_LOBBY;

    private @NotNull Lobby lobby;
}
