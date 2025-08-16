package org.housingstudio.hsl.compiler.ast.builder;

import org.housingstudio.hsl.export.generic.Function;
import org.jetbrains.annotations.NotNull;

public interface FunctionBuilder extends Builder {
    @NotNull Function buildFunction();
}
