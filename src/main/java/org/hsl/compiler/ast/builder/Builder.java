package org.hsl.compiler.ast.builder;

import org.jetbrains.annotations.NotNull;

public interface Builder<T> {
    @NotNull T build();
}
