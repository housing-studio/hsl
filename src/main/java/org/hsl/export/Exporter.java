package org.hsl.export;

import lombok.experimental.UtilityClass;
import org.hsl.compiler.ast.Game;
import org.hsl.compiler.ast.impl.declaration.CommandNode;
import org.hsl.compiler.ast.impl.declaration.Method;
import org.hsl.export.action.Action;
import org.hsl.export.generic.Command;
import org.hsl.export.generic.EventType;
import org.hsl.export.generic.Function;
import org.hsl.export.generic.Region;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class Exporter {
    public @NotNull House export(@NotNull Game game) {
        Map<EventType, List<Action>> events = new HashMap<>();
        List<Function> functions = new ArrayList<>();
        List<Command> commands = new ArrayList<>();
        List<Region> regions = new ArrayList<>();

        for (Method method : game.functions().values())
            functions.add(method.buildFunction());

        for (CommandNode value : game.commands().values())
            commands.add(value.buildCommand());

        return new House(events, functions, commands, regions);
    }
}
