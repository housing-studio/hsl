package org.hsl.export.action.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.export.action.Action;
import org.hsl.export.action.ActionType;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class DisplayTitle implements Action {
    private final ActionType type = ActionType.DISPLAY_TITLE;

    private @NotNull String title, subtitle;
    private int fadein, stay, fadeout;
}
