package org.hsl.compiler.ast;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.hsl.compiler.ast.impl.declaration.CommandNode;
import org.hsl.compiler.ast.impl.declaration.ConstantDeclare;
import org.hsl.compiler.ast.impl.declaration.Event;
import org.hsl.compiler.ast.impl.declaration.Method;
import org.hsl.export.generic.EventType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@NodeInfo(type = NodeType.GAME)
@Accessors(fluent = true)
@Getter
public class Game extends Node {
    private final Map<String, Method> functions = new LinkedHashMap<>();
    private final Map<String, CommandNode> commands = new LinkedHashMap<>();
    private final Map<EventType, List<Event>> events = new LinkedHashMap<>();
    private final Map<String, ConstantDeclare> constants = new LinkedHashMap<>();
}
