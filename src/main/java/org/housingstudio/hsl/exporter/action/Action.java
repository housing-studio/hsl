package org.housingstudio.hsl.exporter.action;

import org.jetbrains.annotations.NotNull;

public interface Action {
    @NotNull ActionType type();
}
