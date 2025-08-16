package org.housingstudio.hsl.exporter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.generic.EventType;
import org.housingstudio.hsl.exporter.generic.Command;
import org.housingstudio.hsl.exporter.generic.Function;
import org.housingstudio.hsl.exporter.generic.Region;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
public class House {
    private @NotNull Map<EventType, List<Action>> events;
    private @NotNull List<Function> functions;
    private @NotNull List<Command> commands;
    private @NotNull List<Region> regions;
}
