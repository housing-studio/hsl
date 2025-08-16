package org.housingstudio.hsl.export.action;

import org.jetbrains.annotations.NotNull;

public interface Action {
    @NotNull ActionType type();
}
