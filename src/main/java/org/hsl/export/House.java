package org.hsl.export;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hsl.export.action.Action;
import org.hsl.export.generic.EventType;
import org.hsl.export.generic.Command;
import org.hsl.export.generic.Function;
import org.hsl.export.generic.Region;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class House {
    private @NotNull Map<EventType, List<Action>> events;
    private @NotNull Map<String, Function> functions;
    private @NotNull Map<String, Command> commands;
    private @NotNull Map<String, Region> regions;
}
