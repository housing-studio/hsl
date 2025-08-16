package org.housingstudio.hsl.compiler.ast.builder;

import org.housingstudio.hsl.exporter.generic.Command;
import org.jetbrains.annotations.NotNull;

public interface CommandBuilder extends Builder {
    @NotNull Command buildCommand();
}
