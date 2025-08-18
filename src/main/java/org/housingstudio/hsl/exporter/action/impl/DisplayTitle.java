package org.housingstudio.hsl.exporter.action.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.action.ActionType;
import org.housingstudio.hsl.importer.interaction.InteractionTarget;
import org.housingstudio.hsl.importer.interaction.InteractionType;
import org.housingstudio.hsl.importer.interaction.defaults.DefaultInt;
import org.housingstudio.hsl.importer.interaction.defaults.DefaultString;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class DisplayTitle implements Action {
    private final ActionType type = ActionType.DISPLAY_TITLE;

    @InteractionTarget(type = InteractionType.CHAT, offset = 0)
    @DefaultString("Hello World!")
    private @NotNull String title;

    @InteractionTarget(type = InteractionType.CHAT, offset = 1)
    @DefaultString("")
    private @NotNull String subtitle;

    @InteractionTarget(type = InteractionType.ANVIL, offset = 2)
    @DefaultInt(1)
    private int fadein;

    @InteractionTarget(type = InteractionType.ANVIL, offset = 3)
    @DefaultInt(5)
    private int stay;

    @InteractionTarget(type = InteractionType.ANVIL, offset = 4)
    @DefaultInt(1)
    private int fadeout;
}
