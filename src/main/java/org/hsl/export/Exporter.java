package org.hsl.export;

import lombok.experimental.UtilityClass;
import org.hsl.compiler.ast.Game;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class Exporter {
    public @NotNull House export(@NotNull Game game) {
        return new House();
    }
}
