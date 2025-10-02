package org.housingstudio.hsl.std.slot;

import org.housingstudio.hsl.compiler.debug.ConstantValue;
import org.jetbrains.annotations.NotNull;

public interface Slot extends ConstantValue {
    @NotNull SlotType type();
}
