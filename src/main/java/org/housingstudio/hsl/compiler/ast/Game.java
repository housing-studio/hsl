package org.housingstudio.hsl.compiler.ast;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.impl.declaration.*;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Enum;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.compiler.codegen.hierarchy.Children;
import org.housingstudio.hsl.compiler.codegen.hierarchy.ChildrenResolver;
import org.housingstudio.hsl.compiler.codegen.impl.generic.EventType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@NodeInfo(type = NodeType.GAME)
@Accessors(fluent = true)
@Getter
public class Game extends Node {
    @Children(resolver = MapValueChildResolver.class)
    private final Map<String, Method> functions = new LinkedHashMap<>();

    @Children(resolver = MapValueChildResolver.class)
    private final Map<String, Macro> macros = new LinkedHashMap<>();

    @Children(resolver = MapValueChildResolver.class)
    private final Map<String, CommandNode> commands = new LinkedHashMap<>();

    @Children(resolver = MapValueChildResolver.class)
    private final Map<EventType, List<Event>> events = new LinkedHashMap<>();

    @Children(resolver = MapValueChildResolver.class)
    private final Map<String, Constant> constants = new LinkedHashMap<>();

    @Children(resolver = MapValueChildResolver.class)
    private final Map<String, Enum> enums = new LinkedHashMap<>();

    public static class MapValueChildResolver implements ChildrenResolver<Map<?, ?>> {
        @Override
        public @NotNull List<Node> resolveChildren(@NotNull Map<?, ?> map) {
            List<Node> result = new ArrayList<>();
            for (Object value : map.values()) {
                if (value instanceof Node)
                    result.add((Node) value);
                else if (value instanceof List) {
                    for (Object o : ((List<?>) value)) {
                        if (o instanceof Node)
                            result.add((Node) o);
                    }
                }
            }
            return result;
        }
    }

    @Override
    public @Nullable Type resolveType(@NotNull String name) {
        Enum anEnum = enums.get(name);
        if (anEnum != null)
            return anEnum;

        // TODO check other user-defined types here
        return null;
    }
}
