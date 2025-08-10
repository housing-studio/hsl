package org.hsl.compiler.ast.impl.declaration;

import org.hsl.compiler.ast.impl.type.Type;
import org.hsl.compiler.ast.impl.value.Value;
import org.hsl.compiler.token.Token;
import org.jetbrains.annotations.NotNull;

public record Parameter(@NotNull Token name, @NotNull Type type, @NotNull Value defaultValue) {
}
