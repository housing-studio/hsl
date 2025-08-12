package org.hsl.compiler.ast.builder;

import org.hsl.export.generic.Function;
import org.jetbrains.annotations.NotNull;

public interface FunctionBuilder extends Builder {
    @NotNull Function buildFunction();
}
