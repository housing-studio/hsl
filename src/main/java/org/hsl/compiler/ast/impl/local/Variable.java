package org.hsl.compiler.ast.impl.local;

import org.hsl.compiler.ast.impl.scope.Statement;
import org.hsl.std.type.Namespace;
import org.hsl.compiler.ast.impl.type.Type;
import org.jetbrains.annotations.NotNull;

public abstract class Variable extends Statement {
    public abstract @NotNull Namespace namespace();
    public abstract @NotNull String name();
    public abstract @NotNull Type type();
}
