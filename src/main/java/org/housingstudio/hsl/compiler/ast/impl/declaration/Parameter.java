package org.housingstudio.hsl.compiler.ast.impl.declaration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.housingstudio.hsl.compiler.ast.impl.type.BaseType;
import org.housingstudio.hsl.compiler.ast.impl.value.Value;
import org.housingstudio.hsl.compiler.token.Token;
import org.housingstudio.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class Parameter {
    private final @NotNull Token name;
    private final @NotNull BaseType type;
    private final @Nullable Value defaultValue;

    public boolean isRequired() {
        return defaultValue == null;
    }

    public static @NotNull Parameter required(@NotNull String name, @NotNull BaseType type) {
        return new Parameter(Token.of(TokenType.IDENTIFIER, name), type, null);
    }

    public static @NotNull Parameter optional(@NotNull String name, @NotNull BaseType type, @NotNull Value defaultValue) {
        return new Parameter(Token.of(TokenType.IDENTIFIER, name), type, defaultValue);
    }
}
