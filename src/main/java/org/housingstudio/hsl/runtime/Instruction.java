package org.housingstudio.hsl.runtime;

import org.jetbrains.annotations.NotNull;

public interface Instruction {
    void execute(@NotNull Frame frame);
}
