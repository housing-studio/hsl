package org.hsl.compiler.ast.impl.declaration;

import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.token.Token;
import org.hsl.compiler.token.TokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Parameter(@NotNull Token name, @NotNull Type type, @Nullable Value defaultValue) {
    public static @NotNull Parameter required(@NotNull String name, @NotNull Type type) {
        return new Parameter(Token.of(TokenType.IDENTIFIER, name), type, null);
    }

    public static @NotNull Parameter optional(@NotNull String name, @NotNull Type type, @NotNull Value defaultValue) {
        return new Parameter(Token.of(TokenType.IDENTIFIER, name), type, defaultValue);
    }
}
