package org.hsl.std.type.slot;

import org.hsl.compiler.debug.Constant;
import org.jetbrains.annotations.NotNull;

public interface Slot extends Constant {
    @NotNull SlotType type();
}
