package org.housingstudio.hsl.std.slot;

import org.housingstudio.hsl.compiler.debug.Constant;
import org.jetbrains.annotations.NotNull;

public interface Slot extends Constant {
    @NotNull SlotType type();
}
