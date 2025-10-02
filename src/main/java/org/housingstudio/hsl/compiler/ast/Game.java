package org.housingstudio.hsl.compiler.ast;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.impl.declaration.*;
import org.housingstudio.hsl.compiler.codegen.impl.generic.EventType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@NodeInfo(type = NodeType.GAME)
@Accessors(fluent = true)
@Getter
public class Game extends Node {
    private final Map<String, Method> functions = new LinkedHashMap<>();
    private final Map<String, Macro> macros = new LinkedHashMap<>();
    private final Map<String, CommandNode> commands = new LinkedHashMap<>();
    private final Map<EventType, List<Event>> events = new LinkedHashMap<>();
    private final Map<String, Constant> constants = new LinkedHashMap<>();
}
