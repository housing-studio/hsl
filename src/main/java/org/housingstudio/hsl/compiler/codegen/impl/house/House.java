package org.housingstudio.hsl.compiler.codegen.impl.house;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.codegen.impl.action.Action;
import org.housingstudio.hsl.compiler.codegen.impl.generic.EventType;
import org.housingstudio.hsl.compiler.codegen.impl.generic.Command;
import org.housingstudio.hsl.compiler.codegen.impl.generic.Function;
import org.housingstudio.hsl.compiler.codegen.impl.generic.Region;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@ToString
public class House {
    private @NotNull Metadata metadata;
    private @NotNull Map<EventType, List<Action>> events;
    private @NotNull List<Function> functions;
    private @NotNull List<Command> commands;
    private @NotNull List<Region> regions;
}
