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
import org.housingstudio.hsl.importer.interaction.defaults.Required;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class DisplayMenu implements Action {
    private final ActionType type = ActionType.DISPLAY_MENU;

    @InteractionTarget(type = InteractionType.DYNAMIC_OPTION, offset = 0)
    @Required
    private @NotNull String menu;
}
