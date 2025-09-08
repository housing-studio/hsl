package org.housingstudio.hsl.compiler.ast.impl.local;

import org.housingstudio.hsl.compiler.ast.impl.scope.Statement;
import org.housingstudio.hsl.std.Namespace;
import org.housingstudio.hsl.compiler.ast.impl.type.Type;
import org.jetbrains.annotations.NotNull;

public abstract class Variable extends Statement {
    public abstract @NotNull Namespace namespace();
    public abstract @NotNull String name();
    public abstract @NotNull Type type();
}
