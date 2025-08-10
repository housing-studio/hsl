package org.hsl.std.type.location;

import org.hsl.compiler.debug.Constant;
import org.jetbrains.annotations.NotNull;

public interface Location extends Constant {
    @NotNull LocationType type();
}
