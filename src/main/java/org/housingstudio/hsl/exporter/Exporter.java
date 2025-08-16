package org.housingstudio.hsl.exporter;

import lombok.experimental.UtilityClass;
import org.housingstudio.hsl.compiler.ast.Game;
import org.housingstudio.hsl.compiler.ast.impl.declaration.CommandNode;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Event;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Method;
import org.housingstudio.hsl.exporter.action.Action;
import org.housingstudio.hsl.exporter.generic.Command;
import org.housingstudio.hsl.exporter.generic.EventType;
import org.housingstudio.hsl.exporter.generic.Function;
import org.housingstudio.hsl.exporter.generic.Region;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class Exporter {
    public @NotNull House export(@NotNull Metadata metadata, @NotNull Game game) {
        Map<EventType, List<Action>> events = new HashMap<>();
        List<Function> functions = new ArrayList<>();
        List<Command> commands = new ArrayList<>();
        List<Region> regions = new ArrayList<>();

        for (Method method : game.functions().values())
            functions.add(method.buildFunction());

        for (CommandNode value : game.commands().values())
            commands.add(value.buildCommand());

        for (List<Event> eventList : game.events().values()) {
            for (Event event : eventList) {
                events
                    .computeIfAbsent(event.type(), k -> new ArrayList<>())
                    .addAll(event.buildActionList());
            }
        }

        return new House(metadata, events, functions, commands, regions);
    }
}
