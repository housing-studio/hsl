package org.housingstudio.hsl.type.location;

import org.housingstudio.hsl.compiler.debug.Constant;
import org.jetbrains.annotations.NotNull;

public interface Location extends Constant {
    @NotNull LocationType type();
}
