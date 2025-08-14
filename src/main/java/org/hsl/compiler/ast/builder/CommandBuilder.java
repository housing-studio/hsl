package org.hsl.compiler.ast.builder;

import org.hsl.export.generic.Command;
import org.jetbrains.annotations.NotNull;

public interface CommandBuilder extends Builder {
    @NotNull Command buildCommand();
}
