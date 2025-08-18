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
import org.housingstudio.hsl.importer.interaction.defaults.DefaultMode;
import org.housingstudio.hsl.type.Mode;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class ChangeHungerLevel implements Action {
    private final ActionType type = ActionType.CHANGE_HUNGER_LEVEL;

    @InteractionTarget(type = InteractionType.ANVIL, offset = 0)
    @DefaultInt(20)
    private int level;

    @InteractionTarget(type = InteractionType.MODE, offset = 1)
    @DefaultMode(Mode.SET)
    private @NotNull Mode mode;
}
