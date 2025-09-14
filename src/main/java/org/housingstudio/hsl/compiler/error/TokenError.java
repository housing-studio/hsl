package org.housingstudio.hsl.compiler.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class TokenError {
    private final @NotNull List<Token> tokens;
    private final @NotNull String message;
}
