package org.housingstudio.hsl.compiler.ast.impl.local;

import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.std.Namespace;
import org.housingstudio.hsl.compiler.ast.impl.type.BaseType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Variable {
    @NotNull Namespace namespace();
    @NotNull String name();
    @NotNull BaseType type();

    default @Nullable Value load() {
        return null;
    }
}
