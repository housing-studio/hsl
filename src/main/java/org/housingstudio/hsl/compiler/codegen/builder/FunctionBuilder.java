package org.housingstudio.hsl.compiler.codegen.builder;

import org.housingstudio.hsl.compiler.codegen.impl.generic.Function;
import org.jetbrains.annotations.NotNull;

public interface FunctionBuilder extends Builder {
    @NotNull Function buildFunction();
}
