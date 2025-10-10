package org.housingstudio.hsl.compiler.ast.impl.placeholder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.impl.declaration.Method;
import org.housingstudio.hsl.compiler.parser.impl.action.ArgAccess;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class Placeholder {
    private final @NotNull Method method;
    private final @NotNull Function<ArgAccess, Result> mapper;
}
