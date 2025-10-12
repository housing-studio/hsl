package org.housingstudio.hsl.compiler.codegen.interop.htsl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class HtslCommand {
    private final @NotNull String pattern;

    public @NotNull HtslInvocation invoke() {
        return new HtslInvocation(pattern);
    }
}
