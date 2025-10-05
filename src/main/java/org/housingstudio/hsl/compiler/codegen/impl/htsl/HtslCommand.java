package org.housingstudio.hsl.compiler.codegen.impl.htsl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class HtslCommand {
    private final @NotNull String pattern;

    public @NotNull HtslInvocation invoke() {
        return new HtslInvocation(pattern);
    }
}
