package org.housingstudio.hsl.runtime;

import org.jetbrains.annotations.NotNull;

public interface Invocable {
    void invoke(@NotNull Frame parent);
}
