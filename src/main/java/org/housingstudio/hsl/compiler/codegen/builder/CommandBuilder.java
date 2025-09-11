package org.housingstudio.hsl.compiler.codegen.builder;

import org.housingstudio.hsl.compiler.codegen.impl.generic.Command;
import org.jetbrains.annotations.NotNull;

public interface CommandBuilder extends Builder {
    @NotNull Command buildCommand();
}
