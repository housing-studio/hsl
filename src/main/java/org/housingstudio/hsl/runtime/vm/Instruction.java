package org.housingstudio.hsl.runtime.vm;

import org.jetbrains.annotations.NotNull;

public interface Instruction {
    void execute(@NotNull Frame frame);
}
