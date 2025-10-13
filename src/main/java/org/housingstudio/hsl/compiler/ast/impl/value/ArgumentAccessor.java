package org.housingstudio.hsl.compiler.ast.impl.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.NodeInfo;
import org.housingstudio.hsl.compiler.ast.NodeType;
import org.housingstudio.hsl.compiler.ast.impl.local.Variable;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.housingstudio.hsl.std.Namespace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@NodeInfo(type = NodeType.MACRO_CALL)
public class ArgumentAccessor implements Variable {
    private final @NotNull String name;
    private final @NotNull Type type;
    private final @NotNull Value argument;

    @Override
    public @NotNull Namespace namespace() {
        return Namespace.PLAYER; // should have no namespace
    }

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull Type type() {
        return type;
    }

    @Override
    public @Nullable Value load() {
        return argument;
    }
}
