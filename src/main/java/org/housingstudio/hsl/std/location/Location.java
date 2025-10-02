package org.housingstudio.hsl.std.location;

import org.housingstudio.hsl.compiler.debug.ConstantValue;
import org.jetbrains.annotations.NotNull;

public interface Location extends ConstantValue {
    @NotNull LocationType type();
}
